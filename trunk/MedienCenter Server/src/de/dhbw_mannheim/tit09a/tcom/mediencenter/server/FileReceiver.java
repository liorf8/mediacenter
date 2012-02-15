package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;
import de.root1.simon.RawChannelDataListener;

public class FileReceiver implements RawChannelDataListener
{
	private FileChannel	fc;
	private long		fileSize;
	private File		dest;
	private long		start;

	FileReceiver(File dest, long fileSize)
	{
		this.fileSize = fileSize;
		this.dest = dest;
	}

	public void write(ByteBuffer data)
	{
		try
		{
			if (start <= 0)
			{
				start = System.currentTimeMillis();
				FileManager.logger.fine("Starting upload of " + dest);
				this.fc = new FileOutputStream(dest).getChannel();
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
			FileManager.logger.fine(String.format("Successfully uploaded %s (%s in %s -> %s/s)", dest, ByteValue.bytesToString(fileSize),
					TimeValue.formatMillis(duration, true, false), ByteValue.bytesToString((long) ProgressUtil.speed(fileSize, duration))));
			fc.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

}