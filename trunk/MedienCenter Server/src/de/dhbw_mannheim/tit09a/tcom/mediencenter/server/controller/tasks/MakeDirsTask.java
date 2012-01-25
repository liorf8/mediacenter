package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MakeDirsTask implements IOTask<Void>
{
    private final File[] dirs;

    public MakeDirsTask(String[] dirPaths)
    {
	dirs = new File[dirPaths.length];
	for (int i = 0; i < dirPaths.length; i++)
	{
	    dirs[i] = new File(dirPaths[i]);
	}
    }

    @Override
    public Void call() throws IOException
    {
	System.out.println(Thread.currentThread() + ": Creating " + Arrays.toString(dirs) + " ...");
	for (File dir : dirs)
	{

	    if (!dir.exists())
	    {
		if (!dir.mkdir()) throw new IOException("Could not create dir: " + dir);
	    }
	}

	return null;
    }
}
