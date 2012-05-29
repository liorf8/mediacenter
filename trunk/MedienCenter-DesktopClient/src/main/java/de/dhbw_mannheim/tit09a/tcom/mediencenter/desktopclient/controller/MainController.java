package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller;

import java.awt.EventQueue;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import com.sun.jna.NativeLibrary;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.MainFrameWindowListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.ScheduledEventDispatcher;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionStateConnected;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionStateDisconnected;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionStateLoggedIn;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnection;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionState;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionStateListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.screen.ScreenTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.NamedThreadPoolFactory;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class MainController implements ServerConnectionStateListener, PropertyChangeListener
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static volatile MainController	instance;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static MainController getInstance()
	{
		if (instance == null)
		{
			synchronized (MainController.class)
			{
				if (instance == null)
				{
					instance = new MainController();
				}
			}
		}
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static Logger				LOGGER					= LoggerFactory.getLogger(MainController.class);
	private ScheduledEventDispatcher	scheduledEventDispatcher;
	private Map<String, ImageIcon>		iconPool;

	private Settings					settings;
	private ServerConnectionImpl		serverConnection;
	private ClientCallback				callback;

	private MainFrame					mainFrame;
	private MainController				thisControllerInstance	= this;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public MainController()
	{
		try
		{
			scheduledEventDispatcher = new ScheduledEventDispatcher(5, new NamedThreadPoolFactory("ScheduledEventDispatcher"));
			iconPool = new HashMap<>();

			serverConnection = new ServerConnectionImpl();
			serverConnection.addStateListener(this);

			settings = new Settings(this);
			// VLCj needs to be initialized
			initVlc();

			callback = new DefaultClientCallback(mainFrame);
			setLookAndFeel();
		}
		catch (Throwable t)
		{
			System.err.println("Exception while initializing:");
			t.printStackTrace();
			System.exit(1);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public ScheduledEventDispatcher getScheduledEventDispatcher()
	{
		return scheduledEventDispatcher;
	}

	// --------------------------------------------------------------------------------
	public ImageIcon getImageIcon(String uri)
	{
		return getImageIcon(uri, null);
	}

	// --------------------------------------------------------------------------------
	public ImageIcon getImageIcon(String uri, String description)
	{
		ImageIcon ico = iconPool.get(uri);
		if (ico == null)
		{
			ico = MediaUtil.getIconFromResource(uri, description);
			iconPool.put(uri, ico);
		}
		return ico;
	}

	// --------------------------------------------------------------------------------
	public Settings getSettings()
	{
		return settings;
	}

	// --------------------------------------------------------------------------------
	public ServerConnection getServerConnection()
	{
		return serverConnection;
	}

	// --------------------------------------------------------------------------------
	public ClientCallback getClientCallback()
	{
		return callback;
	}

	// --------------------------------------------------------------------------------
	public void exit()
	{
		try
		{
			LOGGER.info("Exiting application");

			LOGGER.info("Releasing VLCj components");
			ScreenTab screenTab = mainFrame.getScreenTab();
			if (screenTab != null)
			{
				screenTab.releaseMediaPlayerComponent();
			}

			LOGGER.info("Saving settings");
			getSettings().storeProperties();

			LOGGER.info("Disconnecting from server");
			serverConnection.disconnect();

			LOGGER.info("Disposing window");
			disposeWindow(mainFrame);

			LOGGER.info("Shut down MainController's ScheduledEventDispatcher");
			scheduledEventDispatcher.shutdownNow();

			LOGGER.info("System.exit(0)");
			System.exit(0);
		}
		catch (Throwable t)
		{
			System.err.println("Exception while exiting:");
			t.printStackTrace();
			System.exit(1);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void setLookAndFeel() throws InvocationTargetException, InterruptedException
	{
		EventQueue.invokeAndWait(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					// "javax.swing.plaf.metal.MetalLookAndFeel")
					// "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					// UIManager.getCrossPlatformLookAndFeelClassName());
					// UIManager.getSystemLookAndFeelClassName()
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	// --------------------------------------------------------------------------------
	private MainFrame buildMainFrame(final MainController mainController) throws InvocationTargetException, InterruptedException
	{
		EventQueue.invokeAndWait(new Runnable()
		{
			@Override
			public void run()
			{
				mainFrame = new MainFrame();
				mainFrame.addWindowListener(new MainFrameWindowListener(thisControllerInstance, mainFrame));
			}
		});
		return mainFrame;
	}

	// --------------------------------------------------------------------------------
	private void disposeWindow(final Window window) throws InvocationTargetException, InterruptedException
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (window != null)
					window.dispose();
			}
		});
	}

	// --------------------------------------------------------------------------------
	private void initVlc() throws Exception
	{
		String vlcInstallDir = getSettings().getProperty(Settings.KEY_STREAMING_VLC_INSTALL_DIR);
		LOGGER.info("VLC installation directory: {}", vlcInstallDir);

		// Locate the dll's (libvlc.dll and libvlccore.dll on Windows)
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcInstallDir);
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreName(), vlcInstallDir);
		LOGGER.info("libvlc version: {}", LibVlcVersion.getVersion());

		// Create a libvlc factory
		LibVlcFactory.factory().atLeast("2.0.0").log().synchronise().discovery(new NativeDiscovery()).create();
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void stateChanged(ServerConnectionState oldState, ServerConnectionState newState)
	{
		System.out.println("CONNECTION: stateChanged(): " + oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName() + " @"
				+ Thread.currentThread());

		if (newState instanceof ServerConnectionStateDisconnected)
		{
			EventQueue.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					mainFrame.setLoggedIn(false);
				}
			});
		}
		else if (newState instanceof ServerConnectionStateConnected)
		{

		}
		else if (newState instanceof ServerConnectionStateLoggedIn)
		{
			EventQueue.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					mainFrame.setLoggedIn(true);
				}
			});
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void propertyChange(PropertyChangeEvent evt)
	{
		System.out.println("SETTINGS:propertyChange(): " + evt);
		String propName = evt.getPropertyName();
		if (propName.equals(Settings.KEY_SERVER_HOST))
		{
			serverConnection.setServerHost((String) evt.getNewValue());
		}
		else if (propName.equals(Settings.KEY_SERVER_REGISTRY_PORT))
		{
			serverConnection.setServerRegistryPort(Integer.parseInt((String) evt.getNewValue()));
		}
		else if (propName.equals(Settings.KEY_SERVER_BINDNAME))
		{
			serverConnection.setServerBindname((String) evt.getNewValue());
		}

	}

	// --------------------------------------------------------------------------------
	// -- Main Method -----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void main(String[] args) throws InvocationTargetException, InterruptedException
	{
		MainController ctrl = MainController.getInstance();
		ctrl.buildMainFrame(ctrl).setVisible(true);
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
