package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.screen.ScreenTab.EmbeddedMediaPlayerComponentImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class StopAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= -3005089005821032330L;

	public StopAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Stop.png");
		setShortDescription("Stop the playback (reset to start)");
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
			try
			{
				System.out.println("[StopWorker]");

				mainController.ensureVlcIsLoaded();

				EmbeddedMediaPlayerComponentImpl mediaPlayerComp = mainFrame.getScreenTab().getMediaPlayerComponent();
				StreamPlayer streamPlayer = MainController.getInstance().getServerConnection().getStreamPlayer();
				streamPlayer.stop();
				mediaPlayerComp.getMediaPlayer().stop();
				mainFrame.getMediaToolBar().setPlayAction(true);
				mainFrame.getMediaToolBar().setCurrentTime(0L);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw e;
			}

		}
	}

}
