package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.ContinuePlayAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.PauseAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.StopAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class MediaToolBar extends JToolBar implements ActionListener, ChangeListener
{
	private static final long	serialVersionUID	= 7584348932344982696L;

	private MainFrame			mainFrame;

	private JPanel				progressPanel;
	private TimeLabel			timeLabel;
	private JSlider				sldrProgress;
	private boolean				progressChangedByModel;

	private JPanel				buttonsPanel;
	private JLabel				lblNowPlaying;
	private JButton				btnRandom;
	private JButton				btnRepeat;
	private JButton				btnStop;
	private JButton				btnPrevious;
	private JButton				btnRewind;
	private JButton				btnPlayPause;
	private Action				playAction;
	private Action				pauseAction;
	private JButton				btnFastForward;
	private JButton				btnNext;

	private JToggleButton		btnToggleFullScreen;

	private JButton				btnMute;
	private JSlider				sldrVolume;
	private JLabel				lblVolume;

	private ImageIcon			imgRandom;
	private ImageIcon			imgRepeat;
	private ImageIcon			imgPrevious;
	private ImageIcon			imgRewind;
	private ImageIcon			imgFastForward;
	private ImageIcon			imgNext;

	private ImageIcon			imgMute;
	private ImageIcon			imgVolume0;
	private ImageIcon			imgVolume1;
	private ImageIcon			imgVolume2;
	private ImageIcon			imgVolume3;
	private ImageIcon			imgVolume4;
	private ImageIcon			imgVolume5;

	public MediaToolBar(MainFrame mainFrame)
	{
		setName("MediaScrub - ToolBar");
		this.mainFrame = mainFrame;

		loadImages();

		setLayout(new GridLayout(2, 1));
		this.add(createProgressPanel());
		this.add(createButtonsPanel());
	}

	public void setCurrentTime(long currentTime)
	{
		// if user is not currently moving the knob
		if (!sldrProgress.getValueIsAdjusting())
		{
			timeLabel.setCurrentTime(currentTime);
			int progress;
			if (timeLabel.getFullTime() == 0L)
			{
				// would cause division by 0
				progress = 0;
			}
			else
			{
				progress = (int) (currentTime * 1_000_000 / timeLabel.getFullTime());
			}

			// signalize that the model changed the time. So no change to the model should be done.
			progressChangedByModel = true;
			sldrProgress.setValue(progress);
			progressChangedByModel = false;
		}
	}

	public void setDuration(long duration)
	{
		timeLabel.setFullTime(duration);
	}

	public void setNowPlaying(Icon icon)
	{
		setNowPlaying(icon, null);
	}

	public void setNowPlaying(Icon icon, String text)
	{
		lblNowPlaying.setIcon(icon);
		if (text != null)
		{
			lblNowPlaying.setText(text);
			lblNowPlaying.setToolTipText(text);
		}
		btnPlayPause.setEnabled(true);
		btnStop.setEnabled(true);
	}

	public void setPlayAction(boolean play)
	{
		if (play)
		{
			btnPlayPause.setAction(playAction);
		}
		else
		{
			btnPlayPause.setAction(pauseAction);
		}
	}

	private void loadImages()
	{
		MainController mainController = MainController.getInstance();
		imgRandom = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Random.png");
		imgRepeat = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Repeat.png");

		imgPrevious = mainController.getImageIcon(MediaUtil.PATH_IMGS_22x22 + "Previous.png");
		imgRewind = mainController.getImageIcon(MediaUtil.PATH_IMGS_22x22 + "Backward.png");
		imgFastForward = mainController.getImageIcon(MediaUtil.PATH_IMGS_22x22 + "Forward.png");
		imgNext = mainController.getImageIcon(MediaUtil.PATH_IMGS_22x22 + "Next.png");

		imgMute = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Mute.png");
		imgVolume0 = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 0.png");
		imgVolume1 = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 1.png");
		imgVolume2 = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 2.png");
		imgVolume3 = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 3.png");
		imgVolume4 = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 4.png");
		imgVolume5 = mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Volume 5.png");
	}

	private JPanel createProgressPanel()
	{
		if (progressPanel == null)
		{
			progressPanel = new JPanel();

			lblNowPlaying = new JLabel("Now Playing");

			// progress = value between 0 and 1,000,000. 1,000,000 = 100%, 10,000 = 1%
			sldrProgress = new JSlider(0, 1_000_000, 0);
			sldrProgress.setPreferredSize(new Dimension(500, sldrProgress.getPreferredSize().height));
			sldrProgress.setPaintTrack(true); // the area the slider slides in
			sldrProgress.setPaintTicks(true);
			sldrProgress.setMajorTickSpacing(500_000);
			sldrProgress.setMinorTickSpacing(250_000);
			sldrProgress.setPaintLabels(false);
			sldrProgress.addChangeListener(this);

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
			btnRandom.setVisible(false);

			btnRepeat = new JButton();
			btnRepeat.setIcon(imgRepeat);
			btnRepeat.setVisible(false);

			btnStop = new JButton();
			btnStop.setAction(new StopAction(mainFrame));
			btnStop.setEnabled(false); //only enable when media is available

			btnPrevious = new JButton();
			btnPrevious.setIcon(imgPrevious);
			btnPrevious.setVisible(false);

			btnRewind = new JButton();
			btnRewind.setIcon(imgRewind);
			btnRewind.setVisible(false);

			btnPlayPause = new JButton();
			playAction = new ContinuePlayAction(mainFrame);
			pauseAction = new PauseAction(mainFrame);
			setPlayAction(true);
			btnPlayPause.setEnabled(false); //only enable when media is available

			btnFastForward = new JButton();
			btnFastForward.setIcon(imgFastForward);
			btnFastForward.setVisible(false);

			btnNext = new JButton();
			btnNext.setIcon(imgNext);
			btnNext.setVisible(false);

			btnToggleFullScreen = new JToggleButton();
			btnToggleFullScreen.setIcon(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Fullscreen.png"));
			btnToggleFullScreen.setActionCommand("toggleFullscreen");
			btnToggleFullScreen.addActionListener(this);
			btnToggleFullScreen.setVisible(false);

			btnMute = new JButton();
			btnMute.setIcon(imgMute);
			btnMute.setToolTipText("Mute");
			btnMute.setActionCommand("mute");
			btnMute.addActionListener(this);

			sldrVolume = new JSlider(0, 100);
			lblVolume = new JLabel();
			sldrVolume.addChangeListener(this);
			int lastVolume = MainController.getInstance().getSettings().getPropertyAsInt(Settings.KEY_PLAYER_VOLUME);
			sldrVolume.setValue(lastVolume);

			// Layout
			GroupLayout grpLayout = new GroupLayout(buttonsPanel);
			grpLayout.setHonorsVisibility(true);
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
					.addPreferredGap(ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
					.addComponent(btnToggleFullScreen)
					.addPreferredGap(ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
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
					.addComponent(btnToggleFullScreen)
					.addComponent(btnMute)
					.addComponent(sldrVolume)
					.addComponent(lblVolume)));
		}
		return buttonsPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if ("mute".equals(e.getActionCommand()))
		{
			sldrVolume.setValue(0);
		}
		else if ("toggleFullscreen".equals(e.getActionCommand()))
		{
			// mainFrame.getScreenTab().getMediaPlayerComponent().getMediaPlayer().toggleFullScreen();
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
			if (!sldrVolume.getValueIsAdjusting())
			{
				int volume = ((JSlider) src).getValue();
				displayVolume(volume);
				mainFrame.getScreenTab().getMediaPlayerComponent().getMediaPlayer().setVolume(volume);
				MainController.getInstance().getSettings().setProperty(Settings.KEY_PLAYER_VOLUME, volume);
			}
		}
		else if (src.equals(sldrProgress))
		{
			if (!sldrProgress.getValueIsAdjusting())
			{
				// System.out.println("PROGRESS: " + sldrProgress.getValue() / 10_000f + "%");
				// if changed by USER
				if (!progressChangedByModel)
				{
					float pos = sldrProgress.getValue() / 1_000_000f;
					// System.out.println("[Progress] changedByUser: " + pos);
					MainController.getInstance().getServerConnection().getStreamPlayer().setPosition(pos);
				}
				else
				{
					// System.out.println("[Progress] changedByModell");
				}
			}
		}
	}
}
