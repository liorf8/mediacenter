package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.remoteimage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;

public class RemoteImageComponent extends JComponent
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= -6728124280248726521L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private RemoteImageState	state;

	private NoImgState			noImgState;
	private ExceptionState		exceptionState;
	private LoadingImgState		loadingImageState;
	private ImgAvailableState	imgAvailableState;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public RemoteImageComponent()
	{
		noImgState = new NoImgState(this);
		exceptionState = new ExceptionState(this);
		loadingImageState = new LoadingImgState(this);
		imgAvailableState = new ImgAvailableState(this);

		setState(noImgState);
		
		this.setBackground(Color.BLACK);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void displayImg(Session session, String path)
	{
		System.out.println(System.currentTimeMillis() + " - newimg: " + path + " @" + Thread.currentThread());
		loadingImageState.loadImg(session, path);
	}

	// --------------------------------------------------------------------------------
	public void setScaleToFit(boolean scaleToFit)
	{
		imgAvailableState.setScaleToFit(scaleToFit);
		repaint();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g)
	{
		state.paintComponent(g);
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	synchronized void setState(RemoteImageState newState)
	{
		state = newState;
		// remove all mouselisteners
		for (MouseListener l : getMouseListeners())
		{
			removeMouseListener(l);
		}
		repaint();
	}

	// --------------------------------------------------------------------------------
	NoImgState getNoImageState()
	{
		return noImgState;
	}

	// --------------------------------------------------------------------------------
	ExceptionState getExceptionState(Throwable t)
	{
		exceptionState.setThrowable(t);
		return exceptionState;
	}

	// --------------------------------------------------------------------------------
	LoadingImgState getLoadingImgState()
	{
		return loadingImageState;
	}

	// --------------------------------------------------------------------------------
	ImgAvailableState getImgAvailableState(Image image)
	{
		imgAvailableState.setImage(image);
		return imgAvailableState;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
