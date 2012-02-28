package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.FileSystemException;
import java.rmi.ServerException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.FileInfoTreeModel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.FileInfoTreeRenderer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;

public class FileTreeTest
{
	private static JLabel	info;
	private static Session	session;
	private static Lookup	nameLookup	= null;
	private static Server	server		= null;
	private static JFrame	frame;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{

		try
		{
			// 'lookup' the server object
			// 127.0.0.1
			nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
			server = (Server) nameLookup.lookup(Server.BIND_NAME);

			// create a callback object
			ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl(frame);

			// use the serverobject as it would exist on your local machine
			server.register("Donald Duck", "daisy");
			session = server.login("Donald Duck", "pw", clientCallbackImpl);

			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					frame = new JFrame();
					frame.setBounds(200, 100, 480, 320);
					frame.setTitle("FileTreeTest");
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					frame.addWindowListener(new WindowListener()
					{

						@Override
						public void windowActivated(WindowEvent arg0)
						{}

						@Override
						public void windowClosed(WindowEvent arg0)
						{}

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

						@Override
						public void windowDeactivated(WindowEvent arg0)
						{}

						@Override
						public void windowDeiconified(WindowEvent arg0)
						{

						}

						@Override
						public void windowIconified(WindowEvent arg0)
						{

						}

						@Override
						public void windowOpened(WindowEvent arg0)
						{

						}

					});
					frame.setLayout(new BorderLayout());

					// File homeDir = FileSystemView.getFileSystemView().getHomeDirectory();
					TreeModel model = new FileInfoTreeModel();
					JTree tree = new JTree(model);
					tree.setCellRenderer(new FileInfoTreeRenderer("Donald Duck"));
					tree.addTreeSelectionListener(new TreeSelectionListener()
					{
						@Override
						public void valueChanged(TreeSelectionEvent evt)
						{
							FileInfo fi = (FileInfo) evt.getPath().getLastPathComponent();
							FileTreeTest.setInfo(fi.getPath() + ": " + ByteValue.bytesToString(fi.getSize()) + ", "
									+ new Date(fi.getLastModified()));
						}
					});

					info = new JLabel("Gestartet");

					frame.add(new JScrollPane(tree), BorderLayout.CENTER);
					frame.add(info, BorderLayout.SOUTH);
					// frame.pack();
					frame.setVisible(true);
				}

			});
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			// and finally 'release' the serverobject to release to connection to the server
			// nameLookup.release(server);
		}

	}

	public static void setInfo(String text)
	{
		info.setText(text);
	}

	public static FileInfo[] getChildren(FileInfo parent) throws ServerException, IllegalArgumentException, FileSystemException
	{
		// System.out.println("Asking for Children of " +parent);
		return (FileInfo[]) session.listFileInfos(parent.getPath()).toArray();
	}
}
