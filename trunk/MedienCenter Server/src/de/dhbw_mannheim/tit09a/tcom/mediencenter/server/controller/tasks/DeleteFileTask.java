package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

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
	if(!file.exists())
	{
	    throw new IOException("File does not exist: " + file.getAbsolutePath());
	}
	if(file.isDirectory())
	{
	    if(!file.delete())
	    {
		throw new IOException("Could not delete Directory " + file.getAbsolutePath());
	    }
	}
	else
	{
	    if(!file.delete())
	    {
		throw new IOException("Could not delete File " + file.getAbsolutePath());
	    }
	}
	return null;
    }

}
