package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.remoteimage;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection.ServerConnection;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection.ServerConnectionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class ImgControllerImpl implements ImgController
{
	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private SimpleImageTest			view;

	private DisplayImageTask		displayTask;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public ImgControllerImpl()
	{
		try
		{
			SwingUtilities.invokeAndWait(new BuildViewTask(this));

		}
		catch (Throwable t)
		{
			exit(t);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public void displayImg()
	{
		cancelDisplayImg();

		displayTask = new DisplayImageTask(view.getRIC(), view.getTxtFldPathText());
		displayTask.execute();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void cancelDisplayImg()
	{
		if (displayTask != null)
			displayTask.cancel(true);
	}

	// --------------------------------------------------------------------------------
	@Override
	public void setScaleToFit()
	{
		view.getRIC().setScaleToFit(view.getCkBxScaleToFitIsSelected());
	}

	// --------------------------------------------------------------------------------
	@Override
	public void exit(Throwable cause)
	{
		try
		{
			if (cause != null)
				cause.printStackTrace();

			if (cause == null)
				System.exit(0);
			else
				System.exit(1);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			System.exit(1);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Classes -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private class BuildViewTask implements Runnable
	{
		private ImgController	controller;

		public BuildViewTask(ImgController controller)
		{
			this.controller = controller;
		}

		@Override
		public void run()
		{
			try
			{
				view = new SimpleImageTest(controller);
				view.setVisible(true);
			}
			catch (Exception e)
			{
				controller.exit(e);
			}
		}
	}

	// --------------------------------------------------------------------------------
	private class DisplayImageTask extends SwingWorker<Image, Void>
	{
		private RemoteImageComponent	ric;
		private String					path;

		private DisplayImageTask(RemoteImageComponent ric, String path)
		{
			this.ric = ric;
			this.path = path;
		}

		@Override
		protected Image doInBackground() throws Exception
		{
			System.out.println("doInBackground()--start @" + Thread.currentThread());
			// Set the GUI to loading state
			ric.setState(ric.getImgLoadingState(path));

			// TODO remove
			Thread.sleep(1000);

			ServerConnection simCon = new ServerConnectionImpl();
			simCon.connect();
			simCon.login("max", "pw", new DefaultClientCallback(ric));
			Session session = simCon.getSession();
			byte[] data = session.getFileBytes(path);
			BufferedImage bi = MediaUtil.readImage(data);

			// if could not be read -> change state
			if (bi == null)
			{
				throw new IIOException("Could not read image: '" + path + "'. Maybe no supported image file? Supported: "
						+ Arrays.toString(ImageIO.getReaderMIMETypes()));
			}
			System.out.println("doInBackground()--end @" + Thread.currentThread());
			return bi;
		}

		@Override
		protected void done()
		{
			try
			{
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
			{
				System.out.println(ignore);
				ric.setState(ric.getNoImageState());
			}
		}
	}

	// --------------------------------------------------------------------------------
	// -- Main Method -----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void main(String[] args)
	{
		new ImgControllerImpl();
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
