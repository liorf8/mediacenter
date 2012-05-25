package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.remoteimage;

import java.awt.Graphics;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;

public class ExceptionState extends RICState
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
		t.printStackTrace();

		MediaUtil.drawCenteredString(g, ric, "Exception occured:", 0, -20);
		MediaUtil.drawCenteredString(g, ric, t.getClass().getSimpleName() + ":");
		MediaUtil.drawCenteredString(g, ric, t.getMessage() == null ? "-" : t.getMessage(), 0, 20);
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
