package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.remoteimage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ImageUtil;

public class LoadingImgState extends RemoteImageState
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private LoadImageTask		loadingTask;
	private String				path;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	LoadingImgState(RemoteImageComponent ric)
	{
		super(ric);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g)
	{
		System.out.println(System.currentTimeMillis() + " - " +getClass().getSimpleName() + ": paintComponent() @" + Thread.currentThread());
		ImageUtil.drawCenteredString(g, ric, "Loading '" + path + "' ...", 0, -15);
		ImageUtil.drawCenteredString(g, ric, "Double-click to cancel.", 0, 15);
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	void loadImg(Session session, String path)
	{
		this.path = path;

		// Cancel current loading
		cancelLoading();

		// Load new
		ric.setState(this);
		ric.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					cancelLoading();
				}
			}
		});
		loadingTask = new LoadImageTask(session, path);
		loadingTask.execute();
	}

	// --------------------------------------------------------------------------------
	void cancelLoading()
	{
		ric.setState(ric.getNoImageState());
		if (loadingTask != null)
			loadingTask.cancel(true);
	}

	// --------------------------------------------------------------------------------
	// -- Private Classes -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private class LoadImageTask extends SwingWorker<Image, Void>
	{
		private Session	session;
		private String	path;

		private LoadImageTask(Session session, String path)
		{
			this.session = session;
			this.path = path;
		}

		@Override
		protected Image doInBackground() throws Exception
		{
			ByteArrayInputStream bais = null;
			try
			{
				// TODO remove
				System.out.println(System.currentTimeMillis() +" - doInBackground()--start @" + Thread.currentThread());
				//Thread.sleep(3000);

				byte[] data = session.getFileBytes(path);

				// this operation takes ca 300ms
				bais = new ByteArrayInputStream(data);
				BufferedImage bi = ImageIO.read(bais);

				// if could not be read -> change state
				if (bi == null)
				{
					throw new IIOException("Could not read image: '" + path + "'. Maybe no supported image file? Supported: "
							+ Arrays.toString(ImageIO.getReaderMIMETypes()));
				}
				System.out.println(System.currentTimeMillis() +" - doInBackground()--end @" + Thread.currentThread());
				return bi;
			}
			finally
			{
				if (bais != null)
					bais.close();
			}
		}

		@Override
		protected void done()
		{
			try
			{
				System.out.println(System.currentTimeMillis() +" - done() @" + Thread.currentThread());
				// if everything went fine -> change state to available with the BufferedImage
				// get() can cause Exceptions
				ric.setState(ric.getImgAvailableState(get()));
			}
			catch (ExecutionException e)
			{
				// if exception occurred -> change state to exception
				ric.setState(ric.getExceptionState(e.getCause()));
			}
			catch (CancellationException | InterruptedException ignore)
			{}
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
