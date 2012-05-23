package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.remoteimage;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SimpleImageTest extends JFrame
{
	private static final long		serialVersionUID	= -5833483778115580387L;

	private ImgController				controller;

	private RemoteImageComponent	ric;
	private JTextField				txtFldPath;
	private JCheckBox				ckBxScaleToFit;

	public SimpleImageTest(ImgController controller) throws HeadlessException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException
	{
		super(SimpleImageTest.class.getSimpleName());

		this.controller = controller;

		buildUI();
	}

	private void buildUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		setSize(400, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				controller.exit(null);
			}
		});

		this.getContentPane().setLayout(new BorderLayout());

		ric = new RemoteImageComponent(this);
		getContentPane().add(ric, BorderLayout.CENTER);

		txtFldPath = new JTextField();
		txtFldPath.setColumns(10);

		JButton btnShow = new JButton(new AbstractAction("Show")
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.displayImg();
			}
		});

		ckBxScaleToFit = new JCheckBox(new AbstractAction("Scale to fit")
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.setScaleToFit();
			}
		});

		JPanel southPanel = new JPanel();
		southPanel.add(txtFldPath);
		southPanel.add(btnShow);
		southPanel.add(ckBxScaleToFit);

		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
	}

	public RemoteImageComponent getRIC()
	{
		return ric;
	}

	public String getTxtFldPathText()
	{
		return txtFldPath.getText();
	}

	public ImgController getController()
	{
		return controller;
	}

	public boolean getCkBxScaleToFitIsSelected()
	{
		return ckBxScaleToFit.isSelected();
	}

}
