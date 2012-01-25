package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class ListFilenamesTask implements IOTask<FileInfo[]>
{
    private final File dir;

    public ListFilenamesTask(String dirPath)
    {
	this.dir = new File(dirPath);
    }

    @Override
    public FileInfo[] call() throws IOException
    {
	System.out.println(Thread.currentThread() + ": Listing filenames of " + dir + " ...");
	if (!dir.exists())
	{
	    throw new IOException("File does not exist: " + dir.getAbsolutePath());
	}
	if (!dir.isDirectory())
	{
	    throw new IOException("File is no directory: " + dir.getAbsolutePath());
	}
	
	File[] files = dir.listFiles();
	FileInfo[] fileInfos = new FileInfo[files.length];
	File oneFile = null;
	for(int i=0; i<files.length; i++)
	{
	    oneFile = files[i];
	    fileInfos[i] = new FileInfo(oneFile.getName(), oneFile.length(), oneFile.isDirectory());
	}
	
	return fileInfos;
    }

}
