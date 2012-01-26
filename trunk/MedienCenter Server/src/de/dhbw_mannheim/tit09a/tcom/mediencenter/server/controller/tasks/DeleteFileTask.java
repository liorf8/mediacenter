package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;


public class DeleteFileTask implements IOTask<Void>
{
    private final File file;

    public DeleteFileTask(String filePath)
    {
	file = new File(filePath);
    }

    @Override
    public Void call() throws IOException
    {
	IOUtil.ensureExists(file);
	if (file.isDirectory())
	{
	    IOUtil.ensureEmptyDir(file);
	}
	IOUtil.executeDelete(file);

	return null;
    }
}
