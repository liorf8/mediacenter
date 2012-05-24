package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MediaInfoPanel extends JPanel
{
	private static final long	serialVersionUID	= 6804221637028312266L;
	
	public MediaInfoPanel()
	{
		this.add(new JLabel("no media selected"));
	}
	
	public void resetMediaInfoPanel()
	{
		this.removeAll();
	}
	
	public void setMediaMeta(JPanel mediaMetaPanel)
	{
		this.add(mediaMetaPanel);
	}
	
	public void setMediaDetails(JPanel mediaDetailsPanel)
	{
		this.add(mediaDetailsPanel);
	}
	
	public void setTrackInfo(JPanel trackInfoPanel)
	{
		this.add(trackInfoPanel);
	}

}
