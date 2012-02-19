package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.NotRegularFileException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;
import de.root1.simon.RawChannel;
import de.root1.simon.RawChannelDataListener;
import de.root1.simon.Simon;

public class NFileManager extends Manager
{
	public static final Path	USER_FILES_DIR				= new File(ServerMain.SERVER_DIR, "USER_FILES").toPath();
	public static final char[]	ILLEGAL_CHARS_IN_FILENAME	= "\\/:*?<>|%&".toCharArray();

	public static enum BasicDir
	{
		Music, Pictures, Videos
	};

	static enum FileType
	{
		DIR, FILE, FILE_OR_DIR;
	}

	// --------------------------------------------------------------------------------
	protected NFileManager() throws Exception
	{
		super();
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void init() throws Exception
	{
		initLogging(Level.ALL);
		try
		{
			NIOUtil.createAllDirs(USER_FILES_DIR);
		}
		catch (IOException e)
		{
			throw new IOException("Could not create user files directory: " + USER_FILES_DIR, e);
		}

	}

	// --------------------------------------------------------------------------------
	@Override
	protected void rollbackInit() throws Exception
	{
		// Nothing to do
	}

	// --------------------------------------------------------------------------------
	public Path getUserRootPath(long id)
	{
		return USER_FILES_DIR.resolve(id + "");
	}

	// --------------------------------------------------------------------------------
	public Path getRelativeUserPath(Path path, long id)
	{
		return getUserRootPath(id).relativize(path);
	}

	// --------------------------------------------------------------------------------
	public boolean isWithinUserRoot(Path path, long id)
	{
		return path.startsWith(getUserRootPath(id));
	}

	// --------------------------------------------------------------------------------
	public boolean equalsUserRoot(Path path, long id)
	{
		return path.equals(getUserRootPath(id));
	}

	// --------------------------------------------------------------------------------
	public boolean equalsUserBasicDir(Path path, long id)
	{
		for (BasicDir oneDir : BasicDir.values())
		{
			if (getUserRootPath(id).resolve(oneDir.toString()).equals(path))
				return true;
		}
		return false;
	}

	// --------------------------------------------------------------------------------
	public void createUserDirs(long id) throws IOException
	{
		NIOUtil.createDir(USER_FILES_DIR, id + "");
		for (BasicDir oneDir : BasicDir.values())
		{
			NIOUtil.createDir(getUserRootPath(id), oneDir.toString());
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * @param path
	 * @param expectedFileType
	 * @return 0 if validated. 1 if does not exist. 2 if expected directory but found sth else. 3 if expected file but found sth else.
	 */
	public byte validateFileType(Path path, FileType expectedFileType)
	{
		logger.debug("ENTRY {} {}", new Object[] { path, expectedFileType });
		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS))
			return 1;
		switch (expectedFileType)
		{
			case DIR:
				if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
					return 2;
				break;
			case FILE:
				if (!NIOUtil.isRegularFile(path))
					return 3;
				break;
		}
		return 0;
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
	public boolean grantAccess(SessionImpl session, Path path, boolean needsWriteAccess)
	{
		logger.debug("ENTRY {} {} {}", new Object[] { session, path, needsWriteAccess });
		if (!isWithinUserRoot(path, session.getId()))
		{
			logger.trace("Access denied: isWithinUserRoot = false");
			return false;
		}

		if (needsWriteAccess)
		{
			if (equalsUserRoot(path, session.getId()))
			{
				logger.trace("Access denied: equalsUserRoot = true");
				return false;
			}
			if (equalsUserBasicDir(path, session.getId()))
			{
				logger.trace("Access denied: equalsUserBasicDir = true");
				return false;
			}
		}
		return true;
	}

	// --------------------------------------------------------------------------------
	public Path uriStringToPath(SessionImpl session, String uri, FileType expectedFileType, boolean needsWriteAccess) throws FileSystemException
	{
		logger.debug("ENTRY {} {} {} {}", new Object[] { session, uri, expectedFileType, needsWriteAccess });

		Path userFile = getUserRootPath(session.getId()).resolve(uri);
		logger.debug("userFile: {}", userFile);

		// Check if user can access
		if (!grantAccess(session, userFile, needsWriteAccess))
		{
			if (needsWriteAccess)
				throw new AccessDeniedException(uri, null, "write");
			else
				throw new AccessDeniedException(uri, null, "read");
		}

		switch (validateFileType(userFile, expectedFileType))
		{
			case 1:
				throw new NoSuchFileException(uri);
			case 2:
				throw new NotDirectoryException(uri);
			case 3:
				throw new NotRegularFileException(uri);
		}

		logger.debug("EXIT {}", userFile);
		return userFile;
	}

	// --------------------------------------------------------------------------------
	public List<FileInfo> listFileInfos(SessionImpl session, Path dir) throws IOException
	{
		ListFileInfosVisitor visitor = this.new ListFileInfosVisitor(session, dir);
		Files.walkFileTree(dir, EnumSet.noneOf(FileVisitOption.class), 1, visitor);
		return visitor.getFileInfos();
	}

	// --------------------------------------------------------------------------------
	// -- FileVisitors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private class ListFileInfosVisitor extends SimpleFileVisitor<Path>
	{
		private List<FileInfo>		infos	= null;
		private final SessionImpl	session;
		private final Path			parent;

		public ListFileInfosVisitor(SessionImpl session, Path parent)
		{
			this.session = session;
			this.parent = parent;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			if (infos == null)
				infos = new ArrayList<>();
			infos.add(new FileInfo(file, getUserRootPath(session.getId()), attrs, grantAccess(session, file, true)));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
		{
			if (infos == null)
				infos = new ArrayList<>();
			if (!dir.equals(parent)) // do not add the parent directory
				infos.add(new FileInfo(dir, getUserRootPath(session.getId()), attrs, grantAccess(session, dir, true)));
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
			long fileSize = (Long) Files.getAttribute(path, "size", LinkOption.NOFOLLOW_LINKS);
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
	public class FileReceiver implements RawChannelDataListener
	{
		private final Path			dest;
		private final long			fileSize;
		private WritableByteChannel	wbc;
		private long				start;

		FileReceiver(Path dest, long fileSize) throws IOException
		{
			logger.debug("ENTRY {} {}", dest, fileSize);
			if (Files.exists(dest, LinkOption.NOFOLLOW_LINKS))
				throw new IllegalArgumentException("AUTORENAME not supported yet!"); // TODO
			if(fileSize == 0L)
				throw new IllegalArgumentException("fileSize == 0. No data will be uploaded."); // TODO
			this.fileSize = fileSize;
			this.dest = dest;
		}

		public void write(ByteBuffer data)
		{
			try
			{
				if (start <= 0)
				{
					start = System.currentTimeMillis();
					logger.debug("Starting upload of {}", dest);
					this.wbc = Files.newByteChannel(dest, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));
				}
				wbc.write(data);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

		public void close()
		{
			try
			{
				long duration = System.currentTimeMillis() - start;
				if (logger.isDebugEnabled())
					logger.debug(
							"Successfully uploaded {} ({}) in {} -> {}/s)",
							new Object[] { dest, ByteValue.bytesToString(fileSize), TimeValue.formatMillis(duration, true, false),
									ByteValue.bytesToString((long) ProgressUtil.speed(fileSize, duration)) });
				if (wbc != null)
				{
					wbc.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
