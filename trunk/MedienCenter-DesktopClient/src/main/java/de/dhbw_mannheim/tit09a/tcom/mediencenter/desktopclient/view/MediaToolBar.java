package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MiscUtil;

public class MediaToolBar extends JToolBar implements ActionListener, ChangeListener
{
	private static final long	serialVersionUID	= 7584348932344982696L;

	private JPanel				progressPanel;
	private TimeLabel			timeLabel;
	private JSlider				sldrProgress;

	private JPanel				buttonsPanel;
	private JLabel				lblNowPlaying;
	private JButton				btnShuffle;
	private JButton				btnRepeat;
	private JButton				btnStop;
	private JButton				btnPrevious;
	private JButton				btnRewind;
	private JButton				btnPlay;
	private JButton				btnFastForward;
	private JButton				btnNext;
	private JButton				btnMute;
	private JSlider				sldrVolume;
	private JLabel				lblVolume;

	private ImageIcon			imgVolumeLow;
	private ImageIcon			imgVolumeMedium;
	private ImageIcon			imgVolumeHigh;

	public MediaToolBar()
	{
		setName("MediaScrub - ToolBar");
		setLayout(new GridLayout(2, 1));

		loadImages();
		this.add(createProgressPanel());
		this.add(createButtonsPanel());
	}

	private JPanel createProgressPanel()
	{
		if (progressPanel == null)
		{
			progressPanel = new JPanel();
			GroupLayout groupLayout = new GroupLayout(progressPanel);
			progressPanel.setLayout(groupLayout);

			groupLayout.setHonorsVisibility(false);
			groupLayout.setAutoCreateGaps(true);
			groupLayout.setAutoCreateContainerGaps(false);

			lblNowPlaying = new JLabel("Now Playing");

			sldrProgress = new JSlider(0, 100, 0);
			sldrProgress.setPreferredSize(new Dimension(500, sldrProgress.getPreferredSize().height));
			sldrProgress.setPaintTrack(true); // the area the slider slides in
			sldrProgress.setPaintTicks(true);
			sldrProgress.setMajorTickSpacing(50);
			sldrProgress.setMinorTickSpacing(25);
			sldrProgress.setPaintLabels(false);

			timeLabel = new TimeLabel();

			// Layout
			groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 5, 5)
					.addComponent(lblNowPlaying, 100, GroupLayout.PREFERRED_SIZE, 150)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 5, 5)
					.addComponent(sldrProgress)
					.addComponent(timeLabel)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 5, 5));

			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblNowPlaying, GroupLayout.Alignment.CENTER)
					.addComponent(sldrProgress)
					.addComponent(timeLabel, GroupLayout.Alignment.CENTER)));
		}
		return progressPanel;
	}

	private void loadImages()
	{
		String imgPath16x16 = "/imgs/16x16/";
		InputStream is = getClass().getResourceAsStream( imgPath16x16 + "audio-volume-low.png" );
		System.out.println(is);
		imgVolumeLow = MiscUtil.createImageIcon(imgPath16x16 + "audio-volume-low.png", "audio-volume-low.png");
		imgVolumeMedium = MiscUtil.createImageIcon(imgPath16x16 + "audio-volume-medium.png", "audio-volume-medium.png");
		imgVolumeHigh = MiscUtil.createImageIcon(imgPath16x16 + "audio-volume-high.png", "audio-volume-high.png");
	}

	private JPanel createButtonsPanel()
	{
		if (buttonsPanel == null)
		{
			ImageIcon icon;
			buttonsPanel = new JPanel();

			btnShuffle = new JButton();
			btnShuffle.setText("shuffle");

			btnRepeat = new JButton();
			btnRepeat.setText("repeat");

			btnStop = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/22x22/media-playback-stop.png", "media-playback-stop.png");
			btnStop.setIcon(icon);

			btnPrevious = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/22x22/media-skip-backward.png", "media-skip-backward.png");
			btnPrevious.setIcon(icon);

			btnRewind = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/22x22/media-seek-backward.png", "media-seek-backward.png");
			btnRewind.setIcon(icon);

			btnPlay = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/22x22/media-playback-start.png", "media-playback-start.png");
			btnPlay.setIcon(icon);

			btnFastForward = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/22x22/media-seek-forward.png", "media-seek-forward.png");
			btnFastForward.setIcon(icon);

			btnNext = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/22x22/media-skip-forward.png", "media-skip-forward.png");
			btnNext.setIcon(icon);

			btnMute = new JButton();
			icon = MiscUtil.createImageIcon("/imgs/16x16/audio-volume-muted.png", "audio-volume-muted.png");
			btnMute.setIcon(icon);
			btnMute.setActionCommand("mute");
			btnMute.addActionListener(this);

			sldrVolume = new JSlider(0, 100);
			Dimension sldrVolumeDim = new Dimension(100, sldrVolume.getPreferredSize().height);
			sldrVolume.setPreferredSize(sldrVolumeDim);
			sldrVolume.setMinimumSize(sldrVolumeDim);
			sldrVolume.setMaximumSize(sldrVolumeDim);
			sldrVolume.addChangeListener(this);

			lblVolume = new JLabel();
			displayVolume(50);

			buttonsPanel.add(lblNowPlaying);
			buttonsPanel.add(btnShuffle);
			buttonsPanel.add(btnRepeat);
			buttonsPanel.add(btnStop);
			buttonsPanel.add(btnPrevious);
			buttonsPanel.add(btnRewind);
			buttonsPanel.add(btnPlay);
			buttonsPanel.add(btnFastForward);
			buttonsPanel.add(btnNext);
			buttonsPanel.add(btnMute);
			buttonsPanel.add(sldrVolume);
			buttonsPanel.add(lblVolume);
		}
		return buttonsPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if ("maxVolume".equals(e.getActionCommand()))
		{
			sldrVolume.setValue(100);
		}
		else if ("mute".equals(e.getActionCommand()))
		{
			sldrVolume.setValue(0);
		}
	}

	private void displayVolume(int value)
	{
		lblVolume.setText(value + "");
		if (value < 34)
		{
			lblVolume.setIcon(imgVolumeLow);
		}
		else if (value >= 67)
		{
			lblVolume.setIcon(imgVolumeHigh);
		}
		else
		{
			lblVolume.setIcon(imgVolumeMedium);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object src = e.getSource();
		if (src.equals(sldrVolume))
		{
			displayVolume(((JSlider) src).getValue());
		}
	}

}
