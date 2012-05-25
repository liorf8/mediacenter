package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;

public class MediaToolBar extends JToolBar implements ActionListener, ChangeListener
{
	private static final long	serialVersionUID	= 7584348932344982696L;

	private JPanel				progressPanel;
	private TimeLabel			timeLabel;
	private JSlider				sldrProgress;

	private JPanel				buttonsPanel;
	private JLabel				lblNowPlaying;
	private JButton				btnRandom;
	private JButton				btnRepeat;
	private JButton				btnStop;
	private JButton				btnPrevious;
	private JButton				btnRewind;
	private JButton				btnPlayPause;
	private JButton				btnFastForward;
	private JButton				btnNext;

	private JButton				btnMute;
	private JSlider				sldrVolume;
	private JLabel				lblVolume;

	private ImageIcon			imgRandom;
	private ImageIcon			imgRepeat;
	private ImageIcon			imgStop;
	private ImageIcon			imgPrevious;
	private ImageIcon			imgRewind;
	private ImageIcon			imgPlay;
	private ImageIcon			imgPause;
	private ImageIcon			imgFastForward;
	private ImageIcon			imgNext;

	private ImageIcon			imgMute;
	private ImageIcon			imgVolume0;
	private ImageIcon			imgVolume1;
	private ImageIcon			imgVolume2;
	private ImageIcon			imgVolume3;
	private ImageIcon			imgVolume4;
	private ImageIcon			imgVolume5;

	public MediaToolBar()
	{
		setName("MediaScrub - ToolBar");

		loadImages();

		setLayout(new GridLayout(2, 1));
		this.add(createProgressPanel());
		this.add(createButtonsPanel());
	}

	private void loadImages()
	{
		imgRandom = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Random.png");
		imgRepeat = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Repeat.png");

		imgStop = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Stop.png");
		imgPrevious = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Previous.png");
		imgRewind = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Backward.png");
		imgPlay = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Play.png");
		imgPause = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Pause.png");
		imgFastForward = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Forward.png");
		imgNext = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_22x22 + "Next.png");

		imgMute = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Mute.png");
		imgVolume0 = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 0.png");
		imgVolume1 = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 1.png");
		imgVolume2 = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 2.png");
		imgVolume3 = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 3.png");
		imgVolume4 = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 4.png");
		imgVolume5 = MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 5.png");
	}

	private JPanel createProgressPanel()
	{
		if (progressPanel == null)
		{
			progressPanel = new JPanel();

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
			GroupLayout groupLayout = new GroupLayout(progressPanel);
			groupLayout.setHonorsVisibility(false);
			groupLayout.setAutoCreateGaps(true);
			groupLayout.setAutoCreateContainerGaps(false);
			progressPanel.setLayout(groupLayout);

			groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 5, 5)
					.addComponent(lblNowPlaying, 100, GroupLayout.PREFERRED_SIZE, 200)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 5, 5)
					.addComponent(sldrProgress)
					.addComponent(timeLabel)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 5, 5));

			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lblNowPlaying)
					.addComponent(sldrProgress)
					.addComponent(timeLabel)));
		}
		return progressPanel;
	}

	private JPanel createButtonsPanel()
	{
		if (buttonsPanel == null)
		{
			buttonsPanel = new JPanel();

			btnRandom = new JButton();
			btnRandom.setIcon(imgRandom);

			btnRepeat = new JButton();
			btnRepeat.setIcon(imgRepeat);

			btnStop = new JButton();
			btnStop.setIcon(imgStop);

			btnPrevious = new JButton();
			btnPrevious.setIcon(imgPrevious);

			btnRewind = new JButton();
			btnRewind.setIcon(imgRewind);

			btnPlayPause = new JButton();
			btnPlayPause.setIcon(imgPlay);

			btnFastForward = new JButton();
			btnFastForward.setIcon(imgFastForward);

			btnNext = new JButton();
			btnNext.setIcon(imgNext);

			btnMute = new JButton();
			btnMute.setIcon(imgMute);
			btnMute.setActionCommand("mute");
			btnMute.addActionListener(this);

			sldrVolume = new JSlider(0, 100);
			sldrVolume.addChangeListener(this);

			lblVolume = new JLabel();
			displayVolume(50);

			// Layout
			GroupLayout grpLayout = new GroupLayout(buttonsPanel);
			grpLayout.setHonorsVisibility(false);
			grpLayout.setAutoCreateGaps(true);
			grpLayout.setAutoCreateContainerGaps(false);
			buttonsPanel.setLayout(grpLayout);

			grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup()
					.addComponent(btnRandom)
					.addComponent(btnRepeat)
					.addPreferredGap(ComponentPlacement.UNRELATED, 30, 30)
					.addComponent(btnStop)
					.addComponent(btnPrevious)
					.addComponent(btnRewind)
					.addComponent(btnPlayPause)
					.addComponent(btnFastForward)
					.addComponent(btnNext)
					.addPreferredGap(ComponentPlacement.UNRELATED, 50, Short.MAX_VALUE)
					.addComponent(btnMute)
					.addComponent(sldrVolume, 100, 100, 100)
					.addComponent(lblVolume, 40, 40, 40));

			grpLayout.setVerticalGroup(grpLayout.createSequentialGroup().addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(btnRandom)
					.addComponent(btnRepeat)
					.addComponent(btnStop)
					.addComponent(btnPrevious)
					.addComponent(btnRewind)
					.addComponent(btnPlayPause)
					.addComponent(btnFastForward)
					.addComponent(btnNext)
					.addComponent(btnMute)
					.addComponent(sldrVolume)
					.addComponent(lblVolume)));
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
		if (value < 17)
		{
			lblVolume.setIcon(imgVolume0);
		}
		else if (value < 34)
		{
			lblVolume.setIcon(imgVolume1);
		}
		else if (value < 50)
		{
			lblVolume.setIcon(imgVolume2);
		}
		else if (value < 67)
		{
			lblVolume.setIcon(imgVolume3);
		}
		else if (value < 84)
		{
			lblVolume.setIcon(imgVolume4);
		}
		else
		{
			lblVolume.setIcon(imgVolume5);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object src = e.getSource();
		if (src.equals(sldrVolume))
		{
			displayVolume(((JSlider) src).getValue());
			// TODO: adapt volume on modell
		}
	}

}
