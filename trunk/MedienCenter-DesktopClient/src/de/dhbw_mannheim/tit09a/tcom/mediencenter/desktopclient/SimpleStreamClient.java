package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.sun.jna.NativeLibrary;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

public class SimpleStreamClient
{
	private static Lookup						nameLookup		= null;
	private static Server						server			= null;
	private static StreamMediaPlayer			remotePlayer	= null;

	private static final String					protocol		= "http";
	private static final int					port			= 5555;
	private static final String					path			= "cougar2x20.mkv";

	private final JFrame						frame;

	private final JPanel						contentPane;
	private final JButton						btnPlay;
	private final JButton						btnPause;
	private final JButton						btnStop;

	private final EmbeddedMediaPlayerComponent	mediaPlayerComponent;

	public static void main(String[] args)
	{
		try
		{
			String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
			if (vlcInstallDir == null)
				vlcInstallDir = "D:\\mhertram\\VLCPortable\\App\\vlc";
			NativeLibrary.addSearchPath("libvlc", vlcInstallDir);
			NativeLibrary.addSearchPath("libvlccore", vlcInstallDir);

			nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
			server = (Server) nameLookup.lookup(Server.BIND_NAME);
			nameLookup.addClosedListener(server, new ClosedListener()
			{
				public void closed()
				{
					System.out.println("Closed!");
				}
			});

			ClientCallbackImpl callback = new ClientCallbackImpl(null);
			// server.register("Max", "pw");
			Session session = (Session) server.login("Max", "pw", callback);
			remotePlayer = session.getRemoteMediaPlayer(protocol, port);
			final String streamPath = remotePlayer.getMrlForClient();
			// remotePlayer.clearList();
			remotePlayer.play(path);

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					SimpleStreamClient client = new SimpleStreamClient();
					client.start(streamPath);
				}
			});

		}
		catch (Exception e)
		{
			exit();
		}
	}

	private SimpleStreamClient()
	{
		System.out.println("init");

		frame = new JFrame(this.getClass().getSimpleName());
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

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.black);
		frame.setContentPane(contentPane);

		// Create a media player instance (in this example an embedded media player)
		mediaPlayerComponent = new EventHandlingEmbeddedMediaPlayerComponent();
		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.setMinimumSize(new Dimension(500, 100));
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					remotePlayer.play();
					System.out.println(TimeValue.formatMillis(remotePlayer.getLength()));
					if (!mediaPlayerComponent.getMediaPlayer().isPlayable())
					{
						mediaPlayerComponent.getMediaPlayer().playMedia(mediaPlayerComponent.getMediaPlayer().mrl());
					}
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					remotePlayer.pause();
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					remotePlayer.stop();
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPanel.add(btnPlay);
		btnPanel.add(btnPause);
		btnPanel.add(btnStop);
		contentPane.add(btnPanel, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	// --------------------------------------------------------------------------------
	private void start(String mrl)
	{
		System.out.println("Playing: " + mrl);
		mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
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

}
