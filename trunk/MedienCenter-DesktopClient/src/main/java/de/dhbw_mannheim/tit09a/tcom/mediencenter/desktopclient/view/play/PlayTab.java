package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import javax.swing.Icon;
import javax.swing.JFrame;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;

public class PlayTab extends Tab
{
	private static final long	serialVersionUID	= 1L;

	private MediaComponent		mediaComponent;

	public PlayTab(JFrame frame)
	{
		super("Play");
	}

	public MediaComponent getMediaComponent()
	{
		return mediaComponent;
	}

	@Override
	public String getTip()
	{
		return "Play your media";
	}

	@Override
	public Icon getIcon()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
