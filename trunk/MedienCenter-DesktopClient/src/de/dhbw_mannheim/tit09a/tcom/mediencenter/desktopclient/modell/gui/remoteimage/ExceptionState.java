package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.remoteimage;

import java.awt.Graphics;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ImageUtil;

public class ExceptionState extends RemoteImageState
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Throwable			t;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	ExceptionState(RemoteImageComponent ric)
	{
		super(ric);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g)
	{
		System.out.println(getClass().getSimpleName() + ": paintComponent() @" + Thread.currentThread());

		ImageUtil.drawCenteredString(g, ric, "Exception occured:", 0, -20);
		ImageUtil.drawCenteredString(g, ric, t.getClass().getSimpleName() + ":");
		ImageUtil.drawCenteredString(g, ric, t.getMessage(), 0, 20);
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	void setThrowable(Throwable t)
	{
		this.t = t;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
