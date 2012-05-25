package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public abstract class MediaInfoPanel extends JPanel
{
	private static final long	serialVersionUID	= 6804221637028312266L;

	protected MainFrame			mainFrame;
	protected FileInfo			fi;
	protected ImageIcon			imgLastFm;

	public MediaInfoPanel(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		loadImgs();
	}

	public void setFileInfo(FileInfo fi)
	{
		this.fi = fi;
		String title;
		if (fi != null)
		{
			title = "<html><b>" + fi.getPath() + "</b> (MIME: " + fi.getContentType() + ")</html>";
		}
		else
		{
			title = "";
		}
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title));
	}

	public abstract void reset();

	protected void loadImgs()
	{
		imgLastFm = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "lastfm.png", "www.last.fm");
	}
}
