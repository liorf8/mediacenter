package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.remoteimage;

import java.awt.Graphics;
import java.awt.Image;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class ImgAvailableState extends RICState
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Image				img;
	private boolean				doScale;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	ImgAvailableState(RemoteImageComponent ric)
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

		if (doScale)
			MediaUtil.drawScaledAndCenteredImg(g, ric, img, MediaUtil.calculateScale(ric, img, false));
		else
			MediaUtil.drawCenteredImg(g, ric, img);
	}
	
	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	void setImage(Image img)
	{
		this.img = img;
	}

	// --------------------------------------------------------------------------------
	void setScaleToFit(boolean doScale)
	{
		this.doScale = doScale;
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
