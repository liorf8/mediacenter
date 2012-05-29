package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.HomeTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.LoginTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.MediaLibraryTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.screen.ScreenTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings.SettingsTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.upload.UploadTab;

public class MainFrame extends JFrame
{
	private static final long	serialVersionUID	= -6050971688251747359L;

	private JPanel				mainPanel;
	private JTabbedPane			tabbedPane;
	private LoginTab			loginTab;
	private HomeTab				homeTab;
	private MediaLibraryTab		mediaLibraryTab;
	private ScreenTab			screenTab;
	private UploadTab			uploadTab;
	private SettingsTab			settingsTab;

	private JPanel				statusPanel;
	private int					statusPanelCounter	= 0;

	private MediaToolBar		mediaToolBar;

	public MainFrame()
	{
		super("MediaScrub");

		initGUI();
	}

	public void enterFullScreen(Container fullScreenContainer)
	{
		getContentPane().removeAll();
		getContentPane().add(fullScreenContainer);
	}

	public void exitFullScreen()
	{
		getContentPane().removeAll();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		addMediaToolBar();
	}

	public MediaToolBar getMediaToolBar()
	{
		return mediaToolBar;
	}

	public LoginTab getLoginTab()
	{
		return loginTab;
	}

	public MediaLibraryTab getMediaLibraryTab()
	{
		return mediaLibraryTab;
	}

	public ScreenTab getScreenTab()
	{
		return screenTab;
	}

	public void setLoggedIn(boolean loggedIn)
	{
		tabbedPane.removeAll();
		if (loggedIn)
		{
			addTab(createHomeTab());
			addTab(createMediaLibraryTab());
			addTab(createScreenTab());
			addTab(createUploadTab());
			addTab(createSettingsTab());
			addMediaToolBar();

			setSize(getPreferredSize());
		}
		else
		{
			addTab(createLoginTab());
			addTab(createSettingsTab());

			if (mediaToolBar != null)
				getContentPane().remove(mediaToolBar);

			setSize(getMinimumSize());
		}
	}

	public synchronized void addTaskPanel(TaskPanel tp)
	{
		statusPanelCounter++;
		statusPanel.setLayout(new GridLayout(statusPanelCounter, 1));
		statusPanel.add(tp);
		getContentPane().validate(); // refresh the GUI
	}

	public synchronized void removeTaskPanel(TaskPanel tp)
	{
		statusPanelCounter--;
		statusPanel.setLayout(new GridLayout(statusPanelCounter, 1));
		statusPanel.remove(tp);
		getContentPane().validate();
	}

	private void addTab(Tab tab)
	{
		tabbedPane.addTab(tab.getName(), tab.getIcon(), tab, tab.getTip());
	}

	private void initGUI()
	{
		setLocation(200, 200);
		setPreferredSize(new Dimension(800, 600));
		setMinimumSize(new Dimension(520, 400));
		setSize(getMinimumSize());

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setLayout(new BorderLayout());
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);

		setLoggedIn(false);
	}

	private JPanel createMainPanel()
	{
		if (mainPanel == null)
		{
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());

			tabbedPane = new JTabbedPane();
			mainPanel.add(tabbedPane, BorderLayout.CENTER);

			mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	private LoginTab createLoginTab()
	{
		loginTab = new LoginTab(this);
		return loginTab;
	}

	private HomeTab createHomeTab()
	{
		homeTab = new HomeTab(this);
		return homeTab;
	}

	private MediaLibraryTab createMediaLibraryTab()
	{
		mediaLibraryTab = new MediaLibraryTab(this);
		return mediaLibraryTab;
	}

	private ScreenTab createScreenTab()
	{
		screenTab = new ScreenTab(this);
		return screenTab;
	}
	
	private UploadTab createUploadTab()
	{
		uploadTab = new UploadTab(this);
		return uploadTab;
	}

	private SettingsTab createSettingsTab()
	{
		settingsTab = new SettingsTab(this);
		return settingsTab;
	}

	private JPanel createStatusPanel()
	{
		statusPanel = new JPanel();
		return statusPanel;
	}

	private void addMediaToolBar()
	{
		getContentPane().add(createMediaToolBar(), MainController.getInstance().getSettings().getProperty(Settings.KEY_PLAYER_TOOLBAR_LOCATION));
	}

	private MediaToolBar createMediaToolBar()
	{
		mediaToolBar = new MediaToolBar(this);
		return mediaToolBar;
	}
}
