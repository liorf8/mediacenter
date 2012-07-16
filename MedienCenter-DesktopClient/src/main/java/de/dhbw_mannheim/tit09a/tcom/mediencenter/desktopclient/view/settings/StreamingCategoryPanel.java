package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Parameter;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.Settings;

public class StreamingCategoryPanel extends CategoryPanel
{
	private static final long		serialVersionUID	= -3936765287623870818L;

	private JTextField				txtFldVlcInstallDir;
	private JComboBox<String>		cmbBxProtocol;
	private JSpinner				spnrDestinationPort;

	private StreamingCategoryPanel	thisInstance		= this;

	public StreamingCategoryPanel()
	{
		super("Streaming");
	}

	@Override
	public Map<String, Object> getValueComponents()
	{
		Map<String, Object> valueComponents = new HashMap<>(2);
		valueComponents.put(Settings.KEY_STREAMING_VLC_INSTALL_DIR, txtFldVlcInstallDir);
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

		// VM name
		JLabel lblVmKey = new JLabel("Current Java VM");
		String vmName = System.getProperty("java.vm.name");
		String architecture = (vmName.indexOf("64") != -1 ? "64-bit" : "32-bit");
		JLabel lblVmValue = new JLabel(vmName + "  (Choose a " + architecture + " VLC installation as well!)");

		// VLC installation directory
		p = settings.getParameter(Settings.KEY_STREAMING_VLC_INSTALL_DIR);
		txtFldVlcInstallDir = new JTextField();
		JLabel lblVlcInstallDir = new JLabel(p.getLabel());
		lblVlcInstallDir.setToolTipText(p.getDescription());
		lblVlcInstallDir.setLabelFor(txtFldVlcInstallDir);
		final JFileChooser vlcInstallDirChooser = new JFileChooser();
		vlcInstallDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final JButton btnChooseVlcInstallDir = new JButton("Select...");
		btnChooseVlcInstallDir.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				vlcInstallDirChooser.setCurrentDirectory(new File(txtFldVlcInstallDir.getText()));
				int returnVal = vlcInstallDirChooser.showOpenDialog(thisInstance);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					txtFldVlcInstallDir.setText(vlcInstallDirChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		// protocol
		p = settings.getParameter(Settings.KEY_STREAMING_PROTOCOL);
		cmbBxProtocol = new JComboBox<String>();
		cmbBxProtocol.addItem("http");
		cmbBxProtocol.addItem("udp");
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
						.addComponent(lblVmKey)
						.addComponent(lblVlcInstallDir)
						.addComponent(lblProtocol)
						.addComponent(lblTDestinationPort))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblVmValue)
						.addComponent(txtFldVlcInstallDir)
						.addComponent(cmbBxProtocol, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spnrDestinationPort, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(btnChooseVlcInstallDir));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblVmKey).addComponent(lblVmValue))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblVlcInstallDir)
						.addComponent(txtFldVlcInstallDir)
						.addComponent(btnChooseVlcInstallDir))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblProtocol).addComponent(cmbBxProtocol))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTDestinationPort)
						.addComponent(spnrDestinationPort)));

		return settingsPanel;
	}

}
