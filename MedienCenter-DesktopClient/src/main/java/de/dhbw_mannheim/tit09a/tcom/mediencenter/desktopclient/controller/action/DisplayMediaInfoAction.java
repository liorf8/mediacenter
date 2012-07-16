package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import uk.co.caprica.vlcj.player.AudioTrackInfo;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.TrackDescription;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.media.ExtendedAudioTrackInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.media.ExtendedVideoTrackInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.AudioInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.ImageInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.MediaLibraryTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.NoMediaInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.VideoInfoPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.MediaMetaRemote;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class DisplayMediaInfoAction extends AbstractSwingWorkerAction
{

	private static final long	serialVersionUID	= -4221343794630374803L;

	public DisplayMediaInfoAction(MainFrame mainFrame)
	{
		super(mainFrame);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new DisplayMediaInfoWorker(mainFrame, action, e);
	}

	private class DisplayMediaInfoWorker extends AbstractTaskPanelSwingWorker
	{
		private MediaLibraryTab				playTab;
		private NoMediaInfoPanel	nmip;
		private ImageInfoPanel		iip;
		private AudioInfoPanel		aip;
		private VideoInfoPanel		vip;

		public DisplayMediaInfoWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);

			playTab = mainFrame.getMediaLibraryTab();
			nmip = playTab.getNoMediaInfoPanel();
			iip = playTab.getImageInfoPanel();
			aip = playTab.getAudioInfoPanel();
			vip = playTab.getVideoInfoPanel();
		}

		@Override
		protected void work() throws Exception
		{
			System.out.println("-----");
			FileInfo selectedFileInfo = playTab.getSelectedFileInfo();
			System.out.println("Selected: " + selectedFileInfo);

			publish("Detecting media type");
			int mediaType = MediaUtil.detectMediaType(selectedFileInfo.getContentType());
			// TODO: apparently .flac cannot be parsed correct yet...
			if (selectedFileInfo.getName().endsWith(".flac"))
			{
				mediaType = MediaUtil.MEDIA_TYPE_AUDIO;
			}

			System.out.println("MediaType: " + mediaType);
			setProgress(10);

			InfoPlayer infoPlayer = mainController.getServerConnection().getInfoPlayer();
			long length = -1L;
			List<TrackInfo> trackInfoList = null;
			AudioTrackInfo ati = null;
			VideoTrackInfo vti = null;
			MediaDetails mediaDetails = null;
			MediaMetaRemote mediaMeta = null;
			switch (mediaType)
			{
				case 0:
					playTab.setMediaInfoPanel(iip);
					iip.setFileInfo(selectedFileInfo);
					setProgress(15);

					publish("Getting media information");
					trackInfoList = infoPlayer.getTrackInfo(selectedFileInfo.getPath());
					System.out.println("getTrackInfo:" + trackInfoList);
					setProgress(30);
					for (TrackInfo ti : trackInfoList)
					{
						vti = (VideoTrackInfo) ti;
					}
					if (vti != null)
					{
						iip.setVideoTrackInfo(vti);
					}
					setProgress(50);

					publish("Getting image from server");
					byte[] imgBytes = mainController.getServerConnection().getSession().getFileBytes(selectedFileInfo.getPath());
					Image img = MediaUtil.readImage(imgBytes);
					iip.setImage(img);
					setProgress(100);

					break;

				case 1:
					playTab.setMediaInfoPanel(aip);
					aip.setFileInfo(selectedFileInfo);
					setProgress(15);

					publish("Getting media meta data");
					mediaMeta = infoPlayer.getMediaMeta(selectedFileInfo.getPath());
					System.out.println("getMediaMeta:" + mediaMeta);
					aip.setMediaMeta(mediaMeta);
					setProgress(25);

					publish("Getting media information");
					length = infoPlayer.getLength(selectedFileInfo.getPath());
					System.out.println("getLength:" + length);
					aip.setDuration(length);
					setProgress(40);

					trackInfoList = infoPlayer.getTrackInfo(selectedFileInfo.getPath());
					System.out.println("getTrackInfo:" + trackInfoList);
					for (TrackInfo ti : trackInfoList)
					{
						if (ti instanceof AudioTrackInfo)
						{
							ati = (AudioTrackInfo) ti;
							break;
						}
					}
					if (ati != null)
					{
						aip.setAudioTrackInfo(ati);
					}
					setProgress(60);
					
					publish("Retrieving album cover from last.fm");
					Image albumCover = MediaUtil.getAlbumCoverFromLastFm(mediaMeta.getArtist(), mediaMeta.getAlbum());
					aip.setAlbumCover(albumCover);
					setProgress(100);

					break;

				case 2:
					playTab.setMediaInfoPanel(vip);
					vip.setFileInfo(selectedFileInfo);
					
					String movieTitle = MediaUtil.getTitleFromMovieFilename(selectedFileInfo.getName());
					vip.setTitle(movieTitle);
					setProgress(15);

					publish("Getting media information");
					length = infoPlayer.getLength(selectedFileInfo.getPath());
					System.out.println("getLength:" + length);
					vip.setDuration(length);
					setProgress(30);

					mediaDetails = infoPlayer.getMediaDetails(selectedFileInfo.getPath());
					System.out.println("getMediaDetails:" + mediaDetails);
					setProgress(60);

					trackInfoList = infoPlayer.getTrackInfo(selectedFileInfo.getPath());
					System.out.println("getTrackInfo:" + trackInfoList);
					setProgress(90);

					List<ExtendedAudioTrackInfo> eatis = new ArrayList<>();
					ExtendedVideoTrackInfo evti = null;
					for (TrackInfo ti : trackInfoList)
					{
						if (ti instanceof AudioTrackInfo)
						{
							String descr = getDescriptionForId(mediaDetails.getAudioDescriptions(), ti.id());
							eatis.add(new ExtendedAudioTrackInfo((AudioTrackInfo) ti, descr));
						}
						else if (ti instanceof VideoTrackInfo)
						{
							String descr = getDescriptionForId(mediaDetails.getVideoDescriptions(), ti.id());
							evti = new ExtendedVideoTrackInfo((VideoTrackInfo) ti, descr);
						}
					}
					if (evti != null)
					{
						vip.setExtendedVideoTrackInfo(evti);
					}
					vip.setExtendedAudioTrackInfo(eatis);
					
					publish("Retrieving movie cover from new-video.de");
					Image movieCover = MediaUtil.getMovieCoverFromNewVideo(movieTitle);
					vip.setMovieCover(movieCover);

					setProgress(100);

					break;

				default:
					playTab.setMediaInfoPanel(nmip);
					nmip.setFileInfo(selectedFileInfo);
					nmip.setMessage("unknown media");
			}
			playTab.validate();
		}

		private String getDescriptionForId(List<TrackDescription> tds, int id)
		{
			String descr = null;
			for (TrackDescription td : tds)
			{
				if (td.id() == id)
				{
					descr = td.description();
					break;
				}
			}
			return descr;
		}
	}

}
