package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary;

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

	private ImageComponent		imgComp;
	private JLabel				lblCodecNameKey;
	private JLabel				lblDimensionsKey;
	private JLabel				lblCodecValue;
	private JLabel				lblDimensionsValue;

	public ImageInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);

		initComponents();
	}

	public void setImage(Image img)
	{
		imgComp.setImage(img);
	}

	public void setVideoTrackInfo(VideoTrackInfo vti)
	{
		lblCodecValue.setText(vti.codecName());
		lblDimensionsValue.setText(vti.width() + "x" + vti.height());
	}

	@Override
	public void reset()
	{
		imgComp.setImage(null);
		lblCodecValue.setText(null);
		lblDimensionsValue.setText(null);
	}

	private void initComponents()
	{
		// img
		imgComp = new ImageComponent(null);
		imgComp.setDoScale(true);
		imgComp.setOnlyScaleIfImgIsBigger(true);

		// codecName
		lblCodecNameKey = new JLabel("Codec name:");
		lblCodecValue = new JLabel();

		// dimensions
		lblDimensionsKey = new JLabel("Dimensions:");
		lblDimensionsValue = new JLabel();

		// Layout
		GroupLayout grpLayout = new GroupLayout(this);
		setLayout(grpLayout);
		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup().addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(imgComp)
				.addGroup(grpLayout.createSequentialGroup()
						.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblCodecNameKey)
								.addComponent(lblDimensionsKey))
						.addPreferredGap(ComponentPlacement.RELATED, 20, 20)
						.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblCodecValue)
								.addComponent(lblDimensionsValue)))));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addComponent(imgComp)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCodecNameKey).addComponent(lblCodecValue))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDimensionsKey)
						.addComponent(lblDimensionsValue)));
	}
}
