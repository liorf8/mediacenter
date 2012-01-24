package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileController
{
    public static final String USERS_ROOT_DIR = "F:\\Studienarbeit\\USERS_ROOT_DIR";

    private static enum BASIC_DIRS
    {
	Music, Pictures, Videos
    };

    private static FileController instance;
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    private FileController()
    {

    }

    public static synchronized FileController getInstance()
    {
	if (instance == null) instance = new FileController();
	return instance;
    }

    public String getUsersRootDir()
    {
	return USERS_ROOT_DIR;
    }

    public String getUserRootDir(String login) throws IOException
    {
	System.out.println(Thread.currentThread() + ": getUserRootDir() " + login);
	String rootDir = getUsersRootDir() + File.separator + login;

	Future<Void> f = executor.submit(new CreateIfNotExists(rootDir));
	try
	{
	    f.get();
	}
	catch (Throwable e)
	{
	    Throwable cause = e;
	    if (e.getCause() != null) cause = e.getCause();
	    if (cause instanceof IOException)
	    {
		throw (IOException) cause;
	    }
	    else
	    {
		e.printStackTrace();
	    }
	}
	return rootDir;
    }

    public String getBasicDir(String login, BASIC_DIRS dir) throws IOException
    {
	String dirName = getUserRootDir(login) + File.separator + dir.toString();
	executor.submit(new CreateIfNotExists(dirName));
	return dirName;
    }

    private final class CreateIfNotExists implements Callable<Void>
    {
	private final File dir;

	private CreateIfNotExists(String dirPath)
	{
	    this.dir = new File(dirPath);
	}

	@Override
	public Void call() throws IOException
	{
	    System.out.println(Thread.currentThread() + ": Creating " + dir + " if not exists.");
	    if (!dir.exists())
	    {
		if (!dir.mkdir()) throw new IOException("Could not create dir: " + dir);
	    }
	    System.out.println("Done with call: " + System.currentTimeMillis());
	    throw new IOException("Haha!");
	    // return null;
	}
    };

}
