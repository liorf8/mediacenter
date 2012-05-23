package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Parameter;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;

public class StreamingCategoryPanel extends CategoryPanel
{
	private static final long	serialVersionUID	= -3936765287623870818L;

	private JComboBox<String>	cmbBxProtocol;
	private JSpinner			spnrDestinationPort;

	public StreamingCategoryPanel()
	{
		super("Streaming");
	}

	@Override
	public Map<String, Object> getValueComponents()
	{
		Map<String, Object> valueComponents = new HashMap<>(2);
		valueComponents.put(Settings.KEY_STREAMING_PROTOCOL, cmbBxProtocol);
		valueComponents.put(Settings.KEY_STREAMING_DESTINATION_PORT, spnrDestinationPort);
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
		
		// protocol
		p = settings.getParameter(Settings.KEY_STREAMING_PROTOCOL);
		cmbBxProtocol = new JComboBox<String>();
		cmbBxProtocol.addItem("http");
		cmbBxProtocol.addItem("rtp");
		cmbBxProtocol.addItem("rtsp");
		JLabel lblProtocol = new JLabel(p.getLabel());
		lblProtocol.setToolTipText(p.getDescription());
		lblProtocol.setLabelFor(cmbBxProtocol);
		
		// port
		p = settings.getParameter(Settings.KEY_STREAMING_DESTINATION_PORT);
		SpinnerModel spnrMdl = new SpinnerNumberModel(0, 0, 65535, 1);
		spnrDestinationPort = new JSpinner(spnrMdl);
		JLabel lblTDestinationPort = new JLabel(p.getLabel());
		lblTDestinationPort.setToolTipText(p.getDescription());
		lblTDestinationPort.setLabelFor(spnrDestinationPort);

		// Layout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblProtocol)
						.addComponent(lblTDestinationPort))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(cmbBxProtocol, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spnrDestinationPort, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblProtocol).addComponent(cmbBxProtocol))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblTDestinationPort).addComponent(spnrDestinationPort)));

		return settingsPanel;
	}

}
