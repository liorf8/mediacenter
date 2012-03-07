package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.remoteimage;

import java.awt.Graphics;
import java.awt.Image;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ImageUtil;

public class ImgAvailableState extends RemoteImageState
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Image				img;
	private boolean				scaleToFit;

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
	public void paintComponent(final Graphics g)
	{
		System.out.println(System.currentTimeMillis() + " - " +getClass().getSimpleName() + ": paintComponent() @" + Thread.currentThread());

		if (scaleToFit)
			ImageUtil.drawScaledAndCenteredImg(g, ric, img, ImageUtil.calculateScale(ric, img));
		else
			ImageUtil.drawCenteredImg(g, ric, img);
	}

	// --------------------------------------------------------------------------------
	public void setScaleToFit(boolean scaleToFit)
	{
		this.scaleToFit = scaleToFit;
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	void setImage(Image img)
	{
		this.img = img;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
