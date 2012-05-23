package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.remoteimage;

import java.awt.Graphics;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.GraphicsUtil;

public class ImgLoadingState extends RICState
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private String				path;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	ImgLoadingState(RemoteImageComponent ric)
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
		GraphicsUtil.drawCenteredString(g, ric, "Loading '" + path + "' ...", 0, -15);
		GraphicsUtil.drawCenteredString(g, ric, "Double-click to cancel.", 0, 15);
	}
	
	// --------------------------------------------------------------------------------
	public void setPath(String path)
	{
		this.path = path;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
