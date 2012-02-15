package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class FileManager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	// os: C:\\Users\\mhertram\\ALL_USERS_ROOT_DIR
	// max: C:\\Users\\Max\\ALL_USERS_ROOT_DIR
	private final File			USER_FILES_DIR				= new File("C:\\Users\\Max\\MedienCenter\\USER_FILES");
	public static final char[]	ILLEGAL_CHARS_IN_FILENAME	= "\\/:*?<>|%&".toCharArray();
	public final static Logger	logger						= Logger.getLogger(FileManager.class.getName());

	static enum BASIC_DIRS
	{
		Music, Pictures, Videos
	};

	static enum Role
	{
		USER, ADMIN
	};

	static enum FileType
	{
		DIR, FILE, FILE_OR_DIR;
	}

	private static FileManager	instance;

	public static synchronized FileManager getInstance() throws Exception
	{
		if (instance == null)
			instance = new FileManager();
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private FileManager() throws Exception
	{
		try
		{
			try
			{
				IOUtil.executeMkDir(USER_FILES_DIR);
			}
			catch (IOException e)
			{
				throw new Exception("Could not create user files directory: " + USER_FILES_DIR, e);
			}

			try
			{
				logger.setLevel(Level.ALL);
				logger.addHandler(new FileHandler(FileManager.class.getName() + ".log", false));
				logger.info("Logger started.");
			}
			catch (IOException e)
			{
				throw new Exception("Could not establish logging", e);
			}
		}
		catch (Exception e)
		{
			throw new Exception("FileManager.<init> failed: " + e.getMessage(), e.getCause());
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private/Packet Method(s) ----------------------------------------------------
	// --------------------------------------------------------------------------------
	File getUserRootDir(String user)
	{
		return new File(USER_FILES_DIR, user);
	}

	// --------------------------------------------------------------------------------
	File getUserBasicDir(String user, BASIC_DIRS dir)
	{
		return new File(getUserRootDir(user), dir.toString());
	}

	// --------------------------------------------------------------------------------
	File[] getUsersBasicDirs(String user)
	{
		File[] basicDirs = new File[BASIC_DIRS.values().length];
		int i = 0;
		for (BASIC_DIRS oneDir : BASIC_DIRS.values())
		{
			basicDirs[i] = getUserBasicDir(user, oneDir);
			i++;
		}
		return basicDirs;
	}

	// --------------------------------------------------------------------------------
	void createUserDirs(String user) throws IllegalArgumentException, IOException, ServerException
	{
		IOUtil.executeMkDir(getUserRootDir(user));
		IOUtil.executeMkDirs(getUsersBasicDirs(user));
	}

	// --------------------------------------------------------------------------------
	File uriToUserFile(SessionImpl session, String uri, FileType fileType, boolean wantToModify) throws IllegalArgumentException, ServerException
	{
		File userFile = new File(getUserRootDir(session.getUser()), FileInfo.uriPathToFilePath(uri));
		try
		{
			// To be sure, users do not get access to directories other than their own.
			// f.i. via session.listFiles("..") -> would point at the parent directory.
			// furthermore users cannot alter their root dir or basic dirs
			if (!validateAccess(session, userFile, wantToModify))
			{
				ServerMain.logger.warning(String.format("User %s wanted %sing access to '%s' (URI: %s)", session, (wantToModify ? "modify" : "read"),
						userFile, uri));
				throw new IllegalArgumentException(String.format("%sing access to path '%s' for user '%s' (%s) denied.", (wantToModify ? "Modify"
						: "Read"), uri, session.getUser(), session.getRole()));
			}

			// Check for existence and is dir / file
			if (!userFile.exists())
				throw new IllegalArgumentException("File does not exist: " + uri);
			switch (fileType)
			{
				case DIR:
					if (!userFile.isDirectory())
						throw new IllegalArgumentException("File is no directory: " + uri);
					break;
				case FILE:
					if (userFile.isDirectory())
						throw new IllegalArgumentException("File is a directory: " + uri);
					break;
			}

			return userFile;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Exception e)
		{
			ServerMain.logger.severe(e.toString());
			e.printStackTrace();
			throw new ServerException();
		}
	}

	// --------------------------------------------------------------------------------
	boolean validateAccess(SessionImpl session, File file, boolean wantToModify)
	{
		logger.entering("AccessControls", "checkFileAccess", new Object[] { session, file, wantToModify });
		boolean grantAccess = true;
		try
		{
			String user = session.getUser();
			logger.finer(String.format("user=%s;role=%s;", user, session.getRole()));
			switch (session.getRole())
			{
				case USER:
					// users can only access folders within their dir
					if (!IOUtil.isOrIsInParentDir(getUserRootDir(user), file))
					{
						grantAccess = false;
						logger.finer(String.format("Access denied: File %s is not in parent dir of %s!", file, user));
					}

					if (wantToModify)
					{
						// users cannot modify their user root dir
						if (file.equals(getUserRootDir(user)))
						{
							grantAccess = false;
							logger.finer(String.format("Access denied: File %s equals user's root dir!", file));
						}
						// users also cannot modify their basic dirs
						for (File oneBasicDir : getUsersBasicDirs(user))
						{
							if (file.equals(oneBasicDir))
							{
								grantAccess = false;
								logger.finer(String.format("Access denied: File %s equals basic dir %s!", file, oneBasicDir));
							}
						}
					}
					break;
				case ADMIN:
					// admins can access all user dirs
					if (!IOUtil.isOrIsInParentDir(USER_FILES_DIR, file))
					{
						grantAccess = false;
						logger.finer(String.format("Access denied: File %s is not in parent dir of %s!", file, user));
					}
					break;
			}
		}
		catch (Exception e)
		{
			grantAccess = false;
		}
		logger.exiting("AccessControls", "checkFileAccess", grantAccess);
		return grantAccess;
	}

	// --------------------------------------------------------------------------------
	/**
	 * @param dir
	 * @param parentDirToOmit
	 *            Can be "" so that nothing is omitted.
	 * @return
	 * @throws IOException
	 */
	public FileInfo[] listFileInfos(SessionImpl session, File dir, File parentDirToOmit) throws IOException
	{
		File[] files = dir.listFiles();
		FileInfo[] fileInfos = new FileInfo[files.length];
		File oneFile = null;
		boolean modifyable;
		for (int i = 0; i < files.length; i++)
		{
			oneFile = files[i];
			// check if the user denoted by the session can modify the file
			modifyable = validateAccess(session, oneFile, true);
			fileInfos[i] = new FileInfo(oneFile, parentDirToOmit, modifyable);
		}
		return fileInfos;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
