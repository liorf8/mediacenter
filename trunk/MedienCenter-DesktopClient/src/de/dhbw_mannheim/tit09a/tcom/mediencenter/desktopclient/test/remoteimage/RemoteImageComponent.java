package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.remoteimage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class RemoteImageComponent extends JComponent
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= -6728124280248726521L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private RICState			state;

	private NoImgState			noImgState;
	private ExceptionState		exceptionState;
	private ImgLoadingState		imgLoadingState;
	private ImgAvailableState	imgAvailableState;

	private SimpleImageTest		parent;
	
	private ImgLoadingMouseListener imgLoadingML;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public RemoteImageComponent(SimpleImageTest parent)
	{
		noImgState = new NoImgState(this);
		exceptionState = new ExceptionState(this);
		imgLoadingState = new ImgLoadingState(this);
		imgAvailableState = new ImgAvailableState(this);

		imgLoadingML = new ImgLoadingMouseListener();
		
		this.parent = parent;

		setState(noImgState);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g)
	{
		state.paintComponent(g);
	}

	// --------------------------------------------------------------------------------
	public void setScaleToFit(boolean doScale)
	{
		imgAvailableState.setScaleToFit(doScale);
		repaint();
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public synchronized void setState(RICState newState)
	{
		System.out.println(this.getClass().getSimpleName() + ": Changing state to " + newState.getClass().getSimpleName());
		state = newState;
		repaint();
	}

	// --------------------------------------------------------------------------------
	public NoImgState getNoImageState()
	{
		removeMouseListeners();
		
		return noImgState;
	}

	// --------------------------------------------------------------------------------
	public ExceptionState getExceptionState(Throwable t)
	{
		removeMouseListeners();
		
		exceptionState.setThrowable(t);
		
		return exceptionState;
	}

	// --------------------------------------------------------------------------------
	public ImgLoadingState getImgLoadingState(String path)
	{
		removeMouseListeners();
		addMouseListener(imgLoadingML);
		
		imgLoadingState.setPath(path);
		
		return imgLoadingState;
	}

	// --------------------------------------------------------------------------------
	public ImgAvailableState getImgAvailableState(Image img)
	{
		removeMouseListeners();
		
		imgAvailableState.setImage(img);
		
		return imgAvailableState;
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void removeMouseListeners()
	{
		for (MouseListener l : getMouseListeners())
			removeMouseListener(l);
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private class ImgLoadingMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 2)
			{
				parent.getController().cancelDisplayImg();
			}
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
