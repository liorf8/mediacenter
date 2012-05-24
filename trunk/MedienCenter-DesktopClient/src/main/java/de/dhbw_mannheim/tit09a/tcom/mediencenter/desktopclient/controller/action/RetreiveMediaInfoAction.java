package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.nio.file.FileSystemException;
import java.util.List;

import javax.swing.AbstractAction;

import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.TrackInfo;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnection;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;

public class RetreiveMediaInfoAction extends AbstractSwingWorkerAction<String, String>
{

	private static final long	serialVersionUID	= -4221343794630374803L;

	public RetreiveMediaInfoAction(MainFrame mainFrame)
	{
		super(mainFrame, "INTERNAL_RetreiverMediaInfoAction", null);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new RetreiceMediaInfoWorker(mainFrame, action, e);
	}

	private class RetreiceMediaInfoWorker extends AbstractTaskPanelSwingWorker
	{
		public RetreiceMediaInfoWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			FileInfo fi = mainFrame.getPlayTab().getSelectedFileInfo();

			System.out.println("-----");
			System.out.println("Selected: " + fi);
				try
				{
					InfoPlayer infoPlayer = MainController.getInstance().getSimonConnection().getInfoPlayer();
					setProgress(10);
					
					publish("Getting length");
					long length = infoPlayer.getLength(fi.getPath());
					System.out.println("getLength:" + length);
					setProgress(10);
					
					publish("Getting media details");
					MediaDetails mediaDetails = infoPlayer.getMediaDetails(fi.getPath());
					System.out.println("getMediaDetails:" + mediaDetails);
					setProgress(40);
					
					publish("Getting media meta");
					MediaMeta mediaMeta = infoPlayer.getMediaMeta(fi.getPath());
					System.out.println("getMediaMeta:" + mediaMeta);
					setProgress(70);
					
					publish("Getting trackinfo");
					List<TrackInfo> trackInfoList = infoPlayer.getTrackInfo(fi.getPath());
					System.out.println("getTrackInfo:" + trackInfoList);
					setProgress(100);
				}
				catch (ServerException | FileSystemException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	}

}
