package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.MainFrameWindowListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.cfgs.ConfigPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.files.FilesPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.media.MediaPanel;

public class MainFrame extends JFrame
{
	private static final long	serialVersionUID	= -6050971688251747359L;

	// Controller
	private JTabbedPane	tabbedPane;
	private LoginPanel loginPanel;

	public MainFrame()
	{
		super("MedienCenterApp");
		initGUI();
	}

	private void initGUI()
	{
		setLocation(400, 400);
		
		setPreferredSize(new Dimension(640, 480));
		setMinimumSize(new Dimension(360, 220));
		setSize(getMinimumSize());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new MainFrameWindowListener(this));
		setLayout(new BorderLayout());
		
		setLoggedIn(false);
	}


	public void setLoggedIn(boolean loggedIn)
	{
		if(loggedIn)
		{
			System.out.println("logged in setting tabbedpane");
			getContentPane().removeAll();
			getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
			setSize(getPreferredSize());
		}
		else
		{
			getContentPane().removeAll();
			getContentPane().add(getLoginPanel(), BorderLayout.CENTER);
			setSize(getMinimumSize());
		}
	}
	

	public LoginPanel getLoginPanel()
	{
		if(loginPanel == null)
		{
			loginPanel = new LoginPanel(this);
		}
		return loginPanel;
	}
	
	public JTabbedPane getTabbedPane()
	{
		if(tabbedPane == null)
		{
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Media", new MediaPanel(this));
			tabbedPane.addTab("Files", new FilesPanel(this));
			tabbedPane.addTab("Config", new ConfigPanel(this));
		}
		return tabbedPane;
	}
	

	public void addTaskPanel(TaskPanel tp)
	{
		getContentPane().add(tp, BorderLayout.SOUTH);
	}
	
	public void removeTaskPanel(TaskPanel tp)
	{
		getContentPane().remove(tp);
	}

}
