package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.MainFrameWindowListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.cfgs.ConfigPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.files.FilesPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.media.MediaPanel;

public class MainFrame extends JFrame
{
	private static final long	serialVersionUID	= -6050971688251747359L;

	private JTabbedPane			tabbedPane;
	private LoginPanel			loginPanel;
	private JPanel				statusPanel;
	private int					statusPanelCounter	= 0;

	public MainFrame()
	{
		super("MedienCenterApp");
		initGUI();
	}

	private void initGUI()
	{
		setLocation(400, 400);

		setPreferredSize(new Dimension(640, 480));
		setMinimumSize(new Dimension(360, 240));
		setSize(getMinimumSize());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new MainFrameWindowListener(this));
		setLayout(new BorderLayout());

		setLoggedIn(false);

		getContentPane().add(getStatusPanel(), BorderLayout.SOUTH);
	}

	public void setLoggedIn(boolean loggedIn)
	{
		// TODO not remove all, just the ones in the middle
		if (loggedIn)
		{
			if (loginPanel != null)
				getContentPane().remove(loginPanel);
			getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
			setSize(getPreferredSize());
		}
		else
		{
			if (tabbedPane != null)
				getContentPane().remove(tabbedPane);
			getContentPane().add(getLoginPanel(), BorderLayout.CENTER);
			setSize(getMinimumSize());
		}
	}

	public LoginPanel getLoginPanel()
	{
		if (loginPanel == null)
		{
			loginPanel = new LoginPanel(this);
		}
		return loginPanel;
	}

	public JTabbedPane getTabbedPane()
	{
		if (tabbedPane == null)
		{
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Media", new MediaPanel(this));
			tabbedPane.addTab("Files", new FilesPanel(this));
			tabbedPane.addTab("Config", new ConfigPanel(this));
		}
		return tabbedPane;
	}

	public JPanel getStatusPanel()
	{
		if (statusPanel == null)
		{
			statusPanel = new JPanel();
		}
		return statusPanel;
	}

	public synchronized void addTaskPanel(TaskPanel tp)
	{
		statusPanelCounter++;
		statusPanel.setLayout(new GridLayout(statusPanelCounter, 1));
		getStatusPanel().add(tp);
		getContentPane().validate();
	}

	public synchronized void removeTaskPanel(TaskPanel tp)
	{
		statusPanelCounter--;
		statusPanel.setLayout(new GridLayout(statusPanelCounter, 1));
		getStatusPanel().remove(tp);
		getContentPane().validate();
	}

}
