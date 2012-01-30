package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.FileReceiver;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.DeleteFileTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.ListFileInfosTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MkDirTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MkDirsTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.RenameFileTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.root1.simon.Simon;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Session.class })
public class SessionImpl implements Session, SimonUnreferenced, Serializable
{
    private static final long serialVersionUID = 4608946221610753415L;

    // os: C:\\Users\\mhertram\\USERS_ROOT_DIR
    // max: C:\\Users\\Max\\USERS_ROOT_DIR

    public static final String USERS_ROOT_DIR = "C:\\Users\\Max\\USERS_ROOT_DIR\\";
    public static final char[] ILLEGAL_CHARS_IN_FILENAME = "\\/:*?<>|%&".toCharArray();
    // public static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();
    public static final ThreadPoolExecutor IO_EXECUTOR = new ThreadPoolExecutor(1, 4, 30,
	    TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1024));
    static
    {
	IO_EXECUTOR.setRejectedExecutionHandler(new RejectedExecutionHandler()
	{
	    @Override
	    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
	    {
		ServerMain.serverLogger.warning(String.format(
			"Executor '%s' + rejected '%s'. Queue length: %d", executor, r, executor
				.getQueue().size()));
	    }
	});
	IO_EXECUTOR.prestartCoreThread();
    }

    public static enum BASIC_DIRS
    {
	Music, Pictures, Videos
    };

    private final String user;
    private final LoginServiceImpl loginService;
    private final String sessionId;

    SessionImpl(String user, LoginServiceImpl loginService)
    {
	this.user = user;
	this.loginService = loginService;
	this.sessionId = "default_session_id";
    }

    // Overriding SimonUnreferenced
    @Override
    public void unreferenced()
    {
	ServerMain.serverLogger.fine(Thread.currentThread() + ": Unreferenced: " + user + "@"
		+ this);
	loginService.removeUserSession(this);
    }

    //
    public String getUser()
    {
	return user;
    }

    public String getSessionId()
    {
	return sessionId;
    }

    public String toString()
    {
	return this.getSessionId();
    }

    @Override
    public void changeAttr(String key, String newValue)
    {
	System.out.println("NOCH NICHT IMPLEMENTIERT!");
    }

    @Override
    public void deleteFile(String uri) throws IOException
    {
	IOUtil.executeIOTask(IO_EXECUTOR, new DeleteFileTask(uriToPath(uri, user)));
    }

    @Override
    public void renameFile(String uri, String newName) throws IOException
    {
	IOUtil.executeIOTask(IO_EXECUTOR, new RenameFileTask(uriToPath(uri, user), newName));
    }

    @Override
    public void copyFile(String srcURI, String destURI, boolean replace) throws IOException
    {
	IOUtil.executeIOTask(IO_EXECUTOR, new RenameFileTask(uriToPath(srcURI, user), uriToPath(destURI, user)));
    }

    @Override
    public void mkDir(String uri) throws IOException
    {
	IOUtil.executeIOTask(IO_EXECUTOR, new MkDirTask(uriToPath(uri, user)));
    }

    @Override
    public FileInfo[] listFiles(String dirURI) throws IOException
    {
	return IOUtil.executeIOTask(IO_EXECUTOR, new ListFileInfosTask(user, uriToPath(dirURI, user)));
    }

    @Override
    public int openFileChannel(String destURI, long fileSize)
    {
	return Simon.prepareRawChannel(new FileReceiver(uriToPath(destURI, user), fileSize), this);
    }

    
    

    public static void createUserDirs(String user) throws IOException
    {
	String[] dirs = { getUserRootDir(user), getUserBasicDir(user, BASIC_DIRS.Music),
		getUserBasicDir(user, BASIC_DIRS.Pictures),
		getUserBasicDir(user, BASIC_DIRS.Videos) };
	IOUtil.executeIOTask(IO_EXECUTOR, new MkDirsTask(dirs));
    }

    public static String getUserRootDir(String user)
    {
	return USERS_ROOT_DIR + File.separator + user + File.separator;
    }

    public static String getUserBasicDir(String user, BASIC_DIRS dir)
    {
	return getUserRootDir(user) + File.separator + dir.toString() + File.separator;
    }

    public static String uriToPath(String uri, String user)
    {
	return getUserRootDir(user) + FileInfo.uriPathToFilePath(uri);
    }
    
}
