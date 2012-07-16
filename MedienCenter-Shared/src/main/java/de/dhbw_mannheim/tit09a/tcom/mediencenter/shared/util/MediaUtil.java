package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MediaUtil
{
	public static final Color	COLOR_DARK_GREEN	= new Color(0, 100, 10);

	public static final String	PATH_IMGS			= "/imgs/";
	public static final String	PATH_IMGS_16x16		= PATH_IMGS + "16 x 16/";
	public static final String	PATH_IMGS_22x22		= PATH_IMGS + "22 x 22/";
	public static final String	PATH_IMGS_32x32		= PATH_IMGS + "32 x 32/";
	public static final String	PATH_IMGS_64x64		= PATH_IMGS + "64 x 64/";

	public static final int		MEDIA_TYPE_OTHER	= -1;
	public static final int		MEDIA_TYPE_IMAGE	= 0;
	public static final int		MEDIA_TYPE_AUDIO	= 1;
	public static final int		MEDIA_TYPE_VIDEO	= 2;

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon getImageIconFromResource(String path, String description)
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
		if (data == null || data.length == 0)
			return null;

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

	public static byte[] writeImage(BufferedImage bi) throws IOException
	{
		if (bi == null)
			return null;

		ByteArrayOutputStream baos = null;
		try
		{
			// open
			baos = new ByteArrayOutputStream(1000);

			// write
			ImageIO.write(bi, "jpeg", baos); // i guess that it is jpeg

			// close
			baos.flush();
			byte[] resultImageAsRawBytes = baos.toByteArray();
			return resultImageAsRawBytes;
		}
		finally
		{
			if (baos != null)
				baos.close();
		}
	}

	public static float calculateScale(Component parent, Image img, boolean onlyScaleIfImgExceedsComp)
	{
		// calculate both ratios and return the smaller one, so that the image is not bigger than the parent Component
		Dimension parentDim = parent.getSize();

		// if parent is bigger than the img, do not scale if flag is enabled
		if (onlyScaleIfImgExceedsComp && (parentDim.width > img.getWidth(null) && parentDim.height > img.getWidth(null)))
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

	public static int detectMediaType(String contentType)
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

	// if image url starts with "..", this will be replaced with the host
	public static Image extractImageFromWebsite(URL url, Pattern imgUrlPattern)
	{
		System.out.println("[EXTRACT IMG] url: " + url);
		InputStream is = null;
		try
		{
			is = url.openStream();
			Scanner scanner = new Scanner(is);
			Matcher mAlbumCover = imgUrlPattern.matcher("");
			while (scanner.hasNextLine())
			{
				mAlbumCover.reset(scanner.nextLine());
				if (mAlbumCover.find())
				{
					// return Toolkit.getDefaultToolkit().getImage(new URL(mAlbumCover.group(1)));
					String imgUrl = mAlbumCover.group(1);
					if (imgUrl.startsWith(".."))
					{
						imgUrl = imgUrl.replaceFirst("^\\.\\.", url.getProtocol() + "://" + url.getHost());
					}

					System.out.println("[EXTRACT IMG] imgUrl: " + imgUrl);
					return ImageIO.read(new URL(imgUrl));
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("Could not load img. url=" + url + ", pattern=" + imgUrlPattern);
			e.printStackTrace();
		}
		finally
		{
			if (is != null)
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		System.err.println("URL not found: " + url);
		return null;
	}

	public static Image getAlbumCoverFromLastFm(String artist, String album)
	{
		if (artist == null || album == null)
			return null;

		/*
		 * http://www.lastfm.de/music/Lily+Allen/It%27s+Not+Me,+It%27s+You
		 * 
		 * ...
		 * 
		 * <span id="albumCover" itemprop="image" class="albumCover coverMega"><img width="174"
		 * src="http://userserve-ak.last.fm/serve/174s/72329452.png" class="art" id="albumCover" itemprop="image" class="albumCover coverMega"/><span
		 * class="jewelcase"></span></span>
		 * 
		 * ...
		 */
		try
		{
			URL url = NetUtil.buildLastFmAlbumUri(artist, album).toURL();
			Pattern pAlbumCover = Pattern.compile(".*<span id=\"albumCover\".*?><img.*?src=\"(.*?)\".*");
			return extractImageFromWebsite(url, pAlbumCover);
		}
		catch (MalformedURLException | URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Image getMovieCoverFromNewVideo(String movie)
	{
		if (movie == null)
			return null;

		/*
		 * http://www.new-video.de/film-men-in-black-3/
		 * 
		 * <tr><td bgcolor="#ffffff" align="center"><img src="../co/rc/r.mib3so12.jpg" width="300" alt="Men in Black 3 - Plakat/Cover"><p
		 * class="kl">Cover / Filmplakat "Men in Black 3"</p></td></tr>
		 */
		try
		{
			URL url = NetUtil.buildNewVideoUri(movie).toURL();
			Pattern pAlbumCover = Pattern.compile(".*<img src=\"(.*?)\".*? alt=\".*?Plakat/Cover\">");
			return extractImageFromWebsite(url, pAlbumCover);
		}
		catch (MalformedURLException | URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String getTitleFromMovieFilename(String movieFilename)
	{
		// suffix
		movieFilename = movieFilename.replaceAll("\\.\\w+$", "");

		// prefix group / suffi group
		movieFilename = movieFilename.replaceAll("^\\w+-", "");
		movieFilename = movieFilename.replaceAll("-\\w+$", "");

		// dots
		movieFilename = movieFilename.replaceAll("\\.", " ");

		// tags
		movieFilename = movieFilename.replaceAll("HDTV", "");
		movieFilename = movieFilename.replaceAll("HDDVD", "");
		movieFilename = movieFilename.replaceAll("DVD-Rip", "");
		movieFilename = movieFilename.replaceAll("DVDR", "");
		movieFilename = movieFilename.replaceAll("BluRay", "");
		movieFilename = movieFilename.replaceAll("Blu-ray", "");
		movieFilename = movieFilename.replaceAll("720p", "");
		movieFilename = movieFilename.replaceAll("x264", "");
		movieFilename = movieFilename.replaceAll("XviD", "");
		movieFilename = movieFilename.replaceAll("AC3", "");
		movieFilename = movieFilename.replaceAll("AC3D", "");
		movieFilename = movieFilename.replaceAll("DTS", "");
		movieFilename = movieFilename.replaceAll("DL", "");
		movieFilename = movieFilename.replaceAll("Dubbed", "");
		movieFilename = movieFilename.replaceAll("5 1", "");

		// 2 or more spaces
		movieFilename = movieFilename.replaceAll("\\s{2,}", " ");
		movieFilename = movieFilename.trim();
		return movieFilename;
	}

	private MediaUtil()
	{

	}

}
