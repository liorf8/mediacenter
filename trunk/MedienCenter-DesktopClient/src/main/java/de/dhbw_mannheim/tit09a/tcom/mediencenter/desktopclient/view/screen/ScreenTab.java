package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.screen;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.windows.WindowsCanvas;

import com.sun.jna.Platform;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.MediaComponentEventLister;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class ScreenTab extends Tab
{
	private static final long					serialVersionUID	= -3389749062594573938L;

	private EmbeddedMediaPlayerComponentImpl	mediaPlayerComp;
	private FileInfo							currentFile;

	public ScreenTab(MainFrame mainFrame)
	{
		super(mainFrame, "Screen");

		initGUI();
	}

	@Override
	public String getTip()
	{
		return "The Screen";
	}

	@Override
	public Icon getIcon()
	{
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Play.png");
	}

	public void playStream(String streamMrl, FileInfo currentFile)
	{
		mediaPlayerComp.getMediaPlayer().playMedia(streamMrl);
		setCurrentFile(currentFile);
	}

	public void setCurrentFile(FileInfo currentFile)
	{
		this.currentFile = currentFile;
	}

	public FileInfo getCurrentFile()
	{
		return currentFile;
	}

	public EmbeddedMediaPlayerComponentImpl getMediaPlayerComponent()
	{
		return mediaPlayerComp;
	}

	public void releaseMediaPlayerComponent()
	{
		if (mediaPlayerComp != null)
			mediaPlayerComp.release();
	}

	private void initGUI()
	{
		this.setLayout(new BorderLayout());
		mediaPlayerComp = new EmbeddedMediaPlayerComponentImpl();
		add(mediaPlayerComp, BorderLayout.CENTER);
	}

	public class EmbeddedMediaPlayerComponentImpl extends EmbeddedMediaPlayerComponent
	{
		private static final long	serialVersionUID	= 1L;

		private Canvas				canvas;

		private EmbeddedMediaPlayerComponentImpl()
		{
			getMediaPlayer().addMediaPlayerEventListener(new MediaComponentEventLister(mainFrame));
		}

		@Override
		protected FullScreenStrategy onGetFullScreenStrategy()
		{
			return new MainFrameFullScreenStrategy();
		}

		@Override
		protected Canvas onGetCanvas()
		{
			if (Platform.isWindows())
			{
				//canvas = new WindowsCanvas();
				canvas = new Canvas();
				// TODO with WindowsCanvas no Events are received -.-
				System.out.println("-----WindowsCanvas: " + canvas);
			}
			else
			{
				canvas = new Canvas();
			}
			canvas.setBackground(Color.BLACK);
			canvas.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					System.out.println("MouseClicked");
					if (e.getClickCount() == 2)
					{
						System.out.println("MouseClicked twice: " + e);
						getMediaPlayer().toggleFullScreen();
					}
				}
			});
			return canvas;
		}

		@Override
		public void onBeforeRelease()
		{
			MainController.LOGGER.info("Releasing: {} (Canvas: {})", this, canvas);
			if (canvas != null && canvas instanceof WindowsCanvas)
			{
				((WindowsCanvas) canvas).release();
			}
		}
	}

	public class MainFrameFullScreenStrategy extends DefaultFullScreenStrategy
	{
		public MainFrameFullScreenStrategy()
		{
			super(mainFrame);
		}

		@Override
		protected void onBeforeEnterFullScreenMode()
		{
			mainFrame.dispose();
			mainFrame.enterFullScreen(mediaPlayerComp);
			mainFrame.setUndecorated(true);
			mainFrame.setVisible(true);
		}

		@Override
		protected void onAfterExitFullScreenMode()
		{
			mainFrame.dispose();
			mainFrame.setUndecorated(false);
			mainFrame.exitFullScreen();
			mainFrame.setVisible(true);
		}

	}

}
