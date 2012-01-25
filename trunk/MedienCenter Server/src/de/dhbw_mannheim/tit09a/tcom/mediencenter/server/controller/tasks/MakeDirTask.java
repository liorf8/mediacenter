package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

public class MakeDirTask implements IOTask<Void>
{
    private final File dir;

    public MakeDirTask(String dirPath)
    {
	this.dir = new File(dirPath);
    }

    @Override
    public Void call() throws IOException
    {
	System.out.println(Thread.currentThread() + ": Creating " + dir + " ...");
	if (!dir.exists())
	{
	    if (!dir.mkdir()) throw new IOException("Could not create dir: " + dir);
	}
	return null;
    }
}
