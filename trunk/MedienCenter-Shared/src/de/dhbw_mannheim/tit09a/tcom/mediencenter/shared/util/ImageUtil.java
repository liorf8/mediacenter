package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

public class ImageUtil
{

	public static float calculateScale(Component parent, Image img)
	{
		// calculate both ratios and return the smaller one
		Dimension dim = parent.getSize();
		float ratioH = (float) dim.height / img.getHeight(null);
		float ratioW = (float) dim.width / img.getWidth(null);
		return (ratioH < ratioW ? ratioH : ratioW);
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

	public static void drawCenteredImg(Graphics g, Component component, Image image)
	{
		Dimension d = component.getSize();
		
		g.setColor(component.getBackground());
		g.fillRect(0, 0, d.width, d.height);
		
		g.drawImage(image, (d.width - image.getWidth(null)) / 2, (d.height - image.getHeight(null)) / 2, null);
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
		g2.drawImage(img, (cmpDim.width - w) / 2, (cmpDim.height - h) / 2, w, h, null);
		g2.dispose();
	}

	private ImageUtil()
	{

	}

}
