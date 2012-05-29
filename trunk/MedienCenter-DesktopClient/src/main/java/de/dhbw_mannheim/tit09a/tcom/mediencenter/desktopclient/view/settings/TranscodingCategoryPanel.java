package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Parameter;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;

public class TranscodingCategoryPanel extends CategoryPanel
{
	private static final long	serialVersionUID	= 6501516259299516296L;

	private List<JComponent>	transcodingComponents;
	private JLabel				lblActive;
	private JCheckBox			ckBxActive;
	private JLabel				lblVideoCodec;
	private JComboBox<String>	cmbBxVideoCodec;
	private JLabel				lblVideoKBitRate;
	private JSpinner			spnrVideoKBitRate;
	private JLabel				lblAudioCodec;
	private JComboBox<String>	cmbBxAudioCodec;
	private JLabel				lblAudioKBitRate;
	private JSpinner			spnrAudioKBitRate;
	private JLabel				lblAudioSync;
	private JCheckBox			ckBxAudioSync;
	private JLabel				lblDeinterlace;
	private JCheckBox			ckBxDeinterlace;

	public TranscodingCategoryPanel()
	{
		super("Transcoding");
	}

	@Override
	public Map<String, Object> getValueComponents()
	{
		Map<String, Object> valueComponents = new HashMap<>(7);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_ACTIVE, ckBxActive);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_VIDEO_CODEC, cmbBxVideoCodec);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_AUDIO_CODEC, cmbBxAudioCodec);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_VIDEO_KBIT_RATE, spnrAudioKBitRate);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_AUDIO_KBIT_RATE, spnrVideoKBitRate);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_AUDIO_SYNC, ckBxAudioSync);
		valueComponents.put(Settings.KEY_STREAMING_TRANSCODING_DEINTERLACE, ckBxDeinterlace);
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

		// Active
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_ACTIVE);
		ckBxActive = new JCheckBox();
		ckBxActive.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				enabledDisableTranscodingComponentsEnabled();
			}
		});
		lblActive = new JLabel(p.getLabel());
		lblActive.setToolTipText(p.getDescription());
		lblActive.setLabelFor(ckBxActive);

		// video codec
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_VIDEO_CODEC);
		cmbBxVideoCodec = new JComboBox<String>();
		cmbBxVideoCodec.addItem("");
		cmbBxVideoCodec.addItem("mp1v");
		cmbBxVideoCodec.addItem("mp2v");
		cmbBxVideoCodec.addItem("QPEG");
		// dont ever use h264, it will crash the server
		lblVideoCodec = new JLabel(p.getLabel());
		lblVideoCodec.setToolTipText(p.getDescription());
		lblVideoCodec.setLabelFor(cmbBxVideoCodec);

		// video kbit rate
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_VIDEO_KBIT_RATE);
		SpinnerModel spnrMdl = new SpinnerNumberModel(1024, 1024, 64*1024, 1); // 3072
		spnrVideoKBitRate = new JSpinner(spnrMdl);
		lblVideoKBitRate = new JLabel(p.getLabel());
		lblVideoKBitRate.setToolTipText(p.getDescription());
		lblVideoKBitRate.setLabelFor(spnrVideoKBitRate);

		// audio codec
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_AUDIO_CODEC);
		cmbBxAudioCodec = new JComboBox<String>();
		cmbBxAudioCodec.addItem("");
		cmbBxAudioCodec.addItem("mpga");
		cmbBxAudioCodec.addItem("mp3");
		cmbBxAudioCodec.addItem("mp4a");
		cmbBxAudioCodec.addItem("a52");
		lblAudioCodec = new JLabel(p.getLabel());
		lblAudioCodec.setToolTipText(p.getDescription());
		lblAudioCodec.setLabelFor(cmbBxVideoCodec);

		// audio kbit rate
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_AUDIO_KBIT_RATE);
		spnrMdl = new SpinnerNumberModel(64, 64, 1024, 1);
		spnrAudioKBitRate = new JSpinner(spnrMdl);
		lblAudioKBitRate = new JLabel(p.getLabel());
		lblAudioKBitRate.setToolTipText(p.getDescription());
		lblAudioKBitRate.setLabelFor(spnrAudioKBitRate);

		// audio-sync
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_AUDIO_SYNC);
		ckBxAudioSync = new JCheckBox();
		lblAudioSync = new JLabel(p.getLabel());
		lblAudioSync.setToolTipText(p.getDescription());
		lblAudioSync.setLabelFor(ckBxAudioSync);

		// deinterlace
		p = settings.getParameter(Settings.KEY_STREAMING_TRANSCODING_DEINTERLACE);
		ckBxDeinterlace = new JCheckBox();
		lblDeinterlace = new JLabel(p.getLabel());
		lblDeinterlace.setToolTipText(p.getDescription());
		lblDeinterlace.setLabelFor(ckBxDeinterlace);

		
		// add the components to enabled / disable, depending whether the transcoding active checkbox is checked or not
		transcodingComponents = new ArrayList<>(12);
		transcodingComponents.add(lblVideoCodec);
		transcodingComponents.add(cmbBxVideoCodec);
		transcodingComponents.add(lblVideoKBitRate);
		transcodingComponents.add(spnrVideoKBitRate);
		transcodingComponents.add(lblAudioCodec);
		transcodingComponents.add(cmbBxAudioCodec);
		transcodingComponents.add(lblAudioKBitRate);
		transcodingComponents.add(spnrAudioKBitRate);
		transcodingComponents.add(lblAudioSync);
		transcodingComponents.add(ckBxAudioSync);
		transcodingComponents.add(lblDeinterlace);
		transcodingComponents.add(ckBxDeinterlace);
		
		// initially set the components enabled or disabled
		enabledDisableTranscodingComponentsEnabled();

		// Layout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblActive)
						.addComponent(lblVideoCodec)
						.addComponent(lblVideoKBitRate)
						.addComponent(lblAudioCodec)
						.addComponent(lblAudioKBitRate)
						.addComponent(lblAudioSync)
						.addComponent(lblDeinterlace))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(ckBxActive)
						.addComponent(cmbBxVideoCodec, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spnrVideoKBitRate, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmbBxAudioCodec, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spnrAudioKBitRate, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(ckBxAudioSync)
						.addComponent(ckBxDeinterlace)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblActive).addComponent(ckBxActive))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblVideoCodec).addComponent(cmbBxVideoCodec))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblVideoKBitRate)
						.addComponent(spnrVideoKBitRate))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblAudioCodec).addComponent(cmbBxAudioCodec))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblAudioKBitRate)
						.addComponent(spnrAudioKBitRate))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblAudioSync).addComponent(ckBxAudioSync))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDeinterlace).addComponent(ckBxDeinterlace)));

		return settingsPanel;
	}
	
	private void enabledDisableTranscodingComponentsEnabled()
	{
		boolean enabled = ckBxActive.isSelected();
		for(JComponent comp : transcodingComponents)
		{
			comp.setEnabled(enabled);
		}
	}
}
