package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.BrowseUrlAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.PlayFileAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.media.ExtendedAudioTrackInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.media.ExtendedVideoTrackInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.ImageComponent;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NetUtil;

public class VideoInfoPanel extends MediaInfoPanel
{
	private static final long	serialVersionUID	= 891239762411423316L;

	private JPanel				leftPanel;
	private ImageComponent		coverImgComp;
	private JButton				btnPlay;
	private JButton				btnPlayElapsedTime;
	private JLabel				lblElapsedTime;

	private JPanel				rightPanelVideo;
	private JLabel				lblTitleKey;
	private JTextField			txtFldTitleValue;
	private JButton				btnSearchNewVideo;
	private JButton				btnSearchImdb;
	private JButton				btnSearchFilmstarts;
	private JLabel				lblDurationKey;
	private JLabel				lblDurationValue;
	private JLabel				lblVideoCodecKey;
	private JLabel				lblVideoCodecValue;
	private JLabel				lblDimensionsKey;
	private JLabel				lblDimensionsValue;
	private JLabel				lblVideoTrackDescriptionKey;
	private JLabel				lblVideoTrackDescriptionValue;

	private JPanel				rightPanelAudio;

	public VideoInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);

		initComponents();
	}

	@Override
	public void reset()
	{
		System.out.println("Resetting " + this);
		// LEFT
		coverImgComp.setImage(null);
		lblElapsedTime.setText("--:--");

		// RIGHT video
		lblVideoTrackDescriptionValue.setText(null);
		lblVideoCodecValue.setText(null);
		lblDimensionsValue.setText(null);

		// RIGHT audio
		LayoutManager lm = rightPanelAudio.getLayout();
		if (lm instanceof GroupLayout)
			((GroupLayout) lm).invalidateLayout(rightPanelAudio);
		rightPanelAudio.removeAll();
	}

	@Override
	public void setFileInfo(FileInfo fi)
	{
		super.setFileInfo(fi);
		String elapsedTime = TimeValue.formatMillis(fi.getElapsedTime(), true, true);
		lblElapsedTime.setText(elapsedTime);
	}

	public void setTitle(String title)
	{
		txtFldTitleValue.setText(title);
	}

	public void setDuration(long duration)
	{
		lblDurationValue.setText(TimeValue.formatMillis(duration, true, true));
	}

	public void setExtendedVideoTrackInfo(ExtendedVideoTrackInfo evti)
	{
		System.out.println("Setting EVTI: " + evti);
		lblVideoTrackDescriptionValue.setText(evti.getDescription());
		lblVideoCodecValue.setText(evti.codecName());
		lblDimensionsValue.setText(evti.width() + "x" + evti.height());
	}

	public void setExtendedAudioTrackInfo(List<ExtendedAudioTrackInfo> eatis)
	{
		// layout RIGHT AUDIO
		GroupLayout grpLayout = new GroupLayout(rightPanelAudio);
		rightPanelAudio.setLayout(grpLayout);
		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		// TODO fix the layout so that the description has colspan=2
		ParallelGroup horParallel1 = grpLayout.createParallelGroup(Alignment.LEADING);
		ParallelGroup horParallel2 = grpLayout.createParallelGroup(Alignment.LEADING);
		SequentialGroup verSequential1 = grpLayout.createSequentialGroup();

		for (ExtendedAudioTrackInfo eati : eatis)
		{
			JLabel lblAudioTrack = new JLabel("#" + eati.id() + " " + eati.getDescription());

			JLabel lblAudioCodecKey = new JLabel("Codec:");
			JLabel lblAudioCodecValue = new JLabel(eati.codecName());
			JLabel lblSamplingRateKey = new JLabel("Sampling rate:");
			JLabel lblSamplingRateValue = new JLabel(eati.rate() + " kHz");
			JLabel lblChannelsKey = new JLabel("Channels:");
			JLabel lblChannelsValue = new JLabel(eati.channels() + "");

			// horizontal
			horParallel1.addComponent(lblAudioTrack);
			horParallel1.addComponent(lblAudioCodecKey);
			horParallel1.addComponent(lblSamplingRateKey);
			horParallel1.addComponent(lblChannelsKey);

			horParallel2.addComponent(lblAudioCodecValue);
			horParallel2.addComponent(lblSamplingRateValue);
			horParallel2.addComponent(lblChannelsValue);

			// vertical
			verSequential1.addComponent(lblAudioTrack)
					.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lblAudioCodecKey)
							.addComponent(lblAudioCodecValue))
					.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lblSamplingRateKey)
							.addComponent(lblSamplingRateValue))
					.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lblChannelsKey)
							.addComponent(lblChannelsValue))
					.addPreferredGap(ComponentPlacement.UNRELATED);
		}
		// LAYOUT
		/*
		 * .add(textField) .add(layout.createSequentialGroup() .add(layout.createParallelGroup(GroupLayout.LEADING) .add(caseCheckBox)
		 * .add(wholeCheckBox)) .add(layout.createParallelGroup(GroupLayout.LEADING) .add(wrapCheckBox) .add(backCheckBox))))
		 */
		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup()
				.addGroup(horParallel1)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(horParallel2));

		grpLayout.setVerticalGroup(verSequential1);
	}

	public void setMovieCover(Image cover)
	{
		coverImgComp.setImage(cover);
	}

	private void initComponents()
	{
		createLeftPanel();
		createRightPanelVideo();
		createRightPanelAudio();

		// Layout MAIN
		/*
		 * L R R
		 */
		GroupLayout grpLayout = new GroupLayout(this);
		this.setLayout(grpLayout);

		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup()
				.addComponent(leftPanel)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(rightPanelVideo).addComponent(rightPanelAudio)));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup().addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(leftPanel)
				.addGroup(grpLayout.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20) // gap to top
						.addComponent(rightPanelVideo)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(rightPanelAudio))));
	}

	private void setSearchNewVideo(String movie)
	{
		if (movie == null)
			throw new IllegalArgumentException("movie cannot be null.");

		String uriNewVideo = NetUtil.buildNewVideo(movie);
		btnSearchNewVideo.setToolTipText(uriNewVideo);
		btnSearchNewVideo.setActionCommand(uriNewVideo);
		btnSearchNewVideo.setEnabled(true);
	}

	private void setSearchImdb(String movie)
	{
		if (movie == null)
			throw new IllegalArgumentException("movie cannot be null.");

		String uriNewVideo = NetUtil.buildImdbSearch(movie);
		btnSearchImdb.setToolTipText(uriNewVideo);
		btnSearchImdb.setActionCommand(uriNewVideo);
		btnSearchImdb.setEnabled(true);
	}

	private void setSearchFilmstarts(String movie)
	{
		if (movie == null)
			throw new IllegalArgumentException("movie cannot be null.");

		String uriNewVideo = NetUtil.buildFilmstartsSearch(movie);
		btnSearchFilmstarts.setToolTipText(uriNewVideo);
		btnSearchFilmstarts.setActionCommand(uriNewVideo);
		btnSearchFilmstarts.setEnabled(true);
	}

	private JPanel createLeftPanel()
	{
		leftPanel = new JPanel();
		// leftPanel.setBackground(Color.YELLOW);
		// img
		coverImgComp = new ImageComponent(null);
		coverImgComp.setDoScale(true);
		coverImgComp.setOnlyScaleIfImgIsBigger(true);
		// Verhältnis 1: wurzel(2)
		coverImgComp.setPreferredSize(new Dimension(200, 283));
		coverImgComp.setMaximumSize(new Dimension(200, 283));
		coverImgComp.setMinimumSize(new Dimension(200, 283));

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
				.addComponent(coverImgComp)
				.addGroup(grpLayout.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20)
						.addComponent(btnPlay)
						.addComponent(btnPlayElapsedTime)
						.addComponent(lblElapsedTime))));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addComponent(coverImgComp)
				.addPreferredGap(ComponentPlacement.UNRELATED, 50, Short.MAX_VALUE)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(btnPlay)
						.addComponent(btnPlayElapsedTime)
						.addComponent(lblElapsedTime))
				.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20));

		return leftPanel;

	}

	private JPanel createRightPanelVideo()
	{
		rightPanelVideo = new JPanel();
		// rightPanelVideo.setBackground(Color.RED);
		rightPanelVideo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><b>Video</b></html>"));

		BrowseUrlAction browseUrlAction = new BrowseUrlAction(mainFrame);

		lblTitleKey = new JLabel("Title:");
		txtFldTitleValue = new JTextField();
		txtFldTitleValue.getDocument().addDocumentListener(new DocumentListener()
		{
			private void updateBtns()
			{
				setSearchNewVideo(txtFldTitleValue.getText());
				setSearchImdb(txtFldTitleValue.getText());
				setSearchFilmstarts(txtFldTitleValue.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				updateBtns();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateBtns();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				updateBtns();
			}
		});

		btnSearchNewVideo = new JButton(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "new-video.png"));
		btnSearchNewVideo.setBorder(null);
		btnSearchNewVideo.addActionListener(browseUrlAction);

		btnSearchImdb = new JButton(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "imdb.png"));
		btnSearchImdb.setBorder(null);
		btnSearchImdb.addActionListener(browseUrlAction);

		btnSearchFilmstarts = new JButton(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "filmstarts.png"));
		btnSearchFilmstarts.setBorder(null);
		btnSearchFilmstarts.addActionListener(browseUrlAction);

		lblVideoCodecKey = new JLabel("Codec:");
		lblVideoCodecValue = new JLabel();
		lblDurationKey = new JLabel("Duration:");
		lblDurationValue = new JLabel();
		lblDimensionsKey = new JLabel("Dimensions:");
		lblDimensionsValue = new JLabel();
		lblVideoTrackDescriptionKey = new JLabel("Description:");
		lblVideoTrackDescriptionValue = new JLabel();

		// Layout RIGHT VIDEO
		GroupLayout grpLayout = new GroupLayout(rightPanelVideo);
		rightPanelVideo.setLayout(grpLayout);
		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup()
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblTitleKey)
						.addComponent(lblDurationKey)
						.addComponent(lblVideoCodecKey)
						.addComponent(lblDimensionsKey)
						.addComponent(lblVideoTrackDescriptionKey))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(txtFldTitleValue)
						.addComponent(lblVideoCodecValue)
						.addComponent(lblDurationValue)
						.addComponent(lblDimensionsValue)
						.addComponent(lblVideoTrackDescriptionValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(grpLayout.createSequentialGroup()
						.addComponent(btnSearchNewVideo)
						.addComponent(btnSearchImdb)
						.addComponent(btnSearchFilmstarts))));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTitleKey)
						.addComponent(txtFldTitleValue)
						.addComponent(btnSearchNewVideo)
						.addComponent(btnSearchImdb)
						.addComponent(btnSearchFilmstarts))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDurationKey).addComponent(lblDurationValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblVideoCodecKey)
						.addComponent(lblVideoCodecValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDimensionsKey)
						.addComponent(lblDimensionsValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblVideoTrackDescriptionKey)
						.addComponent(lblVideoTrackDescriptionValue)));

		return rightPanelVideo;
	}

	private JPanel createRightPanelAudio()
	{
		rightPanelAudio = new JPanel();
		// rightPanelAudio.setBackground(Color.BLUE);
		rightPanelAudio.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><b>Audio</b></html>"));

		return rightPanelAudio;
	}
}
