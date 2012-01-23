package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.vlcj;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener;

@SuppressWarnings("deprecation")
public class MyPlayer_deprecated
{
    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private boolean is64BitVM = false;
    private final char fileSep = System.getProperty("file.separator").charAt(0);

    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas videoCanvas;

    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface videoSurface;
    private JLabel header;

    // --------------------------------------------------------------------------------
    // -- Main Method -----------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static void main(final String[] args)
    {
	if (args.length == 0)
	{
	    System.out.println("No argument. Exiting...");
	    System.exit(1);
	}
	SwingUtilities.invokeLater(new Runnable()
	{
	    @Override
	    public void run()
	    {
		new MyPlayer_deprecated().start(args[0]);
	    }
	});
    }

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public MyPlayer_deprecated()
    {
	String vmName = System.getProperty("java.vm.name");
	if (vmName.indexOf("64") > 0)
	{
	    is64BitVM = true;
	}
	System.out.println("VM@" + vmName + " (64-bit: " + is64BitVM + ")");

	// !!! Does not work if VM is 64-bit (JRE7); returns null:
	// String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
	// System.out.println("VLC@" + vlcInstallDir);

	try
	{
	    String bitFolder = "x86";
	    if (is64BitVM) bitFolder = "x64";

	    URL libVLC = new URL(
		    MyPlayer_deprecated.class.getProtectionDomain().getCodeSource().getLocation(),
		    "de\\dhbw_mannheim\\tit09a\\tcom\\lib\\vlc\\" + bitFolder);

	    // URL.getFile() looks like this: "/E:/Java/..." So cut the first char and replace / by \
	    String libVLCStr = libVLC.getFile().replace('/', fileSep).substring(1);
	    System.out.println("DLL@" + libVLCStr);
	    // String VLCPath =
	    // "E:\\Java\\Studienarbeit\\workspace\\MediaCenter\\build\\classes\\de\\dhbw_mannheim\\tit09a\\tcom\\lib\\vlc\\x86";
	    NativeLibrary.addSearchPath("libvlc", libVLCStr);
	    NativeLibrary.addSearchPath("libvlccore", libVLCStr);
	}
	catch (MalformedURLException e1)
	{
	    e1.printStackTrace();
	}

	videoCanvas = new Canvas();
	videoCanvas.setBackground(Color.black);

	contentPane = new JPanel();
	contentPane.setBackground(Color.black);
	contentPane.setLayout(new BorderLayout());
	contentPane.add(videoCanvas, BorderLayout.CENTER);

	header = new JLabel();
	header.setForeground(Color.gray);
	//contentPane.add(header, BorderLayout.NORTH);

	frame = new JFrame("My vlcj player");
	frame.setContentPane(contentPane);
	frame.setSize(640, 480);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	mediaPlayerFactory = new MediaPlayerFactory(new String[] { "--no-video-title-show" });
	mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
	videoSurface = mediaPlayerFactory.newVideoSurface(videoCanvas);
	mediaPlayer.setVideoSurface(videoSurface);
	mediaPlayer.addVideoOutputEventListener(new VideoOutputEventListener()
	{
	    @Override
	    public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput)
	    {
		System.out.println("videoOutput? " + videoOutput);
		System.out.println("mediaPlayer " + mediaPlayer);
		if (videoOutput)
		{
		    Dimension size = mediaPlayer.getVideoDimension();
		    if (size != null)
		    {
			//videoCanvas.setSize(size.width, size.height);
			//frame.pack();
			

			frame.setVisible(false);
			frame.dispose();
			
			GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
			int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			//videoCanvas.setSize(width, height);
			frame.setUndecorated(true);
			frame.setSize(width, height);
			graphicsDevice.setFullScreenWindow(frame);
			
			frame.pack();
			frame.setVisible(true);
		    }
		}
	    }
	});

	frame.addWindowListener(new WindowAdapter()
	{
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		mediaPlayer.release();
		mediaPlayerFactory.release();
	    }
	});

	frame.setVisible(true);
    }

    // --------------------------------------------------------------------------------
    // -- Private Method(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private void start(final String path)
    {
	System.out.println("Playing " + path);
	mediaPlayer.playMedia(path);
	setHeader(path);
    }

    // --------------------------------------------------------------------------------
    private void setHeader(String text)
    {
	this.header.setText(text);
    }
    
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}