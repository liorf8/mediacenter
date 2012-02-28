package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;
import de.root1.simon.RawChannel;
import de.root1.simon.RawChannelDataListener;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;

// mark this class as a remote class and export all methods known in ClientCallbackInterface
@SimonRemote(value = { ClientCallback.class })
public class ClientCallbackImpl implements ClientCallback
{

	@SuppressWarnings("unused")
	private static final long	serialVersionUID	= 1L;

	private final Component parentComponent;
	public ClientCallbackImpl(Component parentComponent)
	{
		this.parentComponent = parentComponent;
	}

	@Override
	public void message(final String text, final int messageType)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JOptionPane.showMessageDialog(parentComponent, text, "Message from Server", messageType);
			}
		});
	}

	public int prepareRawChannel(String filename, long fileSize)
	{
		return Simon.prepareRawChannel(this.new FileReceiver(new File(filename), fileSize), this);
	}

	// --------------------------------------------------------------------------------
	public void sendFile(Session session, Path path, String targetUri) throws IOException
	{
		RawChannel rawChannel = null;
		ReadableByteChannel rbc = null;
		try
		{
			// get a RawChannel Token from server. This is needed to open the RawChannel
			long fileSize = (Long) Files.getAttribute(path, "size", LinkOption.NOFOLLOW_LINKS);
			int token = session.prepareRawChannel(targetUri, path.getFileName().toString(), fileSize);

			// with the remote object and token, tell SIMON that you need a RawChannel
			rawChannel = Simon.openRawChannel(token, session);

			// first, we open a ReadableByteChannel. This is thanks to Java NIO faster than normal file operation
			rbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ));

			// we send the file in 8kb packages through the RawChannel
			ByteBuffer data = ByteBuffer.allocate(8 * 1024);
			while (rbc.read(data) != -1)
			{
				System.out.println("Read " + data);
				rawChannel.write(data);
				data.clear();
			}
		}
		finally
		{
			// all data written. Now we can close the ReadableByteChannel
			if (rbc != null)
				rbc.close();

			// ... and also the RawChannel
			if (rawChannel != null)
				rawChannel.close();
		}
	}

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
					System.out.println("Starting download of " + dest);
					this.fc = new FileOutputStream(dest).getChannel();
				}
				fc.write(data);
				System.out.println(data);
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
				System.out.println(String.format("Successfully downloaded %s (%s) in %s -> %s/s)", dest, ByteValue.bytesToString(fileSize),
						TimeValue.formatMillis(duration, true, false), ByteValue.bytesToString((long) ProgressUtil.speed(fileSize, duration))));
				fc.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	public int getStreamingPort()
	{
		return 5555;
	}
}