package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MakeDirTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.ListFilenamesTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MakeDirsTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.Tasks;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class IOController
{
    public static final String USERS_ROOT_DIR = "C:\\Users\\mhertram\\USERS_ROOT_DIR";

    // os: C:\\Users\\mhertram\\USERS_ROOT_DIR
    // max: C:\\Users\\Max\\USERS_ROOT_DIR

    public static enum BASIC_DIRS
    {
	Music, Pictures, Videos
    };

    private static IOController instance;

    private IOController()
    {
	try
	{
	    Tasks.executeIOTask(new MakeDirTask(USERS_ROOT_DIR));
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public static synchronized IOController getInstance()
    {
	if (instance == null) instance = new IOController();
	return instance;
    }

    public void createUserDirs(String user) throws IOException
    {
	String[] dirs = {getUserRootDir(user), getUserBasicDir(user, BASIC_DIRS.Music), getUserBasicDir(user, BASIC_DIRS.Pictures), getUserBasicDir(user, BASIC_DIRS.Videos)};
	Tasks.executeIOTask(new MakeDirsTask(dirs));
    }
    
    private String getUserRootDir(String user)
    {
	return USERS_ROOT_DIR + File.separator + user;
    }

    private String getUserBasicDir(String user, BASIC_DIRS dir)
    {
	return getUserRootDir(user) + File.separator + dir.toString();
    }

    public FileInfo[] listFilenames(String user, String dirPath) throws IOException
    {
	String fullDirPath = getUserRootDir(user) + File.separator + dirPath;
	return Tasks.executeIOTask(new ListFilenamesTask(fullDirPath));
    }
    
    @SuppressWarnings("unused")
    private String omitUsersRoot(String filePath)
    {
	return filePath.substring(filePath.indexOf(USERS_ROOT_DIR));
    }
}
