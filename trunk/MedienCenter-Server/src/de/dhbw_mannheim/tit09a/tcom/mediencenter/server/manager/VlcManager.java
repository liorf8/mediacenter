package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
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
					instance = new VlcManager();
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
	private Map<String, Long>	elapsedTimeMap;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private VlcManager() throws Exception
	{
		super(Level.ALL);

		// Locate VLC installation directory
		String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
		logger.info("VLC install dir: {}", vlcInstallDir);
		if (vlcInstallDir == null)
			vlcInstallDir = "D:\\mhertram\\VLCPortable\\App\\vlc";

		// Locate the dll's
		NativeLibrary.addSearchPath("libvlc", vlcInstallDir);
		NativeLibrary.addSearchPath("libvlccore", vlcInstallDir);
		logger.info("libvlc version: {}", LibVlcVersion.getVersion());

		// Create a factory
		libvlc = LibVlcFactory.factory().atLeast("2.0.0").synchronise().create();
		libvlcArgs = new String[] { "--vout", "dummy" }; // so that no video output is created
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public HeadlessMediaPlayer borrowPlayer() throws NoSuchElementException, Exception
	{
		return mediaPlayerFactory.newHeadlessMediaPlayer();
	}

	// --------------------------------------------------------------------------------
	public void returnPlayer(HeadlessMediaPlayer player) throws Exception
	{
		player.release();
	}

	// --------------------------------------------------------------------------------
	public void saveElapsedTime(String mrl, long time)
	{
		System.out.println("putElapsedTime: " + mrl + "=" + time);
		elapsedTimeMap.put(mrl, time);
	}

	// --------------------------------------------------------------------------------
	public long getElapsedTime(String mrl)
	{
		Long time = elapsedTimeMap.get(mrl);
		System.out.println("getElapsedTime: " + mrl + "=" + time);
		return time == null ? 0L : time;
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	protected void onStart() throws Exception
	{
		mediaPlayerFactory = new MediaPlayerFactory(libvlc, libvlcArgs);
		elapsedTimeMap = Collections.synchronizedMap(new HashMap<String, Long>());
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onShutdown() throws Exception
	{
		if (mediaPlayerFactory != null)
			mediaPlayerFactory.release();
		if (elapsedTimeMap != null)
			elapsedTimeMap.clear();
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
