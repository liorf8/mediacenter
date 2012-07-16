package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.CancelSupport;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.SimpleFileReceiver.ExistOption;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.root1.simon.RawChannel;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.exceptions.SimonException;

// mark this class as a remote class and export all methods known in ClientCallbackInterface
@SimonRemote(value = { ClientCallback.class })
public class DefaultClientCallback implements ClientCallback
{
	@SuppressWarnings("unused")
	private static final long	serialVersionUID	= 1L;

	private final Component		parentComponent;

	public static final String	PROPERTY_FINISHED	= "finished";
	public static final String	PROPERTY_FILESIZE	= "fileSize";
	public static final String	PROPERTY_READ_BYTES	= "readBytes";

	public DefaultClientCallback(Component parentComponent)
	{
		this.parentComponent = parentComponent;
	}

	@Override
	public void mediaFinished(String path)
	{
		// TODO Auto-generated method stub
		System.out.println("mediaFinished()" + path);
	}

	@Override
	public void notifyShutdown(final int delayInSeconds)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JOptionPane.showMessageDialog(parentComponent,
						"Server will shut down in " + delayInSeconds + " seconds.",
						"Message from Server",
						JOptionPane.WARNING_MESSAGE);
			}
		});
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

	public int prepareRawChannel(String filename, long fileSize) throws SimonException, FileAlreadyExistsException
	{
		return Simon.prepareRawChannel(this.new FileReceiver(Paths.get(filename), fileSize, ExistOption.AUTO_RENAME), this);
	}

	// --------------------------------------------------------------------------------
	public void sendFile(Session session, Path path, String targetUri, PropertyChangeListener pcl, CancelSupport cancelSupport) throws IOException
	{
		RawChannel rawChannel = null;
		ReadableByteChannel rbc = null;

		try
		{
			// get a RawChannel Token from server. This is needed to open the RawChannel
			long fileSize = (Long) Files.getAttribute(path, "size", LinkOption.NOFOLLOW_LINKS);
			if (pcl != null)
			{
				pcl.propertyChange(new PropertyChangeEvent(this, PROPERTY_FILESIZE, null, fileSize));
			}

			int token = session.prepareRawChannel(targetUri, path.getFileName().toString(), fileSize);

			// with the remote object and token, tell SIMON that you need a RawChannel
			rawChannel = Simon.openRawChannel(token, session);

			// first, we open a ReadableByteChannel. This is thanks to Java NIO faster than normal file operation
			rbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ));

			// we send the file in 8kb packages through the RawChannel
			ByteBuffer data = ByteBuffer.allocate(128 * 1024);
			while ((!cancelSupport.isCancelled()) && rbc.read(data) != -1)
			{
				// System.out.println("Read " + data);
				rawChannel.write(data);

				if (pcl != null)
				{
					pcl.propertyChange(new PropertyChangeEvent(this, PROPERTY_READ_BYTES, null, data.position()));
				}

				data.clear();
			}
		}
		finally
		{
			if (pcl != null)
			{
				pcl.propertyChange(new PropertyChangeEvent(this, PROPERTY_FINISHED, null, true));
			}
			// all data written. Now we can close the ReadableByteChannel
			if (rbc != null)
				rbc.close();

			// ... and also the RawChannel
			if (rawChannel != null)
				rawChannel.close();
		}
	}

	// --------------------------------------------------------------------------------
	public class FileReceiver extends SimpleFileReceiver
	{
		public FileReceiver(Path dest, long fileSize, ExistOption option) throws FileAlreadyExistsException
		{
			super(dest, fileSize, option);
		}

		private long	totalBytesWritten;
		private long	start;

		@Override
		protected void onStarted(Path dest, long fileSize)
		{
			start = System.currentTimeMillis();
			System.out.println(dest + " started: " + ByteValue.bytesToString(fileSize));
		}

		@Override
		protected void onWritten(Path dest, int bytesWritten)
		{
			totalBytesWritten += bytesWritten;
			System.out.println(dest + " written: " + bytesWritten);
		}

		@Override
		protected void onClosed(Path dest)
		{
			long duration = System.currentTimeMillis() - start;
			System.out.println(String.format("%s successfully downloaded (%s in %s -> %s/s)",
					dest,
					ByteValue.bytesToString(totalBytesWritten),
					TimeValue.formatMillis(duration, true, false),
					ByteValue.bytesToString((long) ProgressUtil.speed(totalBytesWritten, duration))));
		}

	}
}