package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote.SessionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.NotRegularFileException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.SimpleFileReceiver;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.PathFileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.root1.simon.RawChannel;
import de.root1.simon.Simon;

public class NFileManager extends Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static enum BasicDir
	{
		Music, Pictures, Videos
	};

	public static enum FileType
	{
		DIR, FILE, FILE_OR_DIR;
	}

	public static final byte			VALIDATED		= 0;
	public static final byte			NO_SUCH_FILE	= 1;
	public static final byte			NOT_DIRECTORY	= 2;
	public static final byte			NOT_FILE		= 3;

	public volatile static NFileManager	instance;

	// --------------------------------------------------------------------------------
	// -- Static Methods ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static NFileManager getInstance() throws Exception
	{
		if (instance == null)
		{
			synchronized (NFileManager.class)
			{
				if (instance == null)
				{
					instance = new NFileManager();
					instance.start();
				}
			}
		}
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final Path			userFilesPath;
	private Map<String, Long>	elapsedTimeMap;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private NFileManager() throws Exception
	{
		super(Level.ALL);

		userFilesPath = ServerMain.SERVER_PATH.resolve("USER_FILES");
		NIOUtil.createDirs(userFilesPath);
		logger.info("User files @ {}", userFilesPath);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public Path getUserRootPath(long id)
	{
		return userFilesPath.resolve(id + "");
	}

	// --------------------------------------------------------------------------------
	public Path toRelativeUserPath(Path absoluteServerPath, long id)
	{
		return getUserRootPath(id).relativize(absoluteServerPath);
	}

	// --------------------------------------------------------------------------------
	public Path toAbsoluteServerPath(Path relativeUserPath, long id)
	{
		return getUserRootPath(id).resolve(relativeUserPath);
	}

	// --------------------------------------------------------------------------------
	public void createUserDirs(long id) throws IOException
	{
		for (BasicDir oneDir : BasicDir.values())
		{
			NIOUtil.createDirs(getUserRootPath(id).resolve(oneDir.toString()));
		}
	}

	// --------------------------------------------------------------------------------
	public Path toValidatedAbsoluteServerPath(Session session, String relativeUserPath, FileType expectedFileType, boolean needsWriteAccess)
			throws FileSystemException
	{
		logger.debug("ENTRY {} {} {} {}", new Object[] { session, relativeUserPath, expectedFileType, needsWriteAccess });

		Path absolutePath = toAbsoluteServerPath(Paths.get(relativeUserPath), session.getId());
		logger.debug("absolutePath: {}", absolutePath);

		// Check if user can access
		if (!grantAccess(session, absolutePath, needsWriteAccess))
		{
			if (needsWriteAccess)
				throw new AccessDeniedException(relativeUserPath, null, "write");
			else
				throw new AccessDeniedException(relativeUserPath, null, "read");
		}

		// Check if file type equals expected file type
		switch (validateFileType(absolutePath, expectedFileType))
		{
			case NO_SUCH_FILE:
				throw new NoSuchFileException(relativeUserPath);
			case NOT_DIRECTORY:
				throw new NotDirectoryException(relativeUserPath);
			case NOT_FILE:
				throw new NotRegularFileException(relativeUserPath);
		}

		logger.debug("EXIT {}", absolutePath);
		return absolutePath;
	}

	// --------------------------------------------------------------------------------
	public List<FileInfo> listFileInfos(SessionImpl session, Path dir) throws IOException
	{
		ListFileInfosVisitor visitor = this.new ListFileInfosVisitor(session);
		Files.walkFileTree(dir, EnumSet.noneOf(FileVisitOption.class), 1, visitor);
		return visitor.getFileInfos();
	}
	
	// --------------------------------------------------------------------------------
	public List<FileInfo> listAllFileInfos(SessionImpl session, Path dir) throws IOException
	{
		ListFileInfosVisitor visitor = this.new ListFileInfosVisitor(session);
		Files.walkFileTree(dir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, visitor);
		return visitor.getFileInfos();
	}

	// --------------------------------------------------------------------------------
	public void saveElapsedTime(String mrl, long time)
	{
		logger.trace("{}=>{}", mrl, time);
		elapsedTimeMap.put(mrl, time);
	}

	// --------------------------------------------------------------------------------
	public long getElapsedTime(String mrl)
	{
		Long time = elapsedTimeMap.get(mrl);
		logger.trace("{}={}", mrl, time);
		return time != null ? time : -1L;
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	protected void onStart() throws Exception
	{
		elapsedTimeMap = Collections.synchronizedMap(readElapsedTimeFromDatabase(DatabaseManager.getInstance().getClientConnection()));
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onShutdown() throws Exception
	{
		if (elapsedTimeMap != null)
		{
			saveElapsedTimeToDatabase(DatabaseManager.getInstance().getClientConnection(), elapsedTimeMap);
			elapsedTimeMap.clear();
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private boolean isWithinUserRoot(Path path, long id)
	{
		// needs to be normalized to resolve things as ".." (parent directory)
		return path.normalize().startsWith(getUserRootPath(id));
	}

	// --------------------------------------------------------------------------------
	private boolean equalsUserRoot(Path path, long id)
	{
		return path.equals(getUserRootPath(id));
	}

	// --------------------------------------------------------------------------------
	private boolean equalsUserBasicDir(Path path, long id)
	{
		for (BasicDir oneDir : BasicDir.values())
		{
			if (getUserRootPath(id).resolve(oneDir.toString()).equals(path))
				return true;
		}
		return false;
	}

	// --------------------------------------------------------------------------------
	/**
	 * @param path
	 * @param expectedFileType
	 * @return 0 if validated. 1 if does not exist. 2 if expected directory but found sth else. 3 if expected regular file but found sth else.
	 */
	private byte validateFileType(Path path, FileType expectedFileType)
	{
		logger.debug("checking {} {}", new Object[] { path, expectedFileType });
		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS))
			return NO_SUCH_FILE;
		switch (expectedFileType)
		{
			case DIR:
				if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
					return NOT_DIRECTORY;
				break;
			case FILE:
				if (!NIOUtil.isRegularFile(path))
					return NOT_FILE;
				break;
		}
		return VALIDATED;
	}

	// --------------------------------------------------------------------------------
	/**
	 * 
	 * To be sure, users do not get access to directories other than their own. f.i. via session.listFiles("..") -> would point at the parent
	 * directory. Furthermore, users cannot alter their root dir or basic dirs
	 * 
	 * @param session
	 * @param path
	 * @param needsWriteAccess
	 * @return
	 */
	private boolean grantAccess(Session session, Path path, boolean needsWriteAccess)
	{
		logger.debug("checking {} {} {}", new Object[] { session, path, needsWriteAccess });
		if (!isWithinUserRoot(path, session.getId()))
		{
			logger.trace("Read access denied: isWithinUserRoot = false");
			return false;
		}

		if (needsWriteAccess)
		{
			if (equalsUserRoot(path, session.getId()))
			{
				logger.trace("Write access denied: equalsUserRoot = true");
				return false;
			}
			if (equalsUserBasicDir(path, session.getId()))
			{
				logger.trace("Write access denied: equalsUserBasicDir = true");
				return false;
			}
		}
		return true;
	}

	// --------------------------------------------------------------------------------
	// -- FileVisitors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private class ListFileInfosVisitor extends SimpleFileVisitor<Path>
	{
		private List<FileInfo>		infos	= null;
		private final SessionImpl	session;

		public ListFileInfosVisitor(SessionImpl session)
		{
			this.session = session;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			if (infos == null)
				infos = new ArrayList<>();

			PathFileInfo fi = new PathFileInfo(toRelativeUserPath(file, session.getId()), NIOUtil.probeContentType(file), attrs, grantAccess(session, file, true));
			// Set the elapsed time if the mrl exists in map, otherwise -1L is returned
			fi.setElapsedTime(getElapsedTime(NIOUtil.pathToUri(file)));
			infos.add(fi);
			
			return FileVisitResult.CONTINUE;
		}

		public List<FileInfo> getFileInfos()
		{
			if (infos == null)
				return Collections.emptyList();
			else
				return infos;
		}
	}

	// --------------------------------------------------------------------------------
	public void sendFile(ClientCallback callback, Path path) throws IOException
	{
		RawChannel rawChannel = null;
		ReadableByteChannel rbc = null;
		try
		{
			// get a RawChannel Token from server. This is needed to open the RawChannel
			long fileSize = NIOUtil.sizePerFile(path);
			int token = callback.prepareRawChannel(path.getFileName().toString(), fileSize);

			// with the remote object and token, tell SIMON that you need a RawChannel
			rawChannel = Simon.openRawChannel(token, callback);

			// first, we open a ReadableByteChannel. This is thanks to Java NIO faster than normal file operation
			rbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ));

			// we send the file in 8kb packages through the RawChannel
			ByteBuffer data = ByteBuffer.allocate(8 * 1024);
			while (rbc.read(data) != -1)
			{
				// System.out.println("Read " + data.limit());
				rawChannel.write(data);
				data.clear();
			}
		}
		finally
		{
			// all data written. Now we can close the ReadableByteChannel
			if (rbc != null)
				rbc.close();

			// ... and also the RawChannel
			if (rawChannel != null)
				rawChannel.close();
		}
	}

	// --------------------------------------------------------------------------------
	public class FileReceiver extends SimpleFileReceiver
	{
		private long	start;
		private long	totalBytesWritten;

		public FileReceiver(Path dest, long fileSize, ExistOption option) throws FileAlreadyExistsException
		{
			super(dest, fileSize, option);
		}

		protected void onStarted(Path dest, long fileSize)
		{
			start = System.currentTimeMillis();
			logger.debug("Starting upload of {}", dest);
		}

		@Override
		protected void onWritten(Path dest, int bytesWritten)
		{
			totalBytesWritten += bytesWritten;
		}

		@Override
		protected void onClosed(Path dest)
		{
			if (logger.isDebugEnabled())
			{
				long duration = System.currentTimeMillis() - start;
				logger.debug(
						"Successfully uploaded {} ({}) in {} -> {}/s)",
						new Object[] { dest, ByteValue.bytesToString(totalBytesWritten), TimeValue.formatMillis(duration, true, false),
								ByteValue.bytesToString((long) ProgressUtil.speed(totalBytesWritten, duration)) });
			}

		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Map<String, Long> readElapsedTimeFromDatabase(Connection con) throws SQLException, IOException
	{
		Statement st = null;
		ResultSet rs = null;
		try
		{
			String sql = DatabaseManager.getSQL("PSSelectMedia.sql");
			st = con.createStatement();
			rs = st.executeQuery(sql);
			HashMap<String, Long> map = new HashMap<>();
			while (rs.next())
			{
				map.put(rs.getString("mrl"), rs.getLong("elapsed_time"));
			}
			logger.info("Read elapsedTimes: {}", map);
			return map;
		}
		finally
		{
			DatabaseManager.close(st);
			DatabaseManager.close(rs);
		}
	}

	// --------------------------------------------------------------------------------
	private void saveElapsedTimeToDatabase(Connection con, Map<String, Long> elapsedTimeMap) throws SQLException, IOException
	{
		logger.info("Saving elapsedTimes: {}", elapsedTimeMap);

		if (elapsedTimeMap.isEmpty())
			return;

		PreparedStatement psUpdate = null;
		PreparedStatement psInsert = null;
		try
		{
			psUpdate = con.prepareStatement(DatabaseManager.getSQL("PSUpdateMedia.sql"));

			for (Map.Entry<String, Long> oneEntry : elapsedTimeMap.entrySet())
			{
				// Update
				psUpdate.setLong(1, oneEntry.getValue());
				psUpdate.setString(2, oneEntry.getKey());
				logger.trace("psUpdate: {}", psUpdate);

				int affectedRows = psUpdate.executeUpdate();
				// If 0 rows were affected, the mrl was not inserted yet.
				// Insert
				if (affectedRows == 0)
				{
					logger.info("Entry did not exist yet. Inserting {}={}", oneEntry.getKey(), oneEntry.getValue());

					// Initializing the Prepared Statement
					if (psInsert == null)
						psInsert = con.prepareStatement(DatabaseManager.getSQL("PSInsertMedia.sql"));

					psInsert.setString(1, oneEntry.getKey());
					psInsert.setLong(2, oneEntry.getValue());
					logger.trace("psInsert: {}", psInsert);

					affectedRows = psInsert.executeUpdate();
					if (affectedRows != 1)
						throw new SQLException("Unexpected affectedRows on insert for " + oneEntry + ": " + affectedRows);
				}
				else if (affectedRows != 1)
				{
					throw new SQLException("Unexpected affectedRows on update for " + oneEntry + ": " + affectedRows);
				}
			}

			con.commit();
		}
		catch (Exception e)
		{
			logger.warn("Rolling back due to: " + e.toString());
			DatabaseManager.rollback(con);
			throw e;
		}
		finally
		{
			DatabaseManager.close(psUpdate);
			DatabaseManager.close(psInsert);
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
