package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;


import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;

import javax.swing.tree.TreeModel;


import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.FileInfoTreeModel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui.FileInfoTreeRenderer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;

public class FileTreeTest
{
    private static JLabel info;
    static Session session;
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
	Lookup nameLookup = null;
	LoginService server = null;
	try
	{
	    // create a callback object
	    ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl();

	    // 'lookup' the server object
	    // 127.0.0.1
	    nameLookup = Simon.createNameLookup("127.0.0.1", 22222);
	    server = (LoginService) nameLookup.lookup(LoginService.BIND_NAME);

	    // use the serverobject as it would exist on your local machine
	    session = server.login("Donald Duck", clientCallbackImpl);

	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new BorderLayout());

	    for(FileInfo info : session.listFiles(""))
	    {
		System.out.println(info);
	    }
	    
	    
	    // File homeDir = FileSystemView.getFileSystemView().getHomeDirectory();
	    TreeModel model = new FileInfoTreeModel();
	    JTree tree = new JTree(model);
	    tree.setCellRenderer(new FileInfoTreeRenderer("Donald Duck"));
	    
	    info = new JLabel("Gestartet");

	    
	    frame.add(new JScrollPane(tree), BorderLayout.CENTER);
	    frame.add(info, BorderLayout.SOUTH);
	    frame.pack();
	    frame.setVisible(true);

	}
	catch (Throwable t)
	{
	    t.printStackTrace();
	}
	finally
	{
	    // and finally 'release' the serverobject to release to connection to the server
	    //nameLookup.release(server);
	}

    }
    
    public static void setInfo(String text)
    {
	info.setText(text);
    }
    
    public static FileInfo[] getChildren(FileInfo parent) throws IOException
    {
	return session.listFiles(parent.getURIPath());
    }
}
