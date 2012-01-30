package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;
import de.root1.simon.RawChannelDataListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileReceiver implements RawChannelDataListener
{
    private FileChannel fc;
    private long fileSize;
    private String destPath;
    private long start;

    public FileReceiver(String destPath, long fileSize)
    {
	try
	{
	    this.fc = new FileOutputStream(new File(destPath)).getChannel();
	    this.fileSize = fileSize;
	    this.destPath = destPath;
	}
	catch (FileNotFoundException ex)
	{
	    // cannot really occur, because we want to CREATE the file.
	    ex.printStackTrace();
	}
    }

    public void write(ByteBuffer data)
    {
	try
	{
	    if (start == 0)
	    {
		start = System.currentTimeMillis();
	    }
	    fc.write(data);
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
    }

    public void close()
    {
	try
	{
	    long duration = System.currentTimeMillis() - start;
	    ServerMain.serverLogger.info(String.format(
		    "Successfully uploaded %s in %s to %s (%s/s).",
		    ByteValue.bytesToString(fileSize, true),
		    TimeValue.formatMillis(duration, true, true), destPath,
		    ProgressUtil.speed(fileSize, duration)));
	    fc.close();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
    }

}