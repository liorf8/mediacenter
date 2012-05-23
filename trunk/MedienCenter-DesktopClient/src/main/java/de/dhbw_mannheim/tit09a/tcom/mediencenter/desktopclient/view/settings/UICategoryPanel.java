package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.util.Collections;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UICategoryPanel extends CategoryPanel
{
	private static final long	serialVersionUID	= -7973156148110049903L;

	public UICategoryPanel()
	{
		super("User Interface");
	}

	@Override
	protected JPanel createCategoryPanel()
	{
		JPanel settingsPanel = new JPanel();

		settingsPanel.add(new JLabel("user interface settings"));

		return settingsPanel;

	}

	@Override
	public Map<String, Object> getValueComponents()
	{
		return Collections.emptyMap();
	}

}
