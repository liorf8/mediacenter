package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.util.logging.Level;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

public class VlcManager extends Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private volatile static VlcManager	instance	= null;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static VlcManager getInstance() throws Exception
	{
		if (instance == null)
		{
			synchronized (VlcManager.class)
			{
				if (instance == null)
				{
					instance = new VlcManager();
					instance.start();
				}
			}
		}
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final LibVlc		libvlc;
	private final String[]		libvlcArgs;
	private MediaPlayerFactory	mediaPlayerFactory;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private VlcManager() throws Exception
	{
		super(Level.ALL);

		// Locate VLC installation directory
		String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
		if (vlcInstallDir == null)
			vlcInstallDir = "D:\\mhertram\\VLCPortable\\App\\vlc";
		logger.info("VLC install dir: {}", vlcInstallDir);

		// Locate the dll's
		NativeLibrary.addSearchPath("libvlc", vlcInstallDir);
		NativeLibrary.addSearchPath("libvlccore", vlcInstallDir);
		logger.info("libvlc version: {}", LibVlcVersion.getVersion());

		// Create a libvlc factory
		libvlc = LibVlcFactory.factory().atLeast("2.0.0").synchronise().create();
		libvlcArgs = new String[] { "--vout", "dummy" }; // so that no video output is created
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public HeadlessMediaPlayer buildHeadlessPlayer()
	{
		return mediaPlayerFactory.newHeadlessMediaPlayer();
	}

	// --------------------------------------------------------------------------------
	public void releasePlayer(MediaPlayer player)
	{
		player.release();
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	protected void onStart() throws Exception
	{
		mediaPlayerFactory = new MediaPlayerFactory(libvlc, libvlcArgs);
		logger.debug("Created {}", mediaPlayerFactory);
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onShutdown() throws Exception
	{
		logger.debug("Releasing mediaPlayerFactory: {}", mediaPlayerFactory);
		if (mediaPlayerFactory != null)
			mediaPlayerFactory.release();
	}
	

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
