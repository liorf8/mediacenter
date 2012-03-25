package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller;

import java.awt.EventQueue;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class MainController implements SimonConnectionStateListener
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
	public final Logger			logger	= LoggerFactory.getLogger(this.getClass());

	private MainFrame			mainFrame;
	private SimonConnectionImpl	simonConnection;
	private ClientCallback		callback;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public MainController()
	{
		try
		{
			callback = new DefaultClientCallback(mainFrame);
			simonConnection = new SimonConnectionImpl();
			simonConnection.addStateListener(this);

			setLookAndFeel();
			buildMainFrame().setVisible(true);
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
	public void exit()
	{
		try
		{
			System.out.println("exit() @" + Thread.currentThread());
			simonConnection.disconnect();
			disposeWindow(mainFrame);
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
	private MainFrame buildMainFrame() throws InvocationTargetException, InterruptedException
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
	public void stateChanged(SimonConnectionState oldState, SimonConnectionState newState)
	{
		System.out.println("stateChanged(): " + oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName() + " @"
				+ Thread.currentThread());

		if (newState instanceof SimonConnectionStateDisconnected)
		{
			mainFrame.setLoggedIn(false);
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
	// -- Main Method -----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void main(String[] args)
	{
		MainController.getInstance();
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
