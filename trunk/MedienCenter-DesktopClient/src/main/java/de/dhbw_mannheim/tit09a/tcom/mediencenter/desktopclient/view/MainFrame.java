package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.MainFrameWindowListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.HomeTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.LoginTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play.PlayTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings.SettingsTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.upload.UploadTab;

public class MainFrame extends JFrame
{
	private static final long	serialVersionUID	= -6050971688251747359L;

	private MainController		mainController;

	private JPanel				mainPanel;
	private JTabbedPane			tabbedPane;
	private LoginTab			loginTab;
	private HomeTab				homeTab;
	private PlayTab				playTab;
	private UploadTab			uploadTab;
	private SettingsTab			settingsTab;

	private JPanel				statusPanel;
	private int					statusPanelCounter	= 0;

	private MediaToolBar		mediaToolBar;

	public MainFrame(MainController mainController)
	{
		super("MediaScrub");
		this.mainController = mainController;
		initGUI();
	}

	private void initGUI()
	{
		setLocation(400, 400);
		setPreferredSize(new Dimension(760, 480));
		setMinimumSize(new Dimension(500, 400));
		setSize(getMinimumSize());

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new MainFrameWindowListener(this));

		setLayout(new BorderLayout());
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);

		setLoggedIn(false);
	}

	public void setLoggedIn(boolean loggedIn)
	{
		if (loggedIn)
		{
			tabbedPane.removeAll();
			addTab(getHomeTab());
			addTab(getPlayTab());
			addTab(rebuildUploadTab());
			addTab(getSettingsTab());

			getContentPane().add(getMediaToolBar(), mainController.getSettings().getProperty(Settings.KEY_PLAYER_TOOLBAR_LOCATION));

			setSize(getPreferredSize());
		}
		else
		{
			tabbedPane.removeAll();
			addTab(getLoginTab());
			addTab(getSettingsTab());

			getContentPane().remove(getMediaToolBar());

			setSize(getMinimumSize());
		}
	}

	public JPanel createMainPanel()
	{
		if (mainPanel == null)
		{
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());

			tabbedPane = new JTabbedPane();
			mainPanel.add(tabbedPane, BorderLayout.CENTER);

			mainPanel.add(getStatusPanel(), BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	public LoginTab getLoginTab()
	{
		if (loginTab == null)
		{
			loginTab = new LoginTab(this);
		}
		loginTab.repaint();
		return loginTab;
	}

	public HomeTab getHomeTab()
	{
		if (homeTab == null)
		{
			homeTab = new HomeTab(this);
		}
		homeTab.repaint();
		return homeTab;
	}

	public PlayTab getPlayTab()
	{
		if (playTab == null)
		{
			playTab = new PlayTab(this);
		}
		playTab.repaint();
		return playTab;
	}

	public UploadTab rebuildUploadTab()
	{
		uploadTab = null;
		uploadTab = new UploadTab(this);
		return uploadTab;
	}

	public SettingsTab getSettingsTab()
	{
		if (settingsTab == null)
		{
			settingsTab = new SettingsTab(this);
		}
		return settingsTab;
	}

	public JPanel getStatusPanel()
	{
		if (statusPanel == null)
		{
			statusPanel = new JPanel();
		}
		return statusPanel;
	}

	public MediaToolBar getMediaToolBar()
	{
		if (mediaToolBar == null)
		{
			mediaToolBar = new MediaToolBar();
		}
		return mediaToolBar;
	}

	public synchronized void addTaskPanel(TaskPanel tp)
	{
		statusPanelCounter++;
		statusPanel.setLayout(new GridLayout(statusPanelCounter, 1));
		getStatusPanel().add(tp);
		getContentPane().validate(); // refresh the GUI
	}

	public synchronized void removeTaskPanel(TaskPanel tp)
	{
		statusPanelCounter--;
		statusPanel.setLayout(new GridLayout(statusPanelCounter, 1));
		getStatusPanel().remove(tp);
		getContentPane().validate();
	}

	private void addTab(Tab tab)
	{
		tabbedPane.addTab(tab.getName(), tab.getIcon(), tab, tab.getTip());
	}

}
