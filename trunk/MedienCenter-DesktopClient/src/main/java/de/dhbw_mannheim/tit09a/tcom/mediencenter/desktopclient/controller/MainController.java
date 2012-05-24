package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller;

import java.awt.EventQueue;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnectionStateConnected;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnectionStateDisconnected;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnectionStateLoggedIn;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnection;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnectionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnectionState;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnectionStateListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.NamedThreadPoolFactory;

public class MainController implements SimonConnectionStateListener, PropertyChangeListener
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
	public final Logger					logger	= LoggerFactory.getLogger(this.getClass());

	private ScheduledEventDispatcher	scheduledEventDispatcher;
	private Settings					settings;
	private SimonConnectionImpl			simonConnection;
	private ClientCallback				callback;

	private MainFrame					mainFrame;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public MainController()
	{
		try
		{
			scheduledEventDispatcher = new ScheduledEventDispatcher(5, new NamedThreadPoolFactory("ScheduledEventDispatcher"));

			simonConnection = new SimonConnectionImpl();
			simonConnection.addStateListener(this);
			
			settings = new Settings(this);

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
	public Settings getSettings()
	{
		return settings;
	}

	// --------------------------------------------------------------------------------
	public SimonConnection getSimonConnection()
	{
		return simonConnection;
	}

	// --------------------------------------------------------------------------------
	public ClientCallback getClientCallback()
	{
		return callback;
	}

	// --------------------------------------------------------------------------------
	public ScheduledEventDispatcher getScheduledEventDispatcher()
	{
		return scheduledEventDispatcher;
	}

	// --------------------------------------------------------------------------------
	public void exit()
	{
		try
		{
			System.out.println("exit() @" + Thread.currentThread());

			// save settings
			settings.storeProperties();

			// release connection
			simonConnection.disconnect();

			// close gui
			disposeWindow(mainFrame);

			scheduledEventDispatcher.shutdownNow();
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
	@Override
	public synchronized void stateChanged(SimonConnectionState oldState, SimonConnectionState newState)
	{
		System.out.println("stateChanged(): " + oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName() + " @"
				+ Thread.currentThread());

		if (newState instanceof SimonConnectionStateDisconnected)
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
		else if (newState instanceof SimonConnectionStateConnected)
		{

		}
		else if (newState instanceof SimonConnectionStateLoggedIn)
		{
			mainFrame.setLoggedIn(true);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void propertyChange(PropertyChangeEvent evt)
	{
		System.out.println("propertyChange: " +evt);
		String propName = evt.getPropertyName();
		if (propName.equals(Settings.KEY_SERVER_HOST))
		{
			simonConnection.setServerHost((String) evt.getNewValue());
		}
		else if (propName.equals(Settings.KEY_SERVER_REGISTRY_PORT))
		{
			simonConnection.setServerRegistryPort(Integer.parseInt((String)evt.getNewValue()));
		}
		else if (propName.equals(Settings.KEY_SERVER_BINDNAME))
		{
			simonConnection.setServerBindname((String) evt.getNewValue());
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