package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Parameter;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Settings;


public class ServerCategoryPanel extends CategoryPanel
{
	private static final long	serialVersionUID	= -4046506864656234204L;

	private JTextField			txtFldSvrIP;
	private JSpinner			spnrSvrRegPort;
	private JTextField			txtFldSvrBindname;

	public ServerCategoryPanel()
	{
		super("Server");
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
		
		// Server IP address
		p = settings.getParameter(Settings.KEY_SERVER_HOST);
		txtFldSvrIP = new JTextField();
		JLabel lblSvrIP = new JLabel(p.getLabel());
		lblSvrIP.setToolTipText(p.getDescription());
		lblSvrIP.setLabelFor(txtFldSvrIP);

		// PW Server port
		p = settings.getParameter(Settings.KEY_SERVER_REGISTRY_PORT);
		SpinnerModel spnrMdl = new SpinnerNumberModel(0, 0, 65535, 1);
		spnrSvrRegPort = new JSpinner(spnrMdl);
		JLabel lblSvrRegPort = new JLabel(p.getLabel());
		lblSvrRegPort.setToolTipText(p.getDescription());
		lblSvrRegPort.setLabelFor(spnrSvrRegPort);

		// Bindname
		p = settings.getParameter(Settings.KEY_SERVER_BINDNAME);
		txtFldSvrBindname = new JTextField();
		JLabel lblSvrBindname = new JLabel(p.getLabel());
		lblSvrBindname.setToolTipText(p.getDescription());
		lblSvrBindname.setLabelFor(txtFldSvrBindname);

		// Layout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblSvrIP)
						.addComponent(lblSvrRegPort)
						.addComponent(lblSvrBindname))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(txtFldSvrIP)
						.addComponent(spnrSvrRegPort, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtFldSvrBindname)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblSvrIP).addComponent(txtFldSvrIP))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblSvrRegPort).addComponent(spnrSvrRegPort))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblSvrBindname)
						.addComponent(txtFldSvrBindname)));
		
		return settingsPanel;
	}

	@Override
	public Map<String, Object> getValueComponents()
	{
		Map<String, Object> valueComponents = new HashMap<>(3);
		valueComponents.put(Settings.KEY_SERVER_HOST, txtFldSvrIP);
		valueComponents.put(Settings.KEY_SERVER_REGISTRY_PORT, spnrSvrRegPort);
		valueComponents.put(Settings.KEY_SERVER_BINDNAME, txtFldSvrBindname);
		return valueComponents;
	}

}
