package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

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

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.BrowseUrlAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.InternetUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.ImageComponent;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;

public class AudioInfoPanel extends MediaInfoPanel
{
	private static final long	serialVersionUID	= -196197480677069138L;

	private JPanel				leftPanel;
	private GroupLayout			leftGrpLayout;
	private ImageComponent		albumCoverImgComp;
	private JButton				btnPlay;
	private JButton				btnPlayFromPosition;
	private JLabel				lblElapsedTime;

	private JPanel				rightPanel;
	private GroupLayout			rightGrpLayout;
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

	private JLabel				lblLengthKey;
	private JLabel				lblLengthValue;
	private JLabel				lblCodecNameKey;
	private JLabel				lblCodecNameValue;
	private JLabel				lblRateKey;
	private JLabel				lblRateValue;
	private JLabel				lblChannelsKey;
	private JLabel				lblChannelsValue;

	private BrowseUrlAction		browseUrlAction;

	public AudioInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);

		initComponents();

		applyLayout();
	}

	public void setMediaMeta(MediaMeta mediaMeta)
	{
		if(mediaMeta != null)
		{
			lblTitleValue.setText(mediaMeta.getTitle());
			String uriLastFmTitle = InternetUtil.buildLastFmTitle(mediaMeta.getArtist(), mediaMeta.getTitle());
			btnSearchTitle.setToolTipText(uriLastFmTitle);
			btnSearchTitle.setActionCommand(uriLastFmTitle);

			lblArtistValue.setText(mediaMeta.getArtist());
			String uriLastFmArtist = InternetUtil.buildLastFmArtist(mediaMeta.getArtist());
			btnSearchArtist.setToolTipText(uriLastFmArtist);
			btnSearchArtist.setActionCommand(uriLastFmArtist);

			lblAlbumValue.setText(mediaMeta.getAlbum());
			String uriLastFmAlbum = InternetUtil.buildLastFmAlbum(mediaMeta.getArtist(), mediaMeta.getAlbum());
			btnSearchAlbum.setToolTipText(uriLastFmAlbum);
			btnSearchAlbum.setActionCommand(uriLastFmAlbum);

			lblTrackNumberValue.setText(mediaMeta.getTrackNumber());
			lblGenreValue.setText(mediaMeta.getGenre());
			lblDateValue.setText(mediaMeta.getDate());
			lblDescriptionValue.setText(mediaMeta.getDescription());
		}
	}

	public void setAlbumCover(Image albumCover)
	{
		albumCoverImgComp.setImage(albumCover);
	}

	public void setTechnicals(long length, AudioTrackInfo ati)
	{
		String duration = TimeValue.formatMillis(length, true, true);
		lblLengthValue.setText(duration);

		if (ati != null)
		{
			lblCodecNameValue.setText(ati.codecName());
			lblRateValue.setText(ati.rate() + " kHz");
			lblChannelsValue.setText(ati.channels() + "");
		}
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
		// meta
		lblTitleValue.setText("");
		btnSearchTitle.setToolTipText("");
		lblArtistValue.setText("");
		btnSearchArtist.setToolTipText("");
		lblAlbumValue.setText("");
		btnSearchAlbum.setToolTipText("");
		lblTrackNumberValue.setText("");
		lblGenreValue.setText("");
		lblDateValue.setText("");
		lblDescriptionValue.setText("");

		// img
		albumCoverImgComp.setImage(null);

		// technicals
		lblLengthValue.setText("--:--");
		lblCodecNameValue.setText("");
		lblRateValue.setText("kHz");
		lblChannelsValue.setText("");
	}

	private void initComponents()
	{
		leftPanel = createLeftPanel();
		rightPanel = createRightPanel();
	}

	private JPanel createLeftPanel()
	{
		leftPanel = new JPanel();

		// init layout
		leftGrpLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(leftGrpLayout);
		leftGrpLayout.setHonorsVisibility(false);
		leftGrpLayout.setAutoCreateGaps(true);
		leftGrpLayout.setAutoCreateContainerGaps(true);

		// img
		albumCoverImgComp = new ImageComponent(null);
		albumCoverImgComp.setDoScale(true);
		albumCoverImgComp.setOnlyScaleIfImgIsBigger(true);
		albumCoverImgComp.setPreferredSize(new Dimension(200, 200));
		albumCoverImgComp.setMaximumSize(new Dimension(200, 200));
		albumCoverImgComp.setMinimumSize(new Dimension(200, 200));

		// btns
		btnPlay = new JButton("Play", MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Play.png"));
		btnPlayFromPosition = new JButton("Play from", MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Play from saved.png"));
		lblElapsedTime = new JLabel("--:--:--");

		return leftPanel;
	}

	private JPanel createRightPanel()
	{
		rightPanel = new JPanel();

		// action
		browseUrlAction = new BrowseUrlAction(mainFrame);

		// init layout
		rightGrpLayout = new GroupLayout(rightPanel);
		rightPanel.setLayout(rightGrpLayout);
		rightGrpLayout.setHonorsVisibility(false);
		rightGrpLayout.setAutoCreateGaps(true);
		rightGrpLayout.setAutoCreateContainerGaps(true);

		// mp3tags
		lblTitleKey = new JLabel("Title:");
		lblTitleValue = new JLabel("-");
		btnSearchTitle = new JButton(imgLastFm);
		btnSearchTitle.setBorder(null);
		btnSearchTitle.addActionListener(browseUrlAction);

		lblArtistKey = new JLabel("Artist:");
		lblArtistValue = new JLabel("-");
		btnSearchArtist = new JButton(imgLastFm);
		btnSearchArtist.setBorder(null);
		btnSearchArtist.addActionListener(browseUrlAction);

		lblAlbumKey = new JLabel("Album:");
		lblAlbumValue = new JLabel("-");
		btnSearchAlbum = new JButton(imgLastFm);
		btnSearchAlbum.setBorder(null);
		btnSearchAlbum.addActionListener(browseUrlAction);

		lblTrackNumberKey = new JLabel("Track:");
		lblTrackNumberValue = new JLabel("-");
		lblGenreKey = new JLabel("Genre:");
		lblGenreValue = new JLabel("-");
		lblDateKey = new JLabel("Date:");
		lblDateValue = new JLabel("-");
		lblDescriptionKey = new JLabel("Description:");
		lblDescriptionValue = new JLabel("-");

		// techinal data
		lblLengthKey = new JLabel("Length:");
		lblLengthValue = new JLabel("--:--");
		lblCodecNameKey = new JLabel("Codec name:");
		lblCodecNameValue = new JLabel("-");
		lblRateKey = new JLabel("Sampling rate:");
		lblRateValue = new JLabel("- kHz");
		lblChannelsKey = new JLabel("Channels:");
		lblChannelsValue = new JLabel("-");

		return rightPanel;
	}

	private void applyLayout()
	{
		// Layout MAIN
		GroupLayout mainGrpLayout = new GroupLayout(this);
		this.setLayout(mainGrpLayout);
		mainGrpLayout.setHorizontalGroup(mainGrpLayout.createSequentialGroup().addComponent(leftPanel).addComponent(rightPanel));

		mainGrpLayout.setVerticalGroup(mainGrpLayout.createSequentialGroup().addGroup(mainGrpLayout.createParallelGroup()
				.addComponent(leftPanel)
				.addComponent(rightPanel)));

		// layout LEFT
		leftGrpLayout.setHorizontalGroup(leftGrpLayout.createSequentialGroup()
				.addGroup(leftGrpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(albumCoverImgComp)
						.addGroup(leftGrpLayout.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20)
								.addComponent(btnPlay)
								.addComponent(btnPlayFromPosition)
								.addComponent(lblElapsedTime))));

		leftGrpLayout.setVerticalGroup(leftGrpLayout.createSequentialGroup()
				.addComponent(albumCoverImgComp)
				.addPreferredGap(ComponentPlacement.UNRELATED, 50, Short.MAX_VALUE)
				.addGroup(leftGrpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(btnPlay)
						.addComponent(btnPlayFromPosition)
						.addComponent(lblElapsedTime))
				.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20));

		// Layout RIGHT
		rightGrpLayout.setHorizontalGroup(rightGrpLayout.createSequentialGroup()
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblTitleKey)
						.addComponent(lblArtistKey)
						.addComponent(lblAlbumKey)
						.addComponent(lblTrackNumberKey)
						.addComponent(lblGenreKey)
						.addComponent(lblDateKey)
						.addComponent(lblDescriptionKey)
						.addComponent(lblLengthKey)
						.addComponent(lblCodecNameKey)
						.addComponent(lblRateKey)
						.addComponent(lblChannelsKey))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblTitleValue)
						.addComponent(lblArtistValue)
						.addComponent(lblAlbumValue)
						.addComponent(lblTrackNumberValue)
						.addComponent(lblGenreValue)
						.addComponent(lblDateValue)
						.addComponent(lblDescriptionValue)
						.addComponent(lblLengthValue)
						.addComponent(lblCodecNameValue)
						.addComponent(lblRateValue)
						.addComponent(lblChannelsValue))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(btnSearchTitle)
						.addComponent(btnSearchArtist)
						.addComponent(btnSearchAlbum)));

		rightGrpLayout.setVerticalGroup(rightGrpLayout.createSequentialGroup()
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTitleKey)
						.addComponent(lblTitleValue)
						.addComponent(btnSearchTitle))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblArtistKey)
						.addComponent(lblArtistValue)
						.addComponent(btnSearchArtist))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblAlbumKey)
						.addComponent(lblAlbumValue)
						.addComponent(btnSearchAlbum))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTrackNumberKey)
						.addComponent(lblTrackNumberValue))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGenreKey).addComponent(lblGenreValue))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDateKey).addComponent(lblDateValue))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDescriptionKey)
						.addComponent(lblDescriptionValue))
				.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20)
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblLengthKey).addComponent(lblLengthValue))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblCodecNameKey)
						.addComponent(lblCodecNameValue))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblRateKey).addComponent(lblRateValue))
				.addGroup(rightGrpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblChannelsKey)
						.addComponent(lblChannelsValue)));
	}

}
