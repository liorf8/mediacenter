package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import uk.co.caprica.vlcj.player.MediaPlayer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TranscodeOptions;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class PlayFileAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= -5525233161285440111L;

	private boolean				playFromElapsedTime;

	public PlayFileAction(MainFrame mainFrame, boolean playFromElapsedTime)
	{
		super(mainFrame);
		setActionCommand("Play file");

		this.playFromElapsedTime = playFromElapsedTime;

		if (playFromElapsedTime)
		{
			setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Play from saved.png");
			setShortDescription("Start playing the media from the last position");
		}
		else
		{
			setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Play.png");
			setShortDescription("Start playing the media from the beginning");
		}

	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new PlayFileWorker(mainFrame, action, e);
	}

	private class PlayFileWorker extends AbstractTaskPanelSwingWorker
	{
		public PlayFileWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			System.out.println("[PlayFileInfoWorker]");

			mainController.ensureVlcIsLoaded();

			FileInfo selectedFileInfo = mainFrame.getMediaLibraryTab().getSelectedFileInfo();
			System.out.println("[PlayFileWorker] selectedFileInfo=" + selectedFileInfo);
			if (selectedFileInfo == null)
			{
				throw new IllegalArgumentException("No media file selected!");
			}

			StreamPlayer streamPlayer = mainController.getServerConnection().getStreamPlayer();
			// stopping

			publish("Setting streaming settings");
			// Get Streaming settings
			Settings settings = mainController.getSettings();
			String protocol = settings.getProperty(Settings.KEY_STREAMING_PROTOCOL);
			int destPort = settings.getPropertyAsInt(Settings.KEY_STREAMING_DESTINATION_PORT);
			boolean transcodingIsActive = settings.getPropertyAsBoolean(Settings.KEY_STREAMING_TRANSCODING_ACTIVE);
			TranscodeOptions transcodeOptions = null;
			if (transcodingIsActive)
			{
				String videoCodec = settings.getProperty(Settings.KEY_STREAMING_TRANSCODING_VIDEO_CODEC);
				int videoKBitRate = settings.getPropertyAsInt(Settings.KEY_STREAMING_TRANSCODING_VIDEO_KBIT_RATE);
				String audioCodec = settings.getProperty(Settings.KEY_STREAMING_TRANSCODING_AUDIO_CODEC);
				int audioKBitRate = settings.getPropertyAsInt(Settings.KEY_STREAMING_TRANSCODING_AUDIO_KBIT_RATE);
				boolean audioSync = settings.getPropertyAsBoolean(Settings.KEY_STREAMING_TRANSCODING_AUDIO_SYNC);
				boolean deinterlace = settings.getPropertyAsBoolean(Settings.KEY_STREAMING_TRANSCODING_DEINTERLACE);
				transcodeOptions = new TranscodeOptions(videoCodec, audioCodec, videoKBitRate, audioKBitRate, audioSync, deinterlace);
			}
			System.out.println("[PlayFileWorker] transcodeOptions=" + transcodeOptions);
			String streamPath = streamPlayer.setAndGetStreamTarget(protocol, destPort, transcodeOptions);
			System.out.println("[PlayFileWorker] streampath=" + streamPath);
			setProgress(50);

			publish("Starting playback");
			mainFrame.getScreenTab().setCurrentFile(selectedFileInfo);
			MediaPlayer mediaPlayer = mainFrame.getScreenTab().getMediaPlayerComponent().getMediaPlayer();
			mediaPlayer.playMedia(streamPath);
			streamPlayer.play(selectedFileInfo.getPath());
			if (playFromElapsedTime)
			{
				long elapsedTime = selectedFileInfo.getElapsedTime();
				if (elapsedTime > 0)
				{
					streamPlayer.setTime(elapsedTime);
				}
			}
			mainFrame.getMediaToolBar().setPlayAction(false);
			mainFrame.getMediaToolBar().setDuration(mainController.getServerConnection().getStreamPlayer().getLength());
			System.out.println("[PlayFileWorker] DONE!");
		}
	}
}
