package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;


public class RenameFileTask implements IOTask<Void>
{
    private final File file;
    private final File dest;
    public RenameFileTask(String filePath, String dest)
    {
	this.file = new File(filePath);
	this.dest = new File(dest);
    }
    @Override
    public Void call() throws IOException
    {
	IOUtil.ensureExists(file);
	IOUtil.ensureDoesNotExist(dest);
	IOUtil.executeRenameTo(file, dest);
	return null;
    }

}
