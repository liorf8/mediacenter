package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MediaUtil
{
	public static final Color	COLOR_DARK_GREEN	= new Color(0, 100, 10);

	public static final String	PATH_IMGS			= "/imgs/";
	public static final String	PATH_IMGS_16x16		= PATH_IMGS + "16 x 16/";
	public static final String	PATH_IMGS_22x22		= PATH_IMGS + "22 x 22/";
	public static final String	PATH_IMGS_32x32		= PATH_IMGS + "32 x 32/";

	public static final int		MEDIA_TYPE_OTHER	= -1;
	public static final int		MEDIA_TYPE_IMAGE	= 0;
	public static final int		MEDIA_TYPE_AUDIO	= 1;
	public static final int		MEDIA_TYPE_VIDEO	= 2;

	public static ImageIcon createImageIcon(String path)
	{
		return createImageIcon(path, path);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path, String description)
	{
		URL imgURL = MediaUtil.class.getResource(path);
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

	public static BufferedImage readImage(byte[] data) throws IOException
	{
		ByteArrayInputStream bais = null;
		try
		{
			bais = new ByteArrayInputStream(data);
			return ImageIO.read(bais);
		}
		finally
		{
			if (bais != null)
				bais.close();
		}
	}

	public static float calculateScale(Component parent, Image img, boolean onlyScaleIfImgIsBigger)
	{
		// calculate both ratios and return the smaller one, so that the image is not bigger than the parent Component
		Dimension parentDim = parent.getSize();

		// if parent is bigger than the img, do not scale if flag is enabled
		if (onlyScaleIfImgIsBigger && (parentDim.width > img.getWidth(null) && parentDim.height > img.getWidth(null)))
			return 1.0f;

		float ratioH = (float) parentDim.height / img.getHeight(null);
		float ratioW = (float) parentDim.width / img.getWidth(null);
		return (ratioH < ratioW ? ratioH : ratioW);
	}

	public static void drawCenteredImg(Graphics g, Component component, Image img)
	{
		g.setColor(component.getBackground());
		Dimension d = component.getSize();
		g.fillRect(0, 0, d.width, d.height);
		g.drawImage(img, (d.width - img.getWidth(null)) / 2, (d.height - img.getHeight(null)) / 2, null);
	}

	public static void drawCenteredString(Graphics g, Component component, String s)
	{
		drawCenteredString(g, component, s, 0, 0);
	}

	public static void drawCenteredString(Graphics g, Component component, String s, int xOffset, int yOffset)
	{
		Dimension d = component.getSize();
		FontMetrics fm = g.getFontMetrics();
		int x = (d.width - fm.stringWidth(s)) / 2 + xOffset;
		int y = (fm.getAscent() + (d.height - (fm.getAscent() + fm.getDescent())) / 2) + yOffset;
		g.drawString(s, x, y);
	}

	public static void drawScaledAndCenteredImg(Graphics g, Component component, Image img, float scale)
	{
		Dimension cmpDim = component.getSize();

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(component.getBackground());
		g2.fillRect(0, 0, cmpDim.width, cmpDim.height);

		int w = img.getWidth(null);
		int h = img.getHeight(null);

		// scaling
		if (scale != 1f && scale > 0f)
		{
			// only works on Graphics2D
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			w = (int) (w * scale);
			h = (int) (h * scale);
		}

		// centering
		int x = (cmpDim.width - w) / 2;
		int y = (cmpDim.height - h) / 2;

		g2.drawImage(img, x, y, w, h, null);
		g2.dispose();
	}

	public static int detectMedia(String contentType)
	{
		if (contentType == null)
			return MEDIA_TYPE_OTHER;
		
		if (contentType.startsWith("image"))
			return MEDIA_TYPE_IMAGE;
		if (contentType.startsWith("audio"))
			return MEDIA_TYPE_AUDIO;
		if (contentType.startsWith("video"))
			return MEDIA_TYPE_VIDEO;
		else
			return MEDIA_TYPE_OTHER;
	}

	private MediaUtil()
	{

	}

}
