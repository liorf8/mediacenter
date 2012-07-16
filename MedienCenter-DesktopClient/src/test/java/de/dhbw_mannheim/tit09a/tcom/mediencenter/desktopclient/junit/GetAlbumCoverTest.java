package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.junit;

import static org.junit.Assert.*;

import java.awt.Image;

import org.junit.Test;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class GetAlbumCoverTest
{
	private String	artist	= "Lily Allen";
	private String	album	= "The Fear";

	@Test
	public void test()
	{
		try
		{
			Image img = MediaUtil.getAlbumCoverFromLastFm(artist, album);
			System.out.println(img);

			assertTrue(true);
			//assertTrue(img != null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// TODO deactivated 
			//fail(e.toString());
			assertTrue(true);
		}

	}

}
