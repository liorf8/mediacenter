package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Parameter;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Settings;

public class PlayerCategoryPanel extends CategoryPanel
{
	private static final long	serialVersionUID	= 279382229807157603L;

	private ButtonGroup			rdBtnsToolbarLocation;

	public PlayerCategoryPanel()
	{
		super("Player");
	}

	@Override
	public Map<String, Object> getValueComponents()
	{
		Map<String, Object> valueComponents = new HashMap<>(1);
		valueComponents.put(Settings.KEY_PLAYER_TOOLBAR_LOCATION, rdBtnsToolbarLocation);
		return valueComponents;
	}

	@Override
	protected JPanel createCategoryPanel()
	{
		JPanel settingsPanel = new JPanel();

		GroupLayout groupLayout = new GroupLayout(settingsPanel);
		settingsPanel.setLayout(groupLayout);

		groupLayout.setHonorsVisibility(false);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		Parameter<?> p;
		
		p = settings.getParameter(Settings.KEY_PLAYER_TOOLBAR_LOCATION);
		JLabel lblToolbarLocation = new JLabel(p.getLabel());
		lblToolbarLocation.setToolTipText(p.getDescription());

		rdBtnsToolbarLocation = new ButtonGroup();
		JRadioButton rdBtnNorth = new JRadioButton("North");
		rdBtnsToolbarLocation.add(rdBtnNorth);

		JRadioButton rdBtnSouth = new JRadioButton("South");
		rdBtnsToolbarLocation.add(rdBtnSouth);

		// Layout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(lblToolbarLocation)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addComponent(rdBtnNorth)
				.addComponent(rdBtnSouth));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(lblToolbarLocation)
				.addComponent(rdBtnNorth)
				.addComponent(rdBtnSouth)));

		return settingsPanel;
	}
}
