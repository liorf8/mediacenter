package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.vlcj;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

public class VLCjTest
{
	private static LibVlc				libvlc;
	private static String[]				libvlcArgs;
	private static MediaPlayerFactory	mediaPlayerFactory;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
		if (vlcInstallDir == null)
			vlcInstallDir = "D:\\mhertram\\VLCPortable\\App\\vlc";
		NativeLibrary.addSearchPath("libvlc", vlcInstallDir);
		NativeLibrary.addSearchPath("libvlccore", vlcInstallDir);

		libvlc = LibVlcFactory.factory().atLeast("2.0.0").synchronise().create();
		libvlcArgs = new String[] { "--vout", "dummy" }; // so that no video output is created

		mediaPlayerFactory = new MediaPlayerFactory(libvlc, libvlcArgs);

		MediaListPlayer listPlayer = mediaPlayerFactory.newMediaListPlayer();
		MediaList mediaList = mediaPlayerFactory.newMediaList();
		HeadlessMediaPlayer headlessPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		
		listPlayer.setMediaList(mediaList);
		listPlayer.setMediaPlayer(headlessPlayer);
		
		Path mediaFile = Paths.get("D:\\Downloads\\cougar2x20.mkv");
		String mrl = mediaFile.toUri().toString();
		
		mediaList.addMedia(mrl);
		System.out.println(mediaList.mrls());
		listPlayer.play();
		
		System.out.println(listPlayer.isPlaying());
		System.out.println(headlessPlayer.isPlaying());
		
		//headlessPlayer.playMedia(mediaFile.toUri().toString());
		//System.out.println(headlessPlayer.getMediaMeta().getTitle());
		
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		{
			listPlayer.stop();
			System.out.println("finally");
			headlessPlayer.release();
			mediaList.release();
			listPlayer.release();
			mediaPlayerFactory.release();
		}


	}

}
