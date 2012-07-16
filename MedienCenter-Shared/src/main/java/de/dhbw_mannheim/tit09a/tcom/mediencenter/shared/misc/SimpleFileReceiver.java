package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;
import de.root1.simon.RawChannelDataListener;

public class SimpleFileReceiver implements RawChannelDataListener
{
	private final Path			dest;
	private final long			fileSize;

	private WritableByteChannel	wbc;

	public enum ExistOption
	{
		REPLACE, AUTO_RENAME, EXCEPTION
	};

	public SimpleFileReceiver(Path dest, long fileSize, ExistOption option) throws FileAlreadyExistsException
	{
		// dest
		if (Files.exists(dest, LinkOption.NOFOLLOW_LINKS))
		{
			switch (option)
			{
				case REPLACE:
					this.dest = dest;
					break;
				case AUTO_RENAME:
					this.dest = NIOUtil.autoRename(dest);
					break;
				case EXCEPTION:
					throw new FileAlreadyExistsException(dest.toString());
				default:
					throw new FileAlreadyExistsException(dest.toString());
			}
		}
		else
		{
			this.dest = dest;
		}

		// fileSize
		if (fileSize <= 0L)
			throw new IllegalArgumentException("fileSize == 0. No data will be uploaded.");
		this.fileSize = fileSize;
	}

	@Override
	public void write(ByteBuffer data)
	{
		try
		{
			if (wbc == null)
			{
				this.wbc = Files.newByteChannel(dest,
						EnumSet.of(StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
				onStarted(dest, fileSize);
			}
			wbc.write(data);

			onWritten(dest, data.limit());
		}
		catch (IOException ex)
		{
			onException(dest, ex);
		}
	}

	@Override
	public void close()
	{
		try
		{
			if (wbc != null)
				wbc.close();
			onClosed(dest);
		}
		catch (IOException ex)
		{
			onException(dest, ex);
		}
	}

	protected void onStarted(Path dest, long fileSize)
	{

	}

	protected void onWritten(Path dest, int bytesWritten)
	{

	}

	protected void onClosed(Path dest)
	{

	}

	protected void onException(Path dest, Exception e)
	{
		System.err.println("Receiving of file " + dest + " failed. Deleting file. Exception: ");
		e.printStackTrace();
		if (e instanceof AsynchronousCloseException)
		{
			// delete the dest if not finished
			try
			{
				Files.deleteIfExists(dest);
			}
			catch (IOException e1)
			{
				System.err.println("Deleting of file failed. Exception: ");
				e1.printStackTrace();
			}
		}
	}
}
