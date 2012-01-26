package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class CopyFileTask implements IOTask<Void>
{
    private final File src;
    private final File dest;
    private final boolean replace;

    public CopyFileTask(String srcPath, String destPath, boolean replace)
    {
	this.src = new File(srcPath);
	this.dest = new File(destPath);
	this.replace = replace;
    }

    @Override
    public Void call() throws IOException
    {
	IOUtil.ensureExists(src);
	IOUtil.ensureIsNoDir(src);
	IOUtil.copyFile(src, dest, replace);
	return null;
    }
}
