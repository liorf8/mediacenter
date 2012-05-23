package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import javax.swing.Icon;
import javax.swing.JPanel;

public abstract class Tab extends JPanel
{
	private static final long	serialVersionUID	= 1L;

	
	public Tab(String tabName)
	{
		setName(tabName);
	}
	
	public abstract String getTip();
	
	public abstract Icon getIcon();
}
