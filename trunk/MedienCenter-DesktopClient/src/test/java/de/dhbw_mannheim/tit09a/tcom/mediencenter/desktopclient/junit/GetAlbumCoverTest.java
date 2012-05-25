package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.junit;

import static org.junit.Assert.*;

import java.awt.Image;

import org.junit.Test;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.InternetUtil;

public class GetAlbumCoverTest
{
	private String	artist	= "Lily Allen";
	private String	album	= "The Fear";

	@Test
	public void test()
	{
		try
		{
			Image img = InternetUtil.getAlbumCoverFromLastFm(artist, album);
			System.out.println(img);

			assertTrue(img != null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.toString());
		}

	}

}
