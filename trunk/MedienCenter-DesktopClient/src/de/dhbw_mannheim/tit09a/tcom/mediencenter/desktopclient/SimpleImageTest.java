package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.remoteimage.RemoteImageComponent;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;

public class SimpleImageTest extends JFrame
{
	private static final long				serialVersionUID	= -5833483778115580387L;

	private static Lookup					nameLookup;
	private static Server					server;
	private static Session					session;
	private static JFrame					frame;
	private static DefaultClientCallback	callback;

	private RemoteImageComponent			imgCmp;
	private JTextField						txtFldPath;

	public SimpleImageTest() throws HeadlessException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException
	{
		super(SimpleImageTest.class.getSimpleName());
		buildUI();
		setVisible(true);
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
				exit();
			}
		});

		this.getContentPane().setLayout(new BorderLayout());

		imgCmp = new RemoteImageComponent();
		getContentPane().add(imgCmp, BorderLayout.CENTER);

		txtFldPath = new JTextField();
		txtFldPath.setColumns(10);

		JButton btnShow = new JButton(new AbstractAction("Show")
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				imgCmp.displayImg(session, txtFldPath.getText());
			}
		});

		JCheckBox ckBxScaleToFit = new JCheckBox(new AbstractAction("Scale to fit")
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				JCheckBox src = (JCheckBox) e.getSource();
				imgCmp.setScaleToFit(src.isSelected());
			}
		});

		JPanel southPanel = new JPanel();
		southPanel.add(txtFldPath);
		southPanel.add(btnShow);
		southPanel.add(ckBxScaleToFit);
		
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(btnShow);
		
	}

	public static void main(String[] args)
	{
		try
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

			callback = new DefaultClientCallback(frame);
			// server.register("Max", "pw");
			session = (Session) server.login("Max", "pw", callback);

			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						frame = new SimpleImageTest();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						exit();
					}
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exit();
		}
	}

	// --------------------------------------------------------------------------------
	private static void exit()
	{
		// and finally 'release' the serverobject to release to connection to the server
		if (nameLookup != null && server != null)
		{
			nameLookup.release(server);
			server = null;
		}
		else
			System.err.println("lookup or server == null: " + nameLookup + ", " + server);

		System.exit(0);
	}
}
