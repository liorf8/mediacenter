package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;
import de.root1.simon.RawChannelDataListener;
import java.io.File;
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
	this.fileSize = fileSize;
	this.destPath = destPath;
    }

    public void write(ByteBuffer data)
    {
	try
	{
	    if (start <= 0)
	    {
		start = System.currentTimeMillis();
		ServerMain.serverLogger.info("Starting upload of " + destPath + " @ "
			+ Thread.currentThread());
		this.fc = new FileOutputStream(new File(destPath)).getChannel();
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
		    "Successfully uploaded %s (%s in %s -> %s/s) @ %s", destPath,
		    ByteValue.bytesToString(fileSize),
		    TimeValue.formatMillis(duration, true, false),
		    ByteValue.bytesToString((long) ProgressUtil.speed(fileSize, duration)),
		    Thread.currentThread()));
	    fc.close();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
    }

}