package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.junit;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

public class FindVLC
{
	@Test
	public void test()
	{
		try
		{
			String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
			if (vlcInstallDir == null)
				fail("vlcInstallDir == null (not found)");

			// Locate the dlls (libvlc.dll and libvlccore.dll on Windows)
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcInstallDir);
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreName(), vlcInstallDir);

			// Create a libvlc factory
			LibVlcFactory.factory().atLeast("2.0.0").synchronise().create();
		}
		catch (Exception e)
		{
			fail(e.toString());
		}
	}
}
