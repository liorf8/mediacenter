package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util;

import java.awt.Color;
import java.net.URL;

import javax.swing.ImageIcon;

public class MiscUtil
{

	public static final Color	DARK_GREEN	= new Color(0, 100, 10);

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path, String description)
	{
		URL imgURL = MiscUtil.class.getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL, description);
		}
		else
		{
			System.err.println("Couldn't find resource: " + path);
			return null;
		}
	}
}
