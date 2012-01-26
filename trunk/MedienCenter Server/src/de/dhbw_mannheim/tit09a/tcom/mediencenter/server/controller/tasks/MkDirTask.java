package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;


public class MkDirTask implements IOTask<Void>
{
    private final File dir;

    public MkDirTask(String dirPath)
    {
	this.dir = new File(dirPath);
    }

    @Override
    public Void call() throws IOException
    {
	IOUtil.executeMkDir(dir);
	return null;
    }
}
