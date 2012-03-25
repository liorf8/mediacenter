package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app.SliderMouseTrackListenerFactory;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app.VolumeSliderChangeListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.TimeLabel;

public class Frame extends JFrame
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private static final long serialVersionUID = -2128474204031146225L;
    private static Frame instance;
    public static final int TIME_SLIDE_MAX = 100 * 100; // 100% * 100

    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private JPanel contentPane;
    private JTabbedPane tabPane;
    private JLabel statusLabel;

    // Media Tab
    private JPanel mediaPanel;
    private Canvas videoCanvas;
    private JToolBar mediaToolBar;
    private JButton btnOpenFile;
    private JButton btnPlayMedia;
    private JButton btnPauseMedia;
    private JButton btnStopMedia;
    private JButton btnRepeat;
    private JButton btnShuffle;
    private JSlider timeSlider;
    private JSlider volumeSlider;
    private TimeLabel timeLabel;

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    private Frame() throws HeadlessException
    {
	super("MedienCenterApp");

	initGUI();
	initAction();

	// set visible after all pending AWT events have been processed
	SwingUtilities.invokeLater(new Runnable()
	{
	    @Override
	    public void run()
	    {
		Frame.getInstance().setVisible(true);
	    }
	});

    }

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static Frame getInstance()
    {
	if (instance == null)
	{
	    return instance = new Frame();
	}
	return instance;
    }

    // --------------------------------------------------------------------------------
    // -- Private Method(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private void initGUI()
    {
	contentPane = new JPanel();
	contentPane.setLayout(new BorderLayout());

	// StatusLabel
	statusLabel = new JLabel();
	statusLabel.setForeground(Color.gray);
	setStatus("MedienCenter gestartet!");
	contentPane.add(statusLabel, BorderLayout.SOUTH);

	// Tabs
	tabPane = new JTabbedPane();
	tabPane.addTab("MedienPlayer", initMediaPanel());
	tabPane.addTab("Konfiguration", initConfigPanel());
	tabPane.addTab("Dateibrowser", initFileBrowser());
	contentPane.add(tabPane, BorderLayout.CENTER);

	this.setContentPane(contentPane);
	this.setSize(640, 480);

	this.setLocation(200, 200);
    }

    // --------------------------------------------------------------------------------
    private JPanel initMediaPanel()
    {
	mediaPanel = new JPanel();
	mediaPanel.setBackground(Color.black);
	mediaPanel.setLayout(new BorderLayout());

	mediaPanel.add(initMediaToolBar(), BorderLayout.SOUTH);

	videoCanvas = new Canvas();
	videoCanvas.setBackground(Color.black);
	mediaPanel.add(videoCanvas, BorderLayout.CENTER);

	return mediaPanel;
    }

    // --------------------------------------------------------------------------------
    private JToolBar initMediaToolBar()
    {
	mediaToolBar = new JToolBar("MediaPlayer Toolbar");
	mediaToolBar.setLayout(new GridLayout(2, 1));

	// Buttons
	JPanel mediaButtonsPanel = new JPanel();
	mediaToolBar.add(mediaButtonsPanel);
	mediaButtonsPanel.setLayout(new BoxLayout(mediaButtonsPanel, BoxLayout.LINE_AXIS));
	btnOpenFile = new JButton();
	btnPlayMedia = new JButton();
	btnPauseMedia = new JButton();
	btnStopMedia = new JButton();
	btnRepeat = new JButton();
	btnShuffle = new JButton();
	mediaButtonsPanel.add(btnOpenFile);
	mediaButtonsPanel.add(btnPlayMedia);
	mediaButtonsPanel.add(btnPauseMedia);
	mediaButtonsPanel.add(btnStopMedia);
	mediaButtonsPanel.add(btnRepeat);
	mediaButtonsPanel.add(btnShuffle);

	// VolumeSlider
	volumeSlider = new JSlider(0, 200, 100);
	mediaButtonsPanel.add(volumeSlider);
	Dictionary<Integer, JLabel> volumeSliderLabelTable = new Hashtable<Integer, JLabel>();
	volumeSliderLabelTable.put(new Integer(0), new JLabel("0"));
	volumeSliderLabelTable.put(new Integer(200), new JLabel("200"));
	volumeSlider.setLabelTable(volumeSliderLabelTable);
	volumeSlider.setMajorTickSpacing(100);
	volumeSlider.setPaintLabels(true);
	volumeSlider.setPaintTicks(true);
	volumeSlider.setToolTipText("100");

	// TimeLabel
	timeLabel = new TimeLabel();
	mediaButtonsPanel.add(timeLabel);

	// TimeSlider
	JPanel timeSliderPanel = new JPanel();
	mediaToolBar.add(timeSliderPanel);
	timeSliderPanel.setLayout(new GridLayout(1, 1));
	timeSlider = new JSlider(0, TIME_SLIDE_MAX, 0);
	timeSliderPanel.add(timeSlider);

	return mediaToolBar;
    }

    // --------------------------------------------------------------------------------
    private JPanel initConfigPanel()
    {
	return new JPanel();
    }

    // --------------------------------------------------------------------------------
    private JPanel initFileBrowser()
    {
	return new JPanel();
    }

    // --------------------------------------------------------------------------------
    private void initAction()
    {
	// Window Closing
	this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new WindowAdapter()
	{
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		new ExitAppAction().actionPerformed(null);
	    }
	});

	// Listener
	timeSlider.addMouseMotionListener(SliderMouseTrackListenerFactory
		.getTrackListenerInstance(timeSlider));
	volumeSlider.addChangeListener(new VolumeSliderChangeListener());

	// Buttons
	btnOpenFile.setAction(new OpenFileAction());
	btnPlayMedia.setAction(new PlayMediaAction());
	btnPauseMedia.setAction(new PauseMediaAction());
	btnStopMedia.setAction(new StopMediaAction());
	btnRepeat.setAction((new RepeatAction()));
	btnShuffle.setAction((new ShuffleAction()));
    }

    // --------------------------------------------------------------------------------
    public void setVideoCanvasSize(Dimension dim)
    {
	videoCanvas.setSize(dim);
    }

    // --------------------------------------------------------------------------------
    public Canvas getVideoCanvas()
    {
	return videoCanvas;
    }

    // --------------------------------------------------------------------------------
    public void setStatus(String status)
    {
	this.statusLabel.setText(status);
    }

    // --------------------------------------------------------------------------------
    public void setGUIForPlayMedia(String mrl)
    {
	setStatus("Playing '" + mrl + "'");

	// first reset the timeslider. the infos come later from the VideoOutputEventListener
	setTimeSliderValue(0);
	timeLabel.setFullTime("--:--");
    }

    // --------------------------------------------------------------------------------
    public void setTimeSliderValue(int percentTimes100)
    {
	timeSlider.setValue(percentTimes100);
    }

    // --------------------------------------------------------------------------------
    public TimeLabel getTimeLabel()
    {
	return timeLabel;
    }

    // --------------------------------------------------------------------------------
    public void setVolumeToolTip(String tt)
    {
	volumeSlider.setToolTipText(tt);
    }
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
