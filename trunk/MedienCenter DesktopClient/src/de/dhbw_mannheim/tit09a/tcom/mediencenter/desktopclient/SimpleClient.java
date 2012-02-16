package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ReturnObj;
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class SimpleClient
{
	private static Server				server;
	private static Session				session;
	private static Lookup				nameLookup;
	private static ClientCallbackImpl	callback;
	public JFrame						frame;
	private JTextField					txtFldArgs;
	private JComboBox					comboBox;
	private JButton						btnAction;
	private JTextArea					logArea;
	private JButton						btnLogout;
	private JLabel						lblInfo;

	/**
	 * Launch the application.
	 * 
	 * @throws UnknownHostException
	 * @throws EstablishConnectionFailed
	 * @throws LookupFailedException
	 */
	public static void main(String[] args) throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		String serverIP = Server.IP;
		if (args.length > 0)
		{
			System.out.println("args[0]=" + args[0]);
			serverIP = args[0];
		}

		nameLookup = Simon.createNameLookup(serverIP, Server.REGISTRY_PORT);
		server = (Server) nameLookup.lookup(Server.BIND_NAME);
		nameLookup.addClosedListener(server, new ClosedListener()
		{
			public void closed()
			{
				System.out.println("Closed!");
			}
		});
		callback = new ClientCallbackImpl(nameLookup, server);

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SimpleClient window = new SimpleClient();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public SimpleClient()
	{
		initialize();
	}

	public JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);

		logArea = new JTextArea();
		scrollPane.setViewportView(logArea);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				// and finally 'release' the serverobject to release to connection to the server
				if (nameLookup != null && server != null)
				{
					nameLookup.release(server);
					server = null;
				}
				else
					System.err.println("lookup or server == null");
				System.exit(0);
			}
		});

		initComboBox(panel);
		initButton(panel);

		txtFldArgs = new JTextField();
		panel.add(txtFldArgs);
		txtFldArgs.setColumns(10);

		btnLogout = new JButton("Logout");
		btnLogout.setEnabled(false);
		btnLogout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (nameLookup != null && server != null)
				{
					nameLookup.release(server);
					server = null;
					session = null;
					btnLogout.setEnabled(false);
				}
				else
					System.err.println("lookup or server == null");
			}
		});
		panel.add(btnLogout);

		lblInfo = new JLabel("Info");
		frame.getContentPane().add(lblInfo, BorderLayout.SOUTH);
	}

	private void initComboBox(JPanel panel)
	{
		final Method[] sessionMethods = Session.class.getMethods();
		final Method[] serverMethods = Server.class.getMethods();
		List<String> methodNames = new ArrayList<String>(sessionMethods.length + serverMethods.length);
		for (Method method : sessionMethods)
		{
			methodNames.add("Session." + method.getName());
		}
		for (Method method : serverMethods)
		{
			methodNames.add("Server." + method.getName());
		}
		Collections.sort(methodNames);

		comboBox = new JComboBox();
		for (String methodName : methodNames)
		{
			comboBox.addItem(methodName);
		}

		comboBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				String item = (String) e.getItem();
				if (item.startsWith("Session"))
				{
					for (Method oneMethod : sessionMethods)
					{
						if (item.endsWith(oneMethod.getName()))
						{
							Class<?>[] clazzes = oneMethod.getParameterTypes();
							String[] clazzNames = new String[clazzes.length];
							int i = 0;
							for (Class<?> oneClazz : clazzes)
							{
								clazzNames[i++] = oneClazz.getSimpleName();
							}
							lblInfo.setText(Arrays.toString(clazzNames) + " -> " + oneMethod.getReturnType().getSimpleName());
						}
					}
				}
				if (item.startsWith("Server"))
				{
					for (Method oneMethod : serverMethods)
					{
						if (item.endsWith(oneMethod.getName()))
						{
							Class<?>[] clazzes = oneMethod.getParameterTypes();
							String[] clazzNames = new String[clazzes.length];
							int i = 0;
							for (Class<?> oneClazz : clazzes)
							{
								clazzNames[i++] = oneClazz.getSimpleName();
							}
							lblInfo.setText(Arrays.toString(clazzNames) + " -> " + oneMethod.getReturnType().getSimpleName());
						}
					}
				}

			}
		});

		panel.add(comboBox);

	}

	private void initButton(JPanel panel)
	{
		btnAction = new JButton("action");
		btnAction.addActionListener(new ActionListener()
		{
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String method = (String) comboBox.getSelectedItem();
					String[] args = txtFldArgs.getText().split(",");
					logArea.append("Method: " + method + "\n");
					logArea.append("Args: " + Arrays.toString(args) + "\n");
					if (server == null)
					{
						server = (Server) nameLookup.lookup(Server.BIND_NAME);
					}
					ReturnObj<?> returnValue;
					if (method.startsWith("Session"))
					{

						if (session == null)
						{
							returnValue = new ReturnObj<Void>(ReturnObj.BAD_REQUEST, "LOGIN FIRST");
						}
						else
						{
							if (method.equals("Session.copyFile"))
							{
								returnValue = session.copyFile(args[0], args[1], Boolean.parseBoolean(args[2]));
							}
							else if (method.equals("Session.deleteFile"))
							{
								returnValue = session.deleteFile(args[0]);
							}
							else if (method.equals("Session.mkDir"))
							{
								returnValue = session.mkDir(args[0], args[1]);
							}
							else if (method.equals("Session.renameFile"))
							{
								returnValue = session.renameFile(args[0], args[1]);
							}
							else if (method.equals("Session.listFileInfos"))
							{
								returnValue = session.listFileInfos(args[0]);
							}
							else if (method.equals("Session.changePw"))
							{
								returnValue = session.changePw(args[0], args[1]);
							}
							else if (method.equals("Session.changeLogin"))
							{
								returnValue = session.changeLogin(args[0], args[1]);
							}
							else
							{
								returnValue = new ReturnObj<Void>(ReturnObj.NOT_FOUND, "Method not supported!");
							}
						}
					}
					else
					{
						if (method.equals("Server.serverTime"))
						{
							returnValue = server.serverTime();
						}
						else if (method.equals("Server.login"))
						{
							// TODO: blabla
							if (/* session == null */true)
							{
								Session tmp = server.login(args[0], args[1], callback);
								returnValue = new ReturnObj<Session>(tmp);
								if (tmp == null)
								{
									logArea.append("Login not successfull!\n");
								}
								else
								{
									session = tmp;
									btnLogout.setEnabled(true);
								}
							}
							else
							{
								returnValue = new ReturnObj<Session>(ReturnObj.BAD_REQUEST);
								logArea.append("ALREADY LOGGED IN!");
							}
						}
						else if (method.equals("Server.register"))
						{
							returnValue = server.register(args[0], args[1]);
						}
						else
						{
							returnValue = new ReturnObj<Void>(ReturnObj.NOT_FOUND, "Method not supported!");;
						}
					}
					logArea.append("Result: "+returnValue.get() + " (" +returnValue.code()+": "+returnValue.message()+")\n\n");
				}

				catch (ArrayIndexOutOfBoundsException aiobe)
				{
					logArea.append("NOT ENOUGH ARGS!\n");
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnAction);

	}
}
