package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class ListFileInfosTask implements IOTask<FileInfo[]>
{
    private final File dir;

    public ListFileInfosTask(String dirPath)
    {
	this.dir = new File(dirPath);
    }

    @Override
    public FileInfo[] call() throws IOException
    {
	IOUtil.ensureExists(dir);
	IOUtil.ensureIsDir(dir);
	
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
