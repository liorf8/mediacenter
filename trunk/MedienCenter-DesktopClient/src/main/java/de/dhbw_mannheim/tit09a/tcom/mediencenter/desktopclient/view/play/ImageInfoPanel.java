package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import java.awt.Color;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import uk.co.caprica.vlcj.player.VideoTrackInfo;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.ImageComponent;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class ImageInfoPanel extends MediaInfoPanel
{
	private static final long	serialVersionUID	= -7988253117412089976L;

	private GroupLayout			grpLayout;

	private ImageComponent		imgComp;
	private JLabel				lblCodecNameKey;
	private JLabel				lblDimensionsKey;
	private JLabel				lblCodecNameValue;
	private JLabel				lblDimensionsValue;

	public ImageInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);

		initComponents();
		applyLayout();
	}

	public void setImage(Image img)
	{
		imgComp.setImage(img);
	}
	
	public void setTechnicals(VideoTrackInfo vti)
	{
		if(vti != null)
		{
			lblCodecNameValue.setText(vti.codecName());
			lblDimensionsValue.setText(vti.width() + "x" + vti.height());
		}
	}
	
	@Override
	public void reset()
	{
		imgComp.setImage(null);
		lblCodecNameValue.setText("");
		lblDimensionsValue.setText("");
	}

	private void initComponents()
	{
		// img
		imgComp = new ImageComponent(null);
		imgComp.setDoScale(true);
		imgComp.setOnlyScaleIfImgIsBigger(true);

		// codecName
		lblCodecNameKey = new JLabel("Codec name:");
		lblCodecNameValue = new JLabel("-");

		// dimensions
		lblDimensionsKey = new JLabel("Dimensions:");
		lblDimensionsValue = new JLabel("-");
	}
	
	private void applyLayout()
	{
		// init layout
		grpLayout = new GroupLayout(this);
		setLayout(grpLayout);
		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		// Layout
		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup().addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(imgComp)
				.addGroup(grpLayout.createSequentialGroup()
						.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblCodecNameKey)
								.addComponent(lblDimensionsKey))
						.addPreferredGap(ComponentPlacement.RELATED, 20, 20)
						.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblCodecNameValue)
								.addComponent(lblDimensionsValue)))));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addComponent(imgComp)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCodecNameKey).addComponent(lblCodecNameValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDimensionsKey)
						.addComponent(lblDimensionsValue)));
	}
}
