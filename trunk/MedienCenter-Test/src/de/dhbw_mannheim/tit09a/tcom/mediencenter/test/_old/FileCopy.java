package de.dhbw_mannheim.tit09a.tcom.mediencenter.test._old;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileCopy
{
    
    // both slower than over transferInto with 64kb buffer
    // slowest: transfer all in one
    public static void copyFileByteBuffer(File src, File dest, boolean replace) throws IOException
    {
	// do nothing if the dest exists and replace is false
	if (!replace && dest.exists())
	{
	    return;
	}

	FileChannel srcChannel = null;
	FileChannel destChannel = null;
	try
	{
	    srcChannel = new FileInputStream(src).getChannel();
	    destChannel = new FileOutputStream(dest).getChannel();

	    // we send the file in 64kb packages through the channel
	    ByteBuffer buf = ByteBuffer.allocate(0xFFFF);
	    while (srcChannel.read(buf) != -1)
	    {
		buf.flip();
		destChannel.write(buf);
		buf.clear();
	    }
	}
	finally
	{
	    // all data written. Now we can close the channels
	    IOUtil.close(srcChannel);
	    IOUtil.close(destChannel);
	}
    }

    public static void copyFileTransferWhole(File src, File dest, boolean replace) throws IOException
    {
	// do nothing if the dest exists and replace is false
	if (!replace && dest.exists())
	{
	    return;
	}

	FileChannel srcChannel = null;
	FileChannel destChannel = null;
	try
	{
	    srcChannel = new FileInputStream(src).getChannel();
	    destChannel = new FileOutputStream(dest).getChannel();
	    destChannel.transferFrom(srcChannel, 0, srcChannel.size());
	}
	finally
	{
	    // all data written. Now we can close the channels
	    IOUtil.close(srcChannel);
	    IOUtil.close(destChannel);
	}
    }

}
