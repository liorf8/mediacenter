package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

public class FileManager_deprecated extends Manager
{

	protected FileManager_deprecated() throws Exception
	{
		super();
		// TODO Auto-generated constructor stub
	}
//	// --------------------------------------------------------------------------------
//	// -- Static Variable(s) ----------------------------------------------------------
//	// --------------------------------------------------------------------------------
//	public static final File	USER_FILES_DIR				= new File(ServerMain.SERVER_DIR, "USER_FILES");
//	public static final char[]	ILLEGAL_CHARS_IN_FILENAME	= "\\/:*?<>|%&".toCharArray();
//
//	static enum BASIC_DIRS
//	{
//		Music, Pictures, Videos
//	};
//
//	static enum Role
//	{
//		USER, ADMIN
//	};
//
//	static enum FileType
//	{
//		DIR, FILE, FILE_OR_DIR;
//	}
//
//	// --------------------------------------------------------------------------------
//	// -- Constructors ----------------------------------------------------------------
//	// --------------------------------------------------------------------------------
//	FileManager() throws Exception
//	{
//		super();
//	}
//
//	@Override
//	protected void init() throws Exception
//	{
//		initLogging(Level.ALL);
//		try
//		{
//			IOUtil.executeMkFullDirPath(USER_FILES_DIR);
//		}
//		catch (IOException e)
//		{
//			throw new IOException("Could not create user files directory: " + USER_FILES_DIR, e);
//		}
//	}
//
//	@Override
//	protected void rollbackInit()
//	{
//		// TODO Auto-generated method stub
//	}
//
//	// --------------------------------------------------------------------------------
//	// -- Private/Packet Method(s) ----------------------------------------------------
//	// --------------------------------------------------------------------------------
//	File getUserRootDir(long id)
//	{
//		return new File(USER_FILES_DIR, "" + id);
//	}
//
//	// --------------------------------------------------------------------------------
//	File getUserBasicDir(long id, BASIC_DIRS dir)
//	{
//		return new File(getUserRootDir(id), dir.toString());
//	}
//
//	// --------------------------------------------------------------------------------
//	File[] getUserBasicDirs(long id)
//	{
//		logger.trace("ENTRY {}", id);
//		File[] basicDirs = new File[BASIC_DIRS.values().length];
//		int i = 0;
//		for (BASIC_DIRS oneDir : BASIC_DIRS.values())
//		{
//			basicDirs[i] = getUserBasicDir(id, oneDir);
//			i++;
//		}
//		if (logger.isTraceEnabled())
//			logger.trace("EXIT {}", Arrays.toString(basicDirs));
//		return basicDirs;
//	}
//
//	// --------------------------------------------------------------------------------
//	void createUserDirs(long id) throws IllegalArgumentException, IOException
//	{
//		logger.trace("ENTRY {}", id);
//		IOUtil.executeMkFullDirPath(getUserRootDir(id));
//		IOUtil.executeMkFullDirsPaths(getUserBasicDirs(id));
//		logger.trace("EXIT {}");
//	}
//
//	// --------------------------------------------------------------------------------
//	File uriToUserFile(SessionImpl session, String uri, FileType fileType, boolean wantToModify) throws IllegalArgumentException
//	{
//		logger.debug("ENTRY {} {} {} {}", new Object[] { session, uri, fileType, wantToModify });
//		File userFile = new File(getUserRootDir(session.getId()), FileInfo.uriPathToFilePath(uri));
//
//		// To be sure, users do not get access to directories other than their own.
//		// f.i. via session.listFiles("..") -> would point at the parent directory.
//		// furthermore users cannot alter their root dir or basic dirs
//		if (!validateAccess(session, userFile, wantToModify))
//		{
//			throw new AccessDeniedException(String.format("%sing access to path '%s' for user '%s' (%s) denied.", (wantToModify ? "Modify" : "Read"),
//					uri, session.getLogin(), session.getRole()));
//		}
//
//		// Check for existence and is dir / file
//		if (!userFile.exists())
//			throw new DoesNotExistException("File does not exist: " + uri);
//		switch (fileType)
//		{
//			case DIR:
//				if (!userFile.isDirectory())
//					throw new DoesNotExistException("File is no directory: " + uri);
//				break;
//			case FILE:
//				if (userFile.isDirectory())
//					throw new DoesNotExistException("File is a directory: " + uri);
//				break;
//		}
//		logger.debug("EXIT {}", userFile);
//		return userFile;
//	}
//
//	// --------------------------------------------------------------------------------
//	boolean validateAccess(SessionImpl session, File file, boolean wantToModify)
//	{
//		logger.debug("ENTRY {} {} {}", new Object[] { session, file, wantToModify });
//		boolean grantAccess = true;
//		try
//		{
//			long id = session.getId();
//			switch (session.getRole())
//			{
//				case USER:
//					// users can only access folders within their dir
//					if (!IOUtil.isOrIsInParentDir(getUserRootDir(id), file))
//					{
//						grantAccess = false;
//					}
//
//					if (wantToModify)
//					{
//						// users cannot modify their user root dir
//						if (file.equals(getUserRootDir(id)))
//						{
//							grantAccess = false;
//						}
//						// users also cannot modify their basic dirs
//						for (File oneBasicDir : getUserBasicDirs(id))
//						{
//							if (file.equals(oneBasicDir))
//							{
//								grantAccess = false;
//								break;
//							}
//						}
//					}
//					break;
//				case ADMIN:
//					// admins can access all user dirs
//					if (!IOUtil.isOrIsInParentDir(USER_FILES_DIR, file))
//					{
//						grantAccess = false;
//						logger.warn("Access denied to {}: File {} is not in parent dir!", file, id);
//					}
//					break;
//			}
//		}
//		catch (Exception e)
//		{
//			grantAccess = false;
//		}
//		logger.debug("EXIT {}", grantAccess);
//		return grantAccess;
//	}
//
//	// --------------------------------------------------------------------------------
//	/**
//	 * @param dir
//	 * @param parentDirToOmit
//	 *            Can be "" so that nothing is omitted.
//	 * @return
//	 * @throws IOException
//	 */
//	public FileInfo[] listFileInfos(SessionImpl session, File dir, File parentDirToOmit) throws IOException
//	{
//		File[] files = dir.listFiles();
//		FileInfo[] fileInfos = new FileInfo[files.length];
//		File oneFile = null;
//		boolean modifyable;
//		for (int i = 0; i < files.length; i++)
//		{
//			oneFile = files[i];
//			// check if the user denoted by the session can modify the file
//			modifyable = validateAccess(session, oneFile, true);
//			fileInfos[i] = new FileInfo(oneFile, parentDirToOmit, modifyable);
//		}
//		return fileInfos;
//	}
//
//	// --------------------------------------------------------------------------------
//	public void sendFile(ClientCallback callback, File file) throws IOException
//	{
//		RawChannel rawChannel = null;
//		FileChannel fileChannel = null;
//		try
//		{
//			// get a RawChannel Token from server. This is needed to open the RawChannel
//			int token = callback.prepareRawChannel(file.getName(), file.length());
//
//			// with the remote object and token, tell SIMON that you need a RawChannel
//			rawChannel = Simon.openRawChannel(token, callback);
//
//			// first, we open a FileChannel. This is thanks to Java NIO faster than normal file operation
//			fileChannel = new FileInputStream(file).getChannel();
//
//			// we send the file in 8kb packages through the RawChannel
//			ByteBuffer data = ByteBuffer.allocate(8 * 1024);
//			while (fileChannel.read(data) != -1)
//			{
//				// System.out.println("Read " + data.limit());
//				rawChannel.write(data);
//				data.clear();
//			}
//		}
//		finally
//		{
//			// all data written. Now we can close the FileChannel
//			if (fileChannel != null)
//				fileChannel.close();
//
//			// ... and also the RawChannel
//			if (rawChannel != null)
//				rawChannel.close();
//		}
//	}
//
//	// --------------------------------------------------------------------------------
//	public class FileReceiver implements RawChannelDataListener
//	{
//		private FileChannel	fc;
//		private long		fileSize;
//		private File		dest;
//		private long		start;
//
//		FileReceiver(File dest, long fileSize)
//		{
//			this.fileSize = fileSize;
//			this.dest = dest;
//		}
//
//		public void write(ByteBuffer data)
//		{
//			try
//			{
//				if (start <= 0)
//				{
//					start = System.currentTimeMillis();
//					logger.debug("Starting upload of {}", dest);
//					this.fc = new FileOutputStream(dest).getChannel();
//				}
//				fc.write(data);
//			}
//			catch (IOException ex)
//			{
//				ex.printStackTrace();
//			}
//		}
//
//		public void close()
//		{
//			try
//			{
//				long duration = System.currentTimeMillis() - start;
//				if (logger.isDebugEnabled())
//					logger.debug(
//							"Successfully uploaded {} ({}) in {} -> {}/s)",
//							new Object[] { dest, ByteValue.bytesToString(fileSize), TimeValue.formatMillis(duration, true, false),
//									ByteValue.bytesToString((long) ProgressUtil.speed(fileSize, duration)) });
//				if (fc != null)
//				{
//					fc.close();
//				}
//			}
//			catch (IOException ex)
//			{
//				ex.printStackTrace();
//			}
//		}
//	}
//
//	// --------------------------------------------------------------------------------
//	// --------------------------------------------------------------------------------
//	// --------------------------------------------------------------------------------

	@Override
	protected void init() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void rollbackInit() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}
