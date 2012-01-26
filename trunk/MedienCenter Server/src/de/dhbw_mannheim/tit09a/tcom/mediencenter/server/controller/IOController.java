package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.CopyFileTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.DeleteFileTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MkDirTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.ListFileInfosTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MkDirsTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.RenameFileTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.root1.simon.Simon;

public class IOController
{
    public static final String USERS_ROOT_DIR = "C:\\Users\\Max\\USERS_ROOT_DIR";
    public static final char[] ILLEGAL_CHARS_IN_FILENAME = "\\/:*?<>|%&".toCharArray();
    public static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();

    
    // os: C:\\Users\\mhertram\\USERS_ROOT_DIR
    // max: C:\\Users\\Max\\USERS_ROOT_DIR

    public static enum BASIC_DIRS
    {
	Music, Pictures, Videos
    };

    private IOController()
    {

    }

    public static void createUserDirs(String user) throws IOException
    {
	String[] dirs = { getUserRootDir(user), getUserBasicDir(user, BASIC_DIRS.Music),
		getUserBasicDir(user, BASIC_DIRS.Pictures),
		getUserBasicDir(user, BASIC_DIRS.Videos) };
	IOUtil.executeIOTask(IO_EXECUTOR, new MkDirsTask(dirs));
    }

    private static String getUserRootDir(String user)
    {
	return USERS_ROOT_DIR + File.separator + user;
    }

    private static String getUserBasicDir(String user, BASIC_DIRS dir)
    {
	return getUserRootDir(user) + File.separator + dir.toString();
    }

    public static FileInfo[] listFilenames(String user, String dirPath) throws IOException
    {
	String fullDirPath = getUserRootDir(user) + File.separator + dirPath;
	return IOUtil.executeIOTask(IO_EXECUTOR, new ListFileInfosTask(fullDirPath));
    }

    public static void deleteFile(String user, String filePath) throws IOException
    {
	String fullFilePath = getUserRootDir(user) + File.separator + filePath;
	IOUtil.executeIOTask(IO_EXECUTOR, new DeleteFileTask(fullFilePath));
    }

    public static void mkDir(String user, String parentDir, String dirName) throws IOException
    {
	String fullDirPath = getUserRootDir(user) + File.separator + parentDir + File.separator + dirName;
	IOUtil.executeIOTask(IO_EXECUTOR, new MkDirTask(fullDirPath));
    }

    public static void renameFile(String user, String filePath, String newName) throws IOException
    {
	IOUtil.ensureValidFilename(newName, ILLEGAL_CHARS_IN_FILENAME);
	String fullFilePath = getUserRootDir(user) + File.separator + filePath;
	String newDest = fullFilePath.substring(0, fullFilePath.lastIndexOf(File.separatorChar)+1) + newName;
	IOUtil.executeIOTask(IO_EXECUTOR, new RenameFileTask(fullFilePath, newDest));
    }
    
    public static void copyFile(String user, String srcPath, String destPath, boolean replace) throws IOException
    {
	String fullSrcPath = getUserRootDir(user) + File.separator + srcPath;
	String fullDestPath = getUserRootDir(user) + File.separator + destPath;
	IOUtil.executeIOTask(IO_EXECUTOR, new CopyFileTask(fullSrcPath, fullDestPath, replace));
    }
    
    public static int openFileChannel(String user, Object simonRemote, String destPath, long fileSize)
    {
	String fullDestPath = getUserRootDir(user) + File.separator + destPath;
	int token = Simon.prepareRawChannel(new FileReceiver(fullDestPath, fileSize), simonRemote);
	return token;
    }
}
