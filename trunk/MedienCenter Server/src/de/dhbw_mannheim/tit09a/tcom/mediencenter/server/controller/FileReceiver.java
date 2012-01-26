package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

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
    private long totalBytesWritten;

    public FileReceiver(String destPath, long fileSize)
    {
	try
	{
	    this.fc = new FileOutputStream(new File(destPath)).getChannel();
	    this.fileSize = fileSize;
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
	    long bytesWritten = data.limit();
	    totalBytesWritten += bytesWritten;
	    System.out.printf(Thread.currentThread() +": Written %d (%d/%d - %d%%).%n", bytesWritten, totalBytesWritten,
		    fileSize, (int) (totalBytesWritten * 100L / fileSize));
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
	    System.out.println(Thread.currentThread() +": Closed!");
	    fc.close();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
    }

}