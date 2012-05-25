package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import uk.co.caprica.vlcj.player.AudioTrackInfo;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.InternetUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play.AudioInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play.ImageInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play.NoMediaInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play.PlayTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play.VideoInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;

public class RetrieveMediaInfoAction extends AbstractSwingWorkerAction<String, String>
{

	private static final long	serialVersionUID	= -4221343794630374803L;

	private FileInfo			selectedFileInfo;

	public RetrieveMediaInfoAction(MainFrame mainFrame)
	{
		super(mainFrame, "INTERNAL_RetrieverMediaInfoAction", null);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new RetrieveMediaInfoWorker(mainFrame, action, e);
	}

	public FileInfo getSelectedFileInfo()
	{
		return selectedFileInfo;
	}

	public void setSelectedFileInfo(FileInfo selectedFi)
	{
		this.selectedFileInfo = selectedFi;
	}

	private class RetrieveMediaInfoWorker extends AbstractTaskPanelSwingWorker
	{
		public RetrieveMediaInfoWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			System.out.println("-----");
			System.out.println("Selected: " + selectedFileInfo);
			PlayTab playTab = mainFrame.getPlayTab();

			publish("Detecting media");
			int mediaType = MediaUtil.detectMedia(selectedFileInfo.getContentType());
			System.out.println("MediaType: " + mediaType);
			setProgress(10);
			
			// TODO: apparently .flac cannot be parsed correct yet...
			if(selectedFileInfo.getName().endsWith(".flac"))
			{
				mediaType = MediaUtil.MEDIA_TYPE_AUDIO;
			}
			
			
			InfoPlayer infoPlayer = mainController.getSimonConnection().getInfoPlayer();
			long length = -1L;
			List<TrackInfo> trackInfoList = null;
			AudioTrackInfo ati = null;
			VideoTrackInfo vti = null;
			MediaDetails mediaDetails = null;
			MediaMeta mediaMeta = null;
			switch (mediaType)
			{
				case 0:
					ImageInfoPanel iip = playTab.getImageInfoPanel();
					playTab.setMediaInfoPanel(iip);
					iip.setFileInfo(selectedFileInfo);
					
					publish("Getting media information");
					trackInfoList = infoPlayer.getTrackInfo(selectedFileInfo.getPath());
					System.out.println("getTrackInfo:" + trackInfoList);
					for (TrackInfo ti : trackInfoList)
					{
						vti = (VideoTrackInfo) ti;
					}
					iip.setTechnicals(vti);
					setProgress(50);
					
					publish("Retrieving image from server");
					Thread.sleep(2000);
					byte[] imgBytes = mainController.getSimonConnection().getSession().getFileBytes(selectedFileInfo.getPath());
					Image img = MediaUtil.readImage(imgBytes);
					iip.setImage(img);
					setProgress(100);

					break;

				case 1:
					AudioInfoPanel aip = playTab.getAudioInfoPanel();
					playTab.setMediaInfoPanel(aip);
					aip.setFileInfo(selectedFileInfo);
					
					publish("Getting media meta data");
					mediaMeta = infoPlayer.getMediaMeta(selectedFileInfo.getPath());
					System.out.println("getMediaMeta:" + mediaMeta);
					aip.setMediaMeta(mediaMeta);
					setProgress(40);

					publish("Getting media information");
					length = infoPlayer.getLength(selectedFileInfo.getPath());
					System.out.println("getLength:" + length);
					setProgress(50);
					
					trackInfoList = infoPlayer.getTrackInfo(selectedFileInfo.getPath());
					System.out.println("getTrackInfo:" + trackInfoList);
					for (TrackInfo ti : trackInfoList)
					{
						ati = (AudioTrackInfo) ti;
					}
					aip.setTechnicals(length, ati);
					setProgress(70);
					
					publish("Retrieving album cover from last.fm");
					Image albumCover = InternetUtil.getAlbumCoverFromLastFm(mediaMeta.getArtist(), mediaMeta.getAlbum());
					aip.setAlbumCover(albumCover);
					setProgress(100);
					
					break;

				case 2:
					VideoInfoPanel vip = playTab.getVideoInfoPanel();
					playTab.setMediaInfoPanel(vip);
					vip.setFileInfo(selectedFileInfo);
					
					publish("Getting media details");
					mediaDetails = infoPlayer.getMediaDetails(selectedFileInfo.getPath());
					System.out.println("getMediaDetails:" + mediaDetails);
					setProgress(100);

					break;

				default:
					NoMediaInfoPanel nmip = playTab.getNoMediaInfoPanel();
					playTab.setMediaInfoPanel(nmip);
					
					nmip.setFileInfo(selectedFileInfo);
					nmip.setMessage("unknown media");
			}
			playTab.validate();
		}
	}

}
