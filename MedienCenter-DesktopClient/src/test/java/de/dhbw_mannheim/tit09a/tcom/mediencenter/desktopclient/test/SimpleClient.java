package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test;

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
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
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
	private static DefaultClientCallback	callback;
	public static JFrame						frame;
	private JTextField					txtFldArgs;
	private JComboBox<String>			comboBox;
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
		callback = new DefaultClientCallback(frame);

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					new SimpleClient();
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
					session = null;
				}
				else
					System.err.println("lookup or server == null" + nameLookup + "," + server);
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
		frame.setVisible(true);
	}

	private void initComboBox(JPanel panel)
	{
		final Method[] sessionMethods = Session.class.getMethods();
		final Method[] serverMethods = Server.class.getMethods();
		List<String> methodNames = new ArrayList<>(sessionMethods.length + serverMethods.length);
		for (Method method : sessionMethods)
		{
			methodNames.add("Session." + method.getName());
		}
		for (Method method : serverMethods)
		{
			methodNames.add("Server." + method.getName());
		}
		Collections.sort(methodNames);

		comboBox = new JComboBox<String>();
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
				long start = System.currentTimeMillis();
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
					Object returnValue;
					if (method.startsWith("Session"))
					{
						if (session == null)
							returnValue = "LOGIN FIRST";
						else
						{
							switch (method)
							{
								case "Session.getId":
									returnValue = session.getId();
									break;
								case "Session.getLogin":
									returnValue = session.getLogin();
									break;
								case "Session.getSessionId":
									returnValue = session.getSessionId();
									break;
								case "Session.changeLogin":
									session.changeLogin(args[0], args[1]);
									returnValue = "void";
									break;
								case "Session.changePw":
									session.changePw(args[0], args[1]);
									returnValue = "void";
									break;
								case "Session.copyFile":
									returnValue = session.copyFile(args[0], args[1], Boolean.parseBoolean(args[2]));
									break;
								case "Session.deleteFile":
									returnValue = session.deleteFile(args[0], Boolean.parseBoolean(args[1]));
									break;
								case "Session.downloadFile":
									session.downloadFile(args[0]);
									returnValue = "void";
									break;
								case "Session.listFileInfos":
									returnValue = (session.listFileInfos(args[0]));
									break;
								case "Session.createDir":
									returnValue = session.createDir(args[0], args[1]);
									break;
								case "Session.moveFile":
									returnValue = session.moveFile(args[0], args[1], Boolean.parseBoolean(args[2]));
									break;
								case "Session.prepareRawChannel":
									returnValue = session.prepareRawChannel(args[0], args[1], Long.parseLong(args[2]));
									break;
								case "Session.renameFile":
									returnValue = session.renameFile(args[0], args[1]);
									break;
								default:
									returnValue = "Method not supported!";
							}
						}
					}
					else
					{
						switch (method)
						{
							case "Server.login":
								// TODO: blabla
								if (/* session == null */true)
								{
									Session tmp = server.login(args[0], args[1], callback);
									System.out.println(tmp.hashCode());
									if (tmp == null)
									{
										returnValue = "null";
										logArea.append("Login not successfull!\n");
									}
									else
									{
										returnValue = tmp.toString();
										session = tmp;
										btnLogout.setEnabled(true);
									}
								}
								else
								{
									returnValue = "ALREADY LOGGED IN!";
								}
								break;
							case "Server.register":
								returnValue = server.register(args[0], args[1]);
								break;
							case "Server.resetPw":
								server.resetPw(args[0]);
								returnValue = "void";
								break;
							case "Server.serverTime":
								returnValue = new Date(server.serverTime());
								break;

							default:
								returnValue = "Method not supported!";
						}
					}
					logArea.append("Result: " + returnValue.toString() + "\n");
				}

				catch (ArrayIndexOutOfBoundsException aiobe)
				{
					logArea.append("NOT ENOUGH ARGS!\n");
				}
				catch (Exception e1)
				{
					logArea.append(e1.getClass().getSimpleName() + ": " + e1.getMessage() + "\n");
					e1.printStackTrace();
				}
				finally
				{
					long duration = System.currentTimeMillis() - start;
					logArea.append(duration + "ms\n\n");
				}
			}
		});
		panel.add(btnAction);

	}
}
