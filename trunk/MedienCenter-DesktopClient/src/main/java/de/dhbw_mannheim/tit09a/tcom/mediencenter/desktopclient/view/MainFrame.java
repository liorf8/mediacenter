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

	public MainFrame()
	{
		super("MediaScrub");
		initGUI();
	}

	public LoginTab getLoginTab()
	{
		return loginTab;
	}
	
	public void setLoggedIn(boolean loggedIn)
	{
		if (loggedIn)
		{
			tabbedPane.removeAll();
			addTab(createHomeTab());
			addTab(createPlayTab());
			addTab(createUploadTab());
			addTab(createSettingsTab());

			getContentPane().add(createMediaToolBar(), MainController.getInstance().getSettings().getProperty(Settings.KEY_PLAYER_TOOLBAR_LOCATION));

			setSize(getPreferredSize());
		}
		else
		{
			tabbedPane.removeAll();
			addTab(createLoginTab());
			addTab(createSettingsTab());

			getContentPane().remove(createMediaToolBar());

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

	private PlayTab createPlayTab()
	{
		playTab = new PlayTab(this);
		return playTab;
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

	private MediaToolBar createMediaToolBar()
	{
		mediaToolBar = new MediaToolBar();
		return mediaToolBar;
	}
}
