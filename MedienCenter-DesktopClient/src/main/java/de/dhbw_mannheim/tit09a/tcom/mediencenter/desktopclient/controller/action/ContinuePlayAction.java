package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import uk.co.caprica.vlcj.player.MediaPlayer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class ContinuePlayAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 3857043474779708184L;

	public ContinuePlayAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setActionCommand("Continue play");
		setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Play.png");
		setShortDescription("Continue playback");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new ContinuePlayWorker(mainFrame, action, e);
	}

	private class ContinuePlayWorker extends AbstractTaskPanelSwingWorker
	{
		public ContinuePlayWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			System.out.println("[ContinuePlayWorker]");
			
			mainController.ensureVlcIsLoaded();
			
			StreamPlayer streamPlayer = mainController.getServerConnection().getStreamPlayer();
			if (streamPlayer.play())
			{
				MediaPlayer mediaPlayer = mainFrame.getScreenTab().getMediaPlayerComponent().getMediaPlayer();
				mediaPlayer.playMedia(streamPlayer.getStreamTarget());
			}
			else
			{
				throw new IllegalArgumentException("No media file available for streaming!");
			}
			mainFrame.getMediaToolBar().setPlayAction(false);
		}

	}

}
