package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.remoteimage;

import java.awt.Graphics;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ImageUtil;

public class NoImgState extends RemoteImageState
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 153443016991990200L;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public NoImgState(RemoteImageComponent ric)
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
		ImageUtil.drawCenteredString(g, ric, "No image specified!");
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
