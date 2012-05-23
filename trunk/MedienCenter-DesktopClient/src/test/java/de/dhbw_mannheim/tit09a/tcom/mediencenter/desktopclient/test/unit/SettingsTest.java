package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.unit;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;

public class SettingsTest
{

	@Test
	public void test()
	{
		try
		{
			Settings settings = new Settings();

			settings.storeProperties();
		}
		catch (IOException e)
		{
			fail(e.toString());
		}
	}

}
