package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.screen.ScreenTab.EmbeddedMediaPlayerComponentImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class PauseAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= -3005089005821032330L;

	public PauseAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Pause.png");
		setShortDescription("Pause playback");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new PauseWorker(mainFrame, action, e);
	}

	private class PauseWorker extends AbstractTaskPanelSwingWorker
	{
		public PauseWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			System.out.println("[PauseWorker]");
			
			mainController.ensureVlcIsLoaded();
			
			EmbeddedMediaPlayerComponentImpl mediaPlayerComp = mainFrame.getScreenTab().getMediaPlayerComponent();
			StreamPlayer streamPlayer = MainController.getInstance().getServerConnection().getStreamPlayer();
			if (mediaPlayerComp.getMediaPlayer().isPlaying())
			{
				streamPlayer.pause();
				mediaPlayerComp.getMediaPlayer().pause();
				mainFrame.getMediaToolBar().setPlayAction(true);
			}
		}
	}

}
