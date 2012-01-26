package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;


public class MkDirsTask implements IOTask<Void>
{
    private final File[] dirs;

    public MkDirsTask(String[] dirPaths)
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
	for (File oneDir : dirs)
	{
	    IOUtil.executeMkDir(oneDir);
	}
	return null;
    }
}
