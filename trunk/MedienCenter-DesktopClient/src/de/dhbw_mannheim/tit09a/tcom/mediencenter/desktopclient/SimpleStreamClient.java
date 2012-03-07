package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.sun.jna.NativeLibrary;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.FileInfoTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

public class SimpleStreamClient
{
	private static Lookup					nameLookup		= null;
	private static Server					server			= null;
	private static Session					session;
	private static StreamMediaPlayer		remotePlayer	= null;

	private static final String				protocol		= "http";
	private static final int				port			= 8080;
	private static String					streamPath;

	private JFrame							frame;

	private JTabbedPane						tabbedPane;

	private JPanel							mediaTab;
	private JButton							btnPlay;
	private JButton							btnPause;
	private JButton							btnStop;
	private JToggleButton					btnToggleFullScreen;

	private JPanel							fileTreeTab;
	private JButton							btnPlayPath;
	private JTextField						txtFldPath;
	private JLabel							lblFileInfo;
	private JTextField						txtFldElapsedTime;

	private EmbeddedMediaPlayerComponent	mediaPlayerComponent;

	public static void main(String[] args)
	{
		try
		{
			initSimon();

			initVlc();

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					new SimpleStreamClient();
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
			exit();
		}
	}

	private static void initVlc()
	{
		String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
		if (vlcInstallDir == null)
			vlcInstallDir = "D:\\mhertram\\VLCPortable\\App\\vlc";
		NativeLibrary.addSearchPath("libvlc", vlcInstallDir);
		NativeLibrary.addSearchPath("libvlccore", vlcInstallDir);
	}

	private static void initSimon() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
		server = (Server) nameLookup.lookup(Server.BIND_NAME);
		nameLookup.addClosedListener(server, new ClosedListener()
		{
			public void closed()
			{
				System.out.println("Closed!");
			}
		});

		DefaultClientCallback callback = new DefaultClientCallback(null);
		// server.register("Max", "pw");
		session = (Session) server.login("Max", "pw", callback);
		remotePlayer = session.getRemoteMediaPlayer();
		streamPath = remotePlayer.setAndGetStreamTarget(protocol, port, "h264", "a52", 1024, 192, false, false);
	}

	private SimpleStreamClient()
	{
		try
		{
			System.out.println("init " + getClass().getSimpleName());

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			frame = createFrame();

			mediaTab = createMediaPanel();
			fileTreeTab = createFileTreePanel();
			tabbedPane = createTabbedPane();
			frame.setContentPane(tabbedPane);

			frame.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (mediaPlayerComponent != null)
				mediaPlayerComponent.release();
			exit();
		}
	}

	private JFrame createFrame()
	{
		JFrame frame = new JFrame(this.getClass().getSimpleName());
		frame.setLocation(100, 100);
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				if (mediaPlayerComponent != null)
					mediaPlayerComponent.release();
				exit();
			}
		});

		return frame;
	}

	private JTabbedPane createTabbedPane()
	{
		JTabbedPane pane = new JTabbedPane();
		pane = new JTabbedPane();
		pane.add(mediaTab, "Media");
		pane.add(fileTreeTab, "FileTree");
		return pane;
	}

	private JPanel createMediaPanel()
	{
		JPanel mediaPanel = new JPanel();
		mediaPanel.setLayout(new BorderLayout());
		mediaPanel.setBackground(Color.black);

		// Create a media player instance (in this example an embedded media player)
		mediaPlayerComponent = new EventHandlingEmbeddedMediaPlayerComponent();
		mediaPanel.add(mediaPlayerComponent, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.setMinimumSize(new Dimension(500, 100));
		btnPlay = new JButton(new AbstractAction("Play")
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("[play]");
				if (remotePlayer.play())
				{
					if (!mediaPlayerComponent.getMediaPlayer().isPlayable())
					{
						mediaPlayerComponent.getMediaPlayer().playMedia(mediaPlayerComponent.getMediaPlayer().mrl());
					}
				}
			}
		});

		btnPause = new JButton(new AbstractAction("Pause")
		{
			private static final long	serialVersionUID	= 1L;

			public void actionPerformed(ActionEvent e)
			{
				remotePlayer.pause();
			}
		});
		btnStop = new JButton(new AbstractAction("Stopp")
		{
			private static final long	serialVersionUID	= 1L;

			public void actionPerformed(ActionEvent e)
			{
				remotePlayer.stop();
			}
		});
		btnToggleFullScreen = new JToggleButton(new AbstractAction("FullScreen")
		{
			private static final long	serialVersionUID	= 1L;

			public void actionPerformed(ActionEvent e)
			{
				mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
			}
		});

		btnPanel.add(btnPlay);
		btnPanel.add(btnPause);
		btnPanel.add(btnStop);
		btnPanel.add(btnToggleFullScreen);
		mediaPanel.add(btnPanel, BorderLayout.SOUTH);

		return mediaPanel;
	}

	private JPanel createFileTreePanel()
	{
		JPanel treePanel = new JPanel(new BorderLayout());
		lblFileInfo = new JLabel("");
		treePanel.add(lblFileInfo, BorderLayout.NORTH);

		JTree fileTree = createTree();
		treePanel.add(new JScrollPane(fileTree), BorderLayout.CENTER);

		JPanel treeButtonPanel = new JPanel();
		btnPlayPath = new JButton(new AbstractAction("Play Path")
		{
			private static final long	serialVersionUID	= 1L;

			public void actionPerformed(ActionEvent e)
			{
				try
				{
					mediaPlayerComponent.getMediaPlayer().playMedia(streamPath);
					remotePlayer.play(getTxtFldPathText());
					remotePlayer.setTime(getTxtFldElapsedTimeText());
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		treeButtonPanel.add(btnPlayPath);

		txtFldPath = new JTextField();
		txtFldPath.setColumns(25);
		treeButtonPanel.add(txtFldPath);

		treeButtonPanel.add(new JLabel("start at"));

		txtFldElapsedTime = new JTextField();
		txtFldElapsedTime.setColumns(15);
		treeButtonPanel.add(txtFldElapsedTime);
		treeButtonPanel.add(new JLabel("ms"));

		treePanel.add(treeButtonPanel, BorderLayout.SOUTH);

		return treePanel;
	}

	private JTree createTree()
	{
		final FileInfoTree tree = new FileInfoTree(session);
		tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			@Override
			public void valueChanged(TreeSelectionEvent evt)
			{
				FileInfo fi = tree.getSelectedFileInfo();
				if (fi != null)
				{
					setTxtFldPathText(fi.getPath());
					setLblFileInfo(fi.toString(true));
					setTxtFldElapsedTimeText(fi.getElapsedTime());
				}
			}
		});
		return tree;
	}

	private void setTxtFldPathText(String text)
	{
		txtFldPath.setText(text);
	}

	private String getTxtFldPathText()
	{
		return txtFldPath.getText();
	}

	private void setTxtFldElapsedTimeText(long text)
	{
		txtFldElapsedTime.setText(text + "");
	}

	private long getTxtFldElapsedTimeText()
	{
		return Long.parseLong(txtFldElapsedTime.getText());
	}

	private void setLblFileInfo(String info)
	{
		lblFileInfo.setText(info);
	}

	// --------------------------------------------------------------------------------
	private static void exit()
	{
		// and finally 'release' the serverobject to release to connection to the server
		if (nameLookup != null && server != null)
		{
			nameLookup.release(server);
			server = null;
		}
		else
			System.err.println("lookup or server == null" + nameLookup + "," + server);

		System.exit(0);
	}

	// --------------------------------------------------------------------------------
	private class EventHandlingEmbeddedMediaPlayerComponent extends EmbeddedMediaPlayerComponent
	{
		private static final long	serialVersionUID	= 1L;
		private Canvas				canvas;

		@Override
		public void videoOutput(MediaPlayer mediaPlayer, int newCount)
		{
			System.out.println("videoOutput: " + newCount);
			new GetVideoDimensionTask(mediaPlayer, canvas).execute();
		}

		@Override
		protected FullScreenStrategy onGetFullScreenStrategy()
		{
			return new UnDecoratingFullScreenStrategy(frame);
		}

		@Override
		protected Canvas onGetCanvas()
		{
			canvas = new Canvas();
			canvas.setBackground(Color.black);
			return canvas;
		}

		@Override
		public void playing(MediaPlayer mediaPlayer)
		{
			System.out.println("playing");
		}

		@Override
		public void error(MediaPlayer mediaPlayer)
		{
			System.out.println("error");
		}

		@Override
		public void finished(MediaPlayer mediaPlayer)
		{
			System.out.println("finished");
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * Waits on a SwingWorker Thread until VideoDimension is fetched from the MediaPlayer. If you are streaming, it takes a while until
	 * getVideoDimension() is not returning zero. When the Dimension is fetched, it resizes the Canvas accordingly on the EvendDispatcher Thread.
	 * 
	 * @author Max
	 * 
	 */
	private class GetVideoDimensionTask extends SwingWorker<Dimension, Object>
	{
		private final MediaPlayer	mediaPlayer;
		private final Canvas		canvas;
		private final Dimension		defaultDim	= new Dimension(720, 405);

		private GetVideoDimensionTask(MediaPlayer mediaPlayer, Canvas canvas)
		{
			this.mediaPlayer = mediaPlayer;
			this.canvas = canvas;
		}

		@Override
		protected Dimension doInBackground() throws Exception
		{
			// System.out.println(Thread.currentThread() + " working. "+System.currentTimeMillis());
			Dimension size = mediaPlayer.getVideoDimension();
			if (size != null)
			{
				final int waitLimit = 2000;
				final int sleep = 40;
				int tryCounter = 0;

				while (size.width == 0)
				{
					Thread.sleep(sleep);
					size = mediaPlayer.getVideoDimension();
					if (tryCounter > (waitLimit / sleep))
					{
						System.out.println("VideoDimension still zero. Using defaults!");
						return defaultDim;
					}
					tryCounter++;
				}
				return size;
			}
			return defaultDim;
		}

		@Override
		protected void done()
		{
			try
			{
				// System.out.println(Thread.currentThread() + " done. "+System.currentTimeMillis());
				canvas.setSize(get());
				frame.pack();
			}
			catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		}
	}

	private class UnDecoratingFullScreenStrategy extends DefaultFullScreenStrategy
	{
		public UnDecoratingFullScreenStrategy(JFrame frame)
		{
			super(frame);
		}

		@Override
		protected void onBeforeEnterFullScreenMode()
		{
			// frame.setContentPane(mediaTab);
		}

		@Override
		protected void onAfterExitFullScreenMode()
		{
			// tabbedPane = createTabbedPane();
			// frame.setContentPane(tabbedPane);
		}
	}

}
