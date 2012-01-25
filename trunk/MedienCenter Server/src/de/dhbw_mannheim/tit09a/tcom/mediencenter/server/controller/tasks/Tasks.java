package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Tasks
{
    private static final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    public static <T> T executeIOTask (IOTask<T> task) throws IOException
    {
	System.out.println("Executing " + task.getClass().getSimpleName() +" @" +Thread.currentThread());

	try
	{
	    return ioExecutor.submit(task).get();
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
		return null;
	    }
	}
    }
}
