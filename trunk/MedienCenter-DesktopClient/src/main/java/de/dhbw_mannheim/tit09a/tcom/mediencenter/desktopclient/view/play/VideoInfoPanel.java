package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.ImageComponent;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class VideoInfoPanel extends MediaInfoPanel
{
	private static final long	serialVersionUID	= 891239762411423316L;

	private JPanel				leftPanel;
	private GroupLayout			leftGrpLayout;
	private ImageComponent		coverImgComp;
	private JButton				btnPlay;
	private JButton				btnPlayFromPosition;
	private JLabel				lblPosition;

	private JPanel				rightPanel;
	private GroupLayout			rightGrpLayout;
	private JLabel				lblLengthKey;
	private JLabel				lblLengthValue;
	private JLabel				lblVideoCodecKey;
	private JLabel				lblVideoCodecValue;
	private JLabel				lblDimensionsKey;
	private JLabel				lblDimensionsValue;
	private JLabel				lblAudioTracksKey;
	private JComboBox			lblAudioTracksValue;
	private JLabel				lblAudioCodecKey;
	private JLabel				lblAudioChannelsKey;

	public VideoInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);
		leftPanel = createLeftPanel();
		rightPanel = createRightPanel();

		GroupLayout mainGrpLayout = new GroupLayout(this);
		this.setLayout(mainGrpLayout);

		// Layout MAIN
		mainGrpLayout.setHorizontalGroup(mainGrpLayout.createSequentialGroup()
				.addComponent(leftPanel)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(rightPanel));

		mainGrpLayout.setVerticalGroup(mainGrpLayout.createSequentialGroup().addGroup(mainGrpLayout.createParallelGroup()
				.addComponent(leftPanel)
				.addComponent(rightPanel)));
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}
	private JPanel createLeftPanel()
	{
		leftPanel = new JPanel();

		// init layout
		leftGrpLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(leftGrpLayout);
		leftGrpLayout.setHonorsVisibility(true);
		leftGrpLayout.setAutoCreateGaps(true);
		leftGrpLayout.setAutoCreateContainerGaps(true);

		// img
		coverImgComp = new ImageComponent(null);
		coverImgComp.setDoScale(true);
		coverImgComp.setOnlyScaleIfImgIsBigger(true);
		coverImgComp.setMinimumSize(new Dimension(200, 200));
		coverImgComp.setPreferredSize(new Dimension(200, 200));
		coverImgComp.setMaximumSize(new Dimension(500, 500));

		// btns
		btnPlay = new JButton("Play");
		btnPlayFromPosition = new JButton("Play from Position");
		lblPosition = new JLabel("--:--:--");

		return leftPanel;
	}

	private JPanel createRightPanel()
	{
		rightPanel = new JPanel();

		// init layout
		rightGrpLayout = new GroupLayout(rightPanel);
		rightPanel.setLayout(rightGrpLayout);
		rightGrpLayout.setHonorsVisibility(false);
		rightGrpLayout.setAutoCreateGaps(true);
		rightGrpLayout.setAutoCreateContainerGaps(true);

		return rightPanel;
	}

	public void applyLayout()
	{

	}


}
