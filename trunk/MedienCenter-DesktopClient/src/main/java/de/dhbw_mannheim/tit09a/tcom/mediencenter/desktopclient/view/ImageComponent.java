package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;

public class ImageComponent extends JComponent
{
	private static final long	serialVersionUID	= -6587967099141907509L;

	private boolean				doScale;
	private boolean				onlyScaleIfImgIsBigger;
	private Image				img;

	public ImageComponent(Image img)
	{
		this.setImage(img);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		System.out.println("Painting: " +img);
		if (img == null)
		{
			MediaUtil.drawCenteredString(g, this, "No image to display");
		}
		else
		{
			if (getDoScale())
			{
				MediaUtil.drawScaledAndCenteredImg(g, this, img, MediaUtil.calculateScale(this, img, onlyScaleIfImgIsBigger));
			}
			else
			{
				MediaUtil.drawCenteredImg(g, this, img);
			}
		}
	}

	public Image getImg()
	{
		return img;
	}

	public void setImage(Image img)
	{
		this.img = img;
		repaint();
	}

	public boolean getDoScale()
	{
		return doScale;
	}

	public void setDoScale(boolean doScale)
	{
		this.doScale = doScale;
		repaint();
	}

	public boolean isOnlyScaleIfImgIsBigger()
	{
		return onlyScaleIfImgIsBigger;
	}

	public void setOnlyScaleIfImgIsBigger(boolean onlyScaleIfImgIsBigger)
	{
		this.onlyScaleIfImgIsBigger = onlyScaleIfImgIsBigger;
	}

}
