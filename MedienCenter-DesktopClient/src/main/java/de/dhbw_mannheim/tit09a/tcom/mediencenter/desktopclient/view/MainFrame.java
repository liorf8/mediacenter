package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.LogoutAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.HomeTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.LoginTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.manage.ManageTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary.MediaLibraryTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.screen.ScreenTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings.SettingsTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class MainFrame extends JFrame
{
	private static final long	serialVersionUID	= -6050971688251747359L;

	private JMenuBar			menuBar;
	private JMenu				menuUser;
	private JMenuItem			menuItemLogout;

	private JPanel				mainPanel;
	private JTabbedPane			tabbedPane;
	private LoginTab			loginTab;
	private HomeTab				homeTab;
	private MediaLibraryTab		mediaLibraryTab;
	private ScreenTab			screenTab;
	private ManageTab			manageTab;
	private SettingsTab			settingsTab;

	private JPanel				statusPanel;
	private int					statusPanelCounter	= 0;

	private MediaToolBar		mediaToolBar;

	public MainFrame()
	{
		super(MainController.APP_NAME);

		List<Image> appImgs = new ArrayList<>();
		appImgs.add(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "MediaScrub Logo.png").getImage());
		appImgs.add(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_32x32 + "MediaScrub Logo.png").getImage());
		appImgs.add(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_64x64 + "MediaScrub Logo.png").getImage());
		setIconImages(appImgs);

		initGUI();
	}

	public void enterFullScreen(Container fullScreenContainer)
	{
		getContentPane().removeAll();
		getContentPane().add(fullScreenContainer, BorderLayout.CENTER);
		addMediaToolBar();
		validate();
	}

	public void exitFullScreen()
	{
		getContentPane().removeAll();
		addMediaToolBar();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		validate();
	}

	public MediaToolBar getMediaToolBar()
	{
		return mediaToolBar;
	}

	public LoginTab getLoginTab()
	{
		return loginTab;
	}
	
	public HomeTab getHomeTab()
	{
		return homeTab;
	}

	public MediaLibraryTab getMediaLibraryTab()
	{
		return mediaLibraryTab;
	}

	public ScreenTab getScreenTab()
	{
		return screenTab;
	}

	public ManageTab getManageTab()
	{
		return manageTab;
	}

	public void setLoggedIn(boolean loggedIn)
	{
		tabbedPane.removeAll();
		if (loggedIn)
		{
			// Menu
			menuItemLogout.setEnabled(true);

			// Tabs
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
			// Menu
			if (menuItemLogout != null)
				menuItemLogout.setEnabled(false);

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

		setJMenuBar(createJMenu());

		setLayout(new BorderLayout());
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);

		setLoggedIn(false);
	}

	private JMenuBar createJMenu()
	{
		menuBar = new JMenuBar();

		// User menu
		menuUser = new JMenu("User");
		menuUser.setMnemonic(KeyEvent.VK_U);
		menuUser.getAccessibleContext().setAccessibleDescription("User Menu");
		menuBar.add(menuUser);

		menuItemLogout = new JMenuItem("Logout", KeyEvent.VK_T);
		menuItemLogout.setAction(new LogoutAction(this));
		menuItemLogout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		menuItemLogout.getAccessibleContext().setAccessibleDescription("Log yourself out");
		menuUser.add(menuItemLogout);

		return menuBar;
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

	private ManageTab createUploadTab()
	{
		manageTab = new ManageTab(this);
		return manageTab;
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
