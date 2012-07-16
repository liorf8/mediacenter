package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home;

import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.ChangeLoginAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.ChangePwAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class HomeTab extends Tab
{
	private static final long	serialVersionUID	= 1L;

	private JPanel				aboutPanel;

	private JPanel				resetPanel;
	private JTextField			txtFldNewLogin;
	private JPasswordField		pwFldCurrentPw1;
	private JButton				btnChangeLogin;

	private JPasswordField		pwFldCurrentPw2;
	private JPasswordField		pwFldNewPw;
	private JButton				btnChangePw;

	public HomeTab(MainFrame mainFrame)
	{
		super(mainFrame, "Home");
		initGUI();
	}

	@Override
	public String getTip()
	{
		return "The home";
	}

	@Override
	public Icon getIcon()
	{
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Home Tab.png");
	}

	public String getCurrentPw1()
	{
		return new String(pwFldCurrentPw1.getPassword());
	}

	public String getNewLogin()
	{
		return txtFldNewLogin.getText();
	}

	public String getCurrentPw2()
	{
		return new String(pwFldCurrentPw2.getPassword());
	}

	public String getNewPw()
	{
		return new String(pwFldNewPw.getPassword());
	}

	private void initGUI()
	{
		createAboutPanel();
		createResetPanel();

		// Layout MAIN
		GroupLayout mainGrpLayout = new GroupLayout(this);
		this.setLayout(mainGrpLayout);

		mainGrpLayout.setHorizontalGroup(mainGrpLayout.createParallelGroup().addComponent(aboutPanel).addComponent(resetPanel));

		mainGrpLayout.setVerticalGroup(mainGrpLayout.createSequentialGroup().addComponent(aboutPanel).addComponent(resetPanel));
	}

	private JPanel createAboutPanel()
	{
		aboutPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		JLabel lblAbout = new JLabel();
		lblAbout.setText(MainController.APP_NAME + " " + MainController.APP_VERSION);
		lblAbout.setIcon(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_64x64 + "MediaScrub Logo.png"));
		aboutPanel.add(lblAbout);
		
		return aboutPanel;
	}

	private JPanel createResetPanel()
	{
		resetPanel = new JPanel();

		GroupLayout grpLayout = new GroupLayout(resetPanel);
		resetPanel.setLayout(grpLayout);

		grpLayout.setHonorsVisibility(false);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		// Change Login
		JLabel lblChangeLogin = new JLabel("Change login");
		pwFldCurrentPw1 = new JPasswordField();
		JLabel lblCurrentPw1 = new JLabel("Current password");
		lblCurrentPw1.setToolTipText("Your current password");
		lblCurrentPw1.setLabelFor(pwFldCurrentPw1);

		JLabel lblNewLogin = new JLabel("New login");
		txtFldNewLogin = new JTextField();
		lblNewLogin.setToolTipText("The new login name");
		lblNewLogin.setLabelFor(txtFldNewLogin);
		btnChangeLogin = new JButton("Change login");
		btnChangeLogin.setAction(new ChangeLoginAction(mainFrame));

		// Change pw
		JLabel lblChangePw = new JLabel("Change password");
		pwFldCurrentPw2 = new JPasswordField();
		JLabel lblCurrentPw2 = new JLabel("Current password");
		lblCurrentPw2.setToolTipText("Your current password");
		lblCurrentPw2.setLabelFor(pwFldCurrentPw1);

		JLabel lblNewPw = new JLabel("New password");
		pwFldNewPw = new JPasswordField();
		lblNewPw.setToolTipText("The new password");
		lblNewPw.setLabelFor(pwFldNewPw);
		btnChangePw = new JButton("Change password");
		btnChangePw.setAction(new ChangePwAction(mainFrame));

		/*
		 * Change Login
		 * 
		 * Current pw: [ ]
		 * 
		 * New login: [ ]
		 * 
		 * [change]
		 * 
		 * 
		 * Change pw
		 * 
		 * Current pw: [ ]
		 * 
		 * New Pw: [ ]
		 * 
		 * [change]
		 */

		// Layout
		grpLayout.setHorizontalGroup(grpLayout.createSequentialGroup()
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblChangeLogin)
						.addComponent(lblCurrentPw1)
						.addComponent(lblNewLogin)
						.addGap(20)
						.addComponent(lblChangePw)
						.addComponent(lblCurrentPw2)
						.addComponent(lblNewPw))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pwFldCurrentPw1, 200, 200, 200)
						.addComponent(txtFldNewLogin, 200, 200, 200)
						.addComponent(btnChangeLogin)
						.addGap(20)
						.addComponent(pwFldCurrentPw2, 200, 200, 200)
						.addComponent(pwFldNewPw, 200, 200, 200)
						.addComponent(btnChangePw)));

		grpLayout.setVerticalGroup(grpLayout.createSequentialGroup()
				.addComponent(lblChangeLogin)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCurrentPw1).addComponent(pwFldCurrentPw1))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblNewLogin).addComponent(txtFldNewLogin))
				.addComponent(btnChangeLogin)
				.addPreferredGap(ComponentPlacement.UNRELATED, 20, 20)
				.addComponent(lblChangePw)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCurrentPw2).addComponent(pwFldCurrentPw2))
				.addGroup(grpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblNewPw).addComponent(pwFldNewPw))
				.addComponent(btnChangePw));

		return resetPanel;
	}
}
