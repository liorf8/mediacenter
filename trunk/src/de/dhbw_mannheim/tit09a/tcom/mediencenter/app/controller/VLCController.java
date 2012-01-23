package de.dhbw_mannheim.tit09a.tcom.mediencenter.app.controller;

import com.sun.jna.NativeLibrary;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.app.modell.listener.MediaListPlayerEventListenerImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.app.modell.listener.MediaPlayerEventListenerImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.app.view.MainFrame;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;
import uk.co.caprica.vlcj.version.Version;
import uk.co.caprica.vlcj.version.VlcjVersion;

public class VLCController
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private static VLCController instance;
    private static String vlcInstallDir;
    private static Version libVlcVersion;
    //0.9 -> sofortiger crash
    private static Version vlcjVersion;

    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer mediaPlayer;
    private CanvasVideoSurface videoSurface;
    private MediaListPlayer mediaListPlayer;
    private MediaList mediaList;

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    private VLCController(boolean initIfNotAlready) throws Exception
    {
	if (initIfNotAlready)
	{
	    addSearchPaths();

	    getVLCInstallDir();
	    getVlcjVersion();
	    getLibVlcVersion();

	    System.out.println("VLCInstallDir=" + vlcInstallDir);
	    System.out.println("LibVlcVersion=" + libVlcVersion);
	    System.out.println("vlcjVersion=" + vlcjVersion);

	    mediaPlayerFactory = new MediaPlayerFactory(new String[] { "--no-video-title-show" });
	    mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();

	    videoSurface = mediaPlayerFactory.newVideoSurface(MainFrame.getInstance()
		    .getVideoCanvas());
	    mediaPlayer.setVideoSurface(getVideoSurface());

	    mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
	    // Important: associate the media player with the media list player
	    mediaListPlayer.setMediaPlayer(mediaPlayer);
	    mediaList = mediaPlayerFactory.newMediaList();

	    addListeners();
	}

    }

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static VLCController getInstance(boolean initIfNotAlready)
    {
	try
	{
	    if (instance == null)
	    {
		return instance = new VLCController(initIfNotAlready);
	    }
	    return instance;
	}
	catch (Exception e)
	{
	    return null;
	}
    }

    // --------------------------------------------------------------------------------
    public void setPosition(float progressInPercent)
    {
	try
	{
	    mediaPlayer.setPosition(progressInPercent);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    // --------------------------------------------------------------------------------
    public EmbeddedMediaPlayer getMediaPlayer()
    {
	return mediaPlayer;
    }

    // --------------------------------------------------------------------------------
    public MediaListPlayer getMediaListPlayer()
    {
	return mediaListPlayer;
    }

    // --------------------------------------------------------------------------------
    public MediaList getMediaList()
    {
	return mediaList;
    }

    // --------------------------------------------------------------------------------
    public void playMedia(String mrl)
    {
	MainFrame.getInstance().setGUIForPlayMedia(mrl);
	mediaPlayer.playMedia(mrl);
    }

    // --------------------------------------------------------------------------------
    public CanvasVideoSurface getVideoSurface()
    {
	return videoSurface;
    }

    // --------------------------------------------------------------------------------
    public void releaseResources()
    {
	if (mediaPlayerFactory != null)
	{
	    mediaPlayerFactory.release();
	}
	if (mediaPlayer != null)
	{
	    mediaPlayer.release();
	}
	if (mediaListPlayer != null)
	{
	    mediaListPlayer.release();
	}
    }

    // --------------------------------------------------------------------------------
    public static String getVLCInstallDir()
    {
	if (vlcInstallDir == null)
	{
	    vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
	}
	return vlcInstallDir;
    }

    // --------------------------------------------------------------------------------
    public static Version getLibVlcVersion()
    {
	if (libVlcVersion == null)
	{
	    // The following line throws an Exception with VLC Version: '1.1.11 The Luggage'
	    // due to NumberFormatException for '11 The Luggage' (it was split with Pattern [-.]):
	    /* libVlcVersion = LibVlcVersion.getVersion(); */

	    // Fix:
	    String version = LibVlc.INSTANCE.libvlc_get_version().replaceFirst("\\s", "-");
	    libVlcVersion = new Version(version);
	}
	return libVlcVersion;
    }

    // --------------------------------------------------------------------------------
    public static Version getVlcjVersion()
    {
	if (vlcjVersion == null)
	{
	    vlcjVersion = VlcjVersion.getVersion();
	}
	return vlcjVersion;
    }

    // --------------------------------------------------------------------------------
    // -- Private Method(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private void addListeners()
    {
	mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventListenerImpl());

	mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventListenerImpl());
    }

    // --------------------------------------------------------------------------------
    private void addSearchPaths() throws Exception
    {
	NativeLibrary.addSearchPath("libvlc", vlcInstallDir);
	NativeLibrary.addSearchPath("libvlccore", vlcInstallDir);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
