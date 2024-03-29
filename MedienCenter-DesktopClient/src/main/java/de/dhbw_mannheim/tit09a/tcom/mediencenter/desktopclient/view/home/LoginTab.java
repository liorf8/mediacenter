package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home;

import javax.swing.AbstractButton;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.LoginAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.RegisterAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.ResetPwAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class LoginTab extends Tab
{
	private static final long	serialVersionUID	= 7931204119657376349L;

	private final MainFrame mainFrame;
	
	// North Panel
	private JButton				btnLogin;
	private JButton				btnResetPw;
	private JButton				btnRegister;

	// Center Panel
	private JLabel				lblLogin;
	private JTextField			txtFldLogin;
	private JLabel				lblPw;
	private JPasswordField		pwFld;
	private JLabel				lblPwRepeat;
	private JPasswordField		pwFldRepeat;
	private JButton				btnAction;
	private LoginAction			loginAction;
	private RegisterAction		registerAction;
	private ResetPwAction		resetPwAction;

	public LoginTab(MainFrame mainFrame)
	{
		super(mainFrame, "Login");
		this.mainFrame = mainFrame;
		initGUI();

		setLogin();
	}
	
	@Override
	public String getTip()
	{
		return "Log yourself in";
	}

	@Override
	public Icon getIcon()
	{
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Home Tab.png");
	}

	public void requestFocusForLogin()
	{
		txtFldLogin.requestFocusInWindow();
	}

	public String getLoginText()
	{
		return txtFldLogin.getText();
	}

	public char[] getPwChars()
	{
		return pwFld.getPassword();
	}

	public char[] getPwRepeatChars()
	{
		return pwFldRepeat.getPassword();
	}
	
	private void initGUI()
	{
		setLayout(new BorderLayout());
		add(createNorthPanel(), BorderLayout.NORTH);
		add(createCenterPanel(), BorderLayout.CENTER);

		loginAction = new LoginAction(mainFrame);
		registerAction = new RegisterAction(mainFrame);
		resetPwAction = new ResetPwAction(mainFrame);
		
		mainFrame.getRootPane().setDefaultButton(btnAction);
	}

	private JPanel createNorthPanel()
	{
		JPanel northPanel = new JPanel();

		btnLogin = new JButton("Login...");
		btnLogin.setIcon(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Login.png"));
		btnLogin.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setLogin();
			}
		});
		northPanel.add(btnLogin);

		btnRegister = new JButton("Register...");
		btnRegister.setIcon(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Register.png"));
		btnRegister.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setRegister();
			}
		});
		northPanel.add(btnRegister);

		btnResetPw = new JButton("Reset Password...");
		btnResetPw.setIcon(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Reset Password.png"));
		btnResetPw.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setResetPw();
			}
		});
		northPanel.add(btnResetPw);

		return northPanel;
	}

	private JPanel createCenterPanel()
	{
		JPanel centerPanel = new JPanel();

		GroupLayout groupLayout = new GroupLayout(centerPanel);
		centerPanel.setLayout(groupLayout);

		groupLayout.setHonorsVisibility(false);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		// Login TextField
		txtFldLogin = new JTextField();
		lblLogin = new JLabel("Login");
		lblLogin.setLabelFor(txtFldLogin);

		// PW TextField
		pwFld = new JPasswordField();
		lblPw = new JLabel("Password");
		lblPw.setLabelFor(pwFld);

		// PW TextField
		pwFldRepeat = new JPasswordField();
		lblPwRepeat = new JLabel("Repeat Password");
		lblPwRepeat.setLabelFor(pwFldRepeat);

		// Login Button
		btnAction = new JButton();

		// Layout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblLogin)
						.addComponent(lblPw)
						.addComponent(lblPwRepeat))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, 20)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(txtFldLogin)
						.addComponent(pwFld)
						.addComponent(pwFldRepeat)
						.addComponent(btnAction)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblLogin).addComponent(txtFldLogin))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblPw).addComponent(pwFld))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblPwRepeat).addComponent(pwFldRepeat))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnAction)));

		return centerPanel;
	}

	// North Panel
	private void setLogin()
	{
		removeActionListeners(btnAction);

		btnLogin.setEnabled(false);
		btnRegister.setEnabled(true);
		btnResetPw.setEnabled(true);

		// lblLogin.setVisible(true); is visible anyway
		lblPw.setVisible(true);
		pwFld.setVisible(true);
		lblPwRepeat.setVisible(false);
		pwFldRepeat.setVisible(false);

		// Alternatively instead of always resetting the Buttons properties,
		// you could create three Buttons and let the GroupLayout replace() them for each other.
		btnAction.setAction(loginAction);
	}

	private void setRegister()
	{
		removeActionListeners(btnAction);

		btnLogin.setEnabled(true);
		btnRegister.setEnabled(false);
		btnResetPw.setEnabled(true);

		// lblLogin.setVisible(true); is visible anyway
		lblPw.setVisible(true);
		pwFld.setVisible(true);
		lblPwRepeat.setVisible(true);
		pwFldRepeat.setVisible(true);

		// centerPanel.setBorder(BorderFactory.createTitledBorder("Register"));
		btnAction.setAction(registerAction);
	}

	private void setResetPw()
	{
		removeActionListeners(btnAction);

		btnLogin.setEnabled(true);
		btnRegister.setEnabled(true);
		btnResetPw.setEnabled(false);

		// lblLogin.setVisible(true); is visible anyway
		lblPw.setVisible(false);
		pwFld.setVisible(false);
		lblPwRepeat.setVisible(false);
		pwFldRepeat.setVisible(false);

		// centerPanel.setBorder(BorderFactory.createTitledBorder("Reset Password"));
		btnAction.setAction(resetPwAction);
	}

	private void removeActionListeners(AbstractButton btn)
	{
		for (ActionListener l : btn.getActionListeners())
			btn.removeActionListener(l);
	}
}
