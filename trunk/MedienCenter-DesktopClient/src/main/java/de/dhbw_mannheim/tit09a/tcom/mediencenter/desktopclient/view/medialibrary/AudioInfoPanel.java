package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;

import uk.co.caprica.vlcj.player.AudioTrackInfo;
import uk.co.caprica.vlcj.player.MediaMeta;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.BrowseUrlAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.PlayFileAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.ImageComponent;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NetUtil;

public class AudioInfoPanel extends MediaInfoPanel
{
	private static final long	serialVersionUID	= -196197480677069138L;

	private JPanel				leftPanel;
	private ImageComponent		albumCoverImgComp;
	private JButton				btnPlay;
	private JButton				btnPlayElapsedTime;
	private JLabel				lblElapsedTime;

	private JPanel				rightPanel;
	private JLabel				lblTitleKey;
	private JLabel				lblTitleValue;
	private JButton				btnSearchTitle;
	private JLabel				lblArtistKey;
	private JLabel				lblArtistValue;
	private JButton				btnSearchArtist;
	private JLabel				lblAlbumKey;
	private JLabel				lblAlbumValue;
	private JButton				btnSearchAlbum;
	private JLabel				lblTrackNumberKey;
	private JLabel				lblTrackNumberValue;
	private JLabel				lblGenreKey;
	private JLabel				lblGenreValue;
	private JLabel				lblDateKey;
	private JLabel				lblDateValue;
	private JLabel				lblDescriptionKey;
	private JLabel				lblDescriptionValue;

	private JLabel				lblDurationKey;
	private JLabel				lblDurationValue;
	private JLabel				lblCodecKey;
	private JLabel				lblCodecValue;
	private JLabel				lblSamplingRateKey;
	private JLabel				lblSamplingRateValue;
	private JLabel				lblChannelsKey;
	private JLabel				lblChannelsValue;

	private BrowseUrlAction		browseUrlAction;

	public AudioInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);

		initComponents();
	}

	public void setMediaMeta(MediaMeta mediaMeta)
	{
		if (mediaMeta == null)
			throw new IllegalArgumentException("mediaMeta == null");

		String title = mediaMeta.getTitle();
		String artist = mediaMeta.getArtist();
		String album = mediaMeta.getAlbum();
		lblTitleValue.setText(title);
		lblArtistValue.setText(artist);
		lblAlbumValue.setText(album);
		if (artist != null)
		{
			setSearchArtistBtn(artist);
			setSearchTitleBtn(artist, title);
			if (title != null)
				setSearchTitleBtn(artist, title);
			if (album != null)
				setSearchAlbumBtn(artist, album);
		}

		lblGenreValue.setText(mediaMeta.getGenre());
		lblDateValue.setText(mediaMeta.getDate());
		lblDescriptionValue.setText(mediaMeta.getDescription());
	}

	private void setSearchTitleBtn(String artist, String title)
	{
		if (artist == null || title == null)
			throw new IllegalArgumentException("artist and title cannot be null: artist= " + artist + ", title=" + title);

		String uriLastFmTitle = NetUtil.buildLastFmTitle(artist, title);
		btnSearchTitle.setToolTipText(uriLastFmTitle);
		btnSearchTitle.setActionCommand(uriLastFmTitle);
		btnSearchTitle.setEnabled(true);
	}

	private void setSearchArtistBtn(String artist)
	{
		if (artist == null)
			throw new IllegalArgumentException("artist cannot be null.");

		String uriLastFmArtist = NetUtil.buildLastFmArtist(artist);
		btnSearchArtist.setToolTipText(uriLastFmArtist);
		btnSearchArtist.setActionCommand(uriLastFmArtist);
		btnSearchArtist.setEnabled(true);
	}

	private void setSearchAlbumBtn(String artist, String album)
	{
		if (artist == null || album == null)
			throw new IllegalArgumentException("artist and album cannot be null: artist= " + artist + ", album=" + album);

		String uriLastFmAlbum = NetUtil.buildLastFmAlbum(artist, album);
		btnSearchAlbum.setToolTipText(uriLastFmAlbum);
		btnSearchAlbum.setActionCommand(uriLastFmAlbum);
		btnSearchAlbum.setEnabled(true);
	}

	public void setDuration(long duration)
	{
		lblDurationValue.setText(TimeValue.formatMillis(duration, true, true));
	}

	public void setAudioTrackInfo(AudioTrackInfo aip)
	{
		lblCodecValue.setText(aip.codecName());
		lblSamplingRateValue.setText(aip.rate() + " kHz");
		lblChannelsValue.setText(aip.channels() + "");
	}

	public void setAlbumCover(Image albumCover)
	{
		albumCoverImgComp.setImage(albumCover);
	}

	@Override
	public void setFileInfo(FileInfo fi)
	{
		super.setFileInfo(fi);
		String elapsedTime = TimeValue.formatMillis(fi.getElapsedTime(), true, true);
		lblElapsedTime.setText(elapsedTime);
	}

	@Override
	public void reset()
	{
		// LEFT
		albumCoverImgComp.setImage(null);
		lblElapsedTime.setText("--:--");

		// RIGHT meta
		lblTitleValue.setText(null);
		btnSearchTitle.setToolTipText(null);
		btnSearchTitle.setEnabled(false);
		lblArtistValue.setText(null);
		btnSearchArtist.setToolTipText(null);
		btnSearchArtist.setEnabled(false);
		lblAlbumValue.setText(null);
		btnSearchAlbum.setToolTipText(null);
		btnSearchAlbum.setEnabled(false);
		lblTrackNumberValue.setText(null);
		lblGenreValue.setText(null);
		lblDateValue.setText(null);
		lblDescriptionValue.setText(null);

		// RIGHT ati
		lblDurationValue.setText("--:--");
		lblCodecValue.setText(null);
		lblSamplingRateValue.setText("kHz");
		lblChannelsValue.setText(null);
	}

	private void initComponents()
	{
		createLeftPanel();
		createRightPanel();

		// Layout MAIN
		GroupLayout mainGrpLayout = new GroupLayout(this);
		this.setLayout(mainGrpLayout);
		mainGrpLayout.setHorizontalGroup(mainGrpLayout.createSequentialGroup().addComponent(leftPanel).addComponent(rightPanel));

		mainGrpLayout.setVerticalGroup(mainGrpLayout.createSequentialGroup().addGroup(mainGrpLayout.createParallelGroup()
				.addComponent(leftPanel)
				.addComponent(rightPanel)));
	}

	private JPanel createLeftPanel()
	{
		leftPanel = new JPanel();

		// img
		albumCoverImgComp = new ImageComponent(null);
		albumCoverImgComp.setDoScale(true);
		albumCoverImgComp.setOnlyScaleIfImgIsBigger(true);
		albumCoverImgComp.setPreferredSize(new Dimension(200, 200));
		albumCoverImgComp.setMaximumSize(new Dimension(200, 200));
		albumCoverImgComp.setMinimumSize(new Dimension(200, 200));

		// btns
		btnPlay = new JButton();
		btnPlay.setAction(new PlayFileAction(mainFrame, false));
		btnPlayElapsedTime = new JButton();
		btnPlayElapsedTime.setAction(new PlayFileAction(mainFrame, true));
		lblElapsedTime = new JLabel("--:--");

		// layout LEFT
		GroupLayout grpLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(grpLayout);
		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup().addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(albumCoverImgComp)
				.addGroup(grpLayout.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20)
						.addComponent(btnPlay)
						.addComponent(btnPlayElapsedTime)
						.addComponent(lblElapsedTime))));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addComponent(albumCoverImgComp)
				.addPreferredGap(ComponentPlacement.UNRELATED, 50, Short.MAX_VALUE)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(btnPlay)
						.addComponent(btnPlayElapsedTime)
						.addComponent(lblElapsedTime))
				.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20));

		return leftPanel;
	}

	private JPanel createRightPanel()
	{
		rightPanel = new JPanel();

		// action
		browseUrlAction = new BrowseUrlAction(mainFrame);

		// init layout
		GroupLayout grpLayout = new GroupLayout(rightPanel);
		rightPanel.setLayout(grpLayout);
		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		// mp3tags
		lblTitleKey = new JLabel("Title:");
		lblTitleValue = new JLabel();
		btnSearchTitle = new JButton(imgLastFm);
		btnSearchTitle.setBorder(null);
		btnSearchTitle.addActionListener(browseUrlAction);

		lblArtistKey = new JLabel("Artist:");
		lblArtistValue = new JLabel();
		btnSearchArtist = new JButton(imgLastFm);
		btnSearchArtist.setBorder(null);
		btnSearchArtist.addActionListener(browseUrlAction);

		lblAlbumKey = new JLabel("Album:");
		lblAlbumValue = new JLabel();
		btnSearchAlbum = new JButton(imgLastFm);
		btnSearchAlbum.setBorder(null);
		btnSearchAlbum.addActionListener(browseUrlAction);

		lblTrackNumberKey = new JLabel("Track:");
		lblTrackNumberValue = new JLabel();
		lblGenreKey = new JLabel("Genre:");
		lblGenreValue = new JLabel();
		lblDateKey = new JLabel("Date:");
		lblDateValue = new JLabel();
		lblDescriptionKey = new JLabel("Description:");
		lblDescriptionValue = new JLabel();

		// techinal data
		lblDurationKey = new JLabel("Length:");
		lblDurationValue = new JLabel("--:--");
		lblCodecKey = new JLabel("Codec name:");
		lblCodecValue = new JLabel();
		lblSamplingRateKey = new JLabel("Sampling rate:");
		lblSamplingRateValue = new JLabel("kHz");
		lblChannelsKey = new JLabel("Channels:");
		lblChannelsValue = new JLabel();

		// Layout RIGHT
		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup()
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblTitleKey)
						.addComponent(lblArtistKey)
						.addComponent(lblAlbumKey)
						.addComponent(lblTrackNumberKey)
						.addComponent(lblGenreKey)
						.addComponent(lblDateKey)
						.addComponent(lblDescriptionKey)
						.addComponent(lblDurationKey)
						.addComponent(lblCodecKey)
						.addComponent(lblSamplingRateKey)
						.addComponent(lblChannelsKey))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblTitleValue)
						.addComponent(lblArtistValue)
						.addComponent(lblAlbumValue)
						.addComponent(lblTrackNumberValue)
						.addComponent(lblGenreValue)
						.addComponent(lblDateValue)
						.addComponent(lblDescriptionValue)
						.addComponent(lblDurationValue)
						.addComponent(lblCodecValue)
						.addComponent(lblSamplingRateValue)
						.addComponent(lblChannelsValue))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(btnSearchTitle)
						.addComponent(btnSearchArtist)
						.addComponent(btnSearchAlbum)));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTitleKey)
						.addComponent(lblTitleValue)
						.addComponent(btnSearchTitle))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblArtistKey)
						.addComponent(lblArtistValue)
						.addComponent(btnSearchArtist))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblAlbumKey)
						.addComponent(lblAlbumValue)
						.addComponent(btnSearchAlbum))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTrackNumberKey)
						.addComponent(lblTrackNumberValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGenreKey).addComponent(lblGenreValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDateKey).addComponent(lblDateValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDescriptionKey)
						.addComponent(lblDescriptionValue))
				.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDurationKey).addComponent(lblDurationValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCodecKey).addComponent(lblCodecValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblSamplingRateKey)
						.addComponent(lblSamplingRateValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblChannelsKey).addComponent(lblChannelsValue)));

		return rightPanel;
	}

}
