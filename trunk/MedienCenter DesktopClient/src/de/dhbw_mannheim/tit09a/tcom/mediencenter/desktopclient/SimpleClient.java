package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class SimpleClient
{
    private static Server server;
    private static Session session;
    private static Lookup nameLookup;
    private static ClientCallbackImpl callback;

    public JFrame frame;
    private JTextField txtFldArgs;
    private JComboBox comboBox;
    private JButton btnAction;
    private JTextArea logArea;

    /**
     * Launch the application.
     * 
     * @throws UnknownHostException
     * @throws EstablishConnectionFailed
     * @throws LookupFailedException
     */
    public static void main(String[] args) throws UnknownHostException, LookupFailedException,
	    EstablishConnectionFailed
    {
	nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
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
	frame.setBounds(100, 100, 450, 300);
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
		    nameLookup.release(server);
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
    }

    private void initComboBox(JPanel panel)
    {
	Method[] sessionMethods = Session.class.getMethods();
	Method[] serverMethods = Server.class.getMethods();
	List<String> methodNames = new ArrayList<String>(sessionMethods.length
		+ serverMethods.length);
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

	panel.add(comboBox);

    }

    private void initButton(JPanel panel)
    {
	btnAction = new JButton("action");
	btnAction.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent e)
	    {
		try
		{
		    String method = (String) comboBox.getSelectedItem();
		    String[] args = txtFldArgs.getText().split(",");
		    logArea.append("Method: " + method + "\n");
		    logArea.append("Args: " + Arrays.toString(args) + "\n");
		    if (method.startsWith("Session"))
		    {
			if (session == null)
			{
			    logArea.append("LOGIN FIRST");
			}
			else
			{
			    if (method.endsWith("changeAttr"))
			    {
				session.changeAttr(args[0], args[1]);
			    }
			    else if (method.endsWith("copyFile"))
			    {
				session.copyFile(args[0], args[1], Boolean.parseBoolean(args[2]));
			    }
			    else if (method.endsWith("deleteFile"))
			    {
				session.deleteFile(args[0], Boolean.parseBoolean(args[1]));
			    }
			    else if (method.endsWith("getAttr"))
			    {
				logArea.append("Result: " + session.getAttr(args[0]));
			    }
			    else if (method.endsWith("getAttrs"))
			    {
				logArea.append("Result: " + session.getAttrs());
			    }
			    else if (method.endsWith("listFileInfos"))
			    {
				logArea.append("Result: " + session.listFileInfos(args[0]));
			    }
			    else if (method.endsWith("mkDir"))
			    {
				session.mkDir(args[0]);
			    }
			    else if (method.endsWith("renameFile"))
			    {
				session.renameFile(args[0], args[1]);
			    }
			    else if (method.endsWith(""))
			    {

			    }

			    else
			    {
				logArea.append("METHOD NOT SUPPORTED!");
			    }
			}
		    }
		    else
		    {
			if(server == null)
			{
			    server = (Server) nameLookup.lookup(Server.BIND_NAME);
			}
			if (method.endsWith("serverTime"))
			{
			    logArea.append("Result: " + server.serverTime() + "\n");
			}
			else if (method.endsWith("login"))
			{
			    if (/*session == null*/ true)
			    {
				logArea.append("Result: "
					+ (session = server.login(args[0], args[1], callback))
					+ "\n");
			    }
			    else
			    {
				logArea.append("ALREADY LOGGED IN!");
			    }

			}
			else if (method.endsWith("register"))
			{
			    server.register(args[0], args[1]);
			}
			else
			{
			    logArea.append("METHOD NOT SUPPORTED!");
			}
		    }
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
