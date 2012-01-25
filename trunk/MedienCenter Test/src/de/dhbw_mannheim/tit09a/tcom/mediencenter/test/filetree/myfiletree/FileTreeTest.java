package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.myfiletree;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileTreeTest
{

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {

	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// File homeDir = FileSystemView.getFileSystemView().getHomeDirectory();
	File root = new File("C:\\Users\\mhertram");
	TreeModel model = new FileTreeModel(root);
	JTree tree = new JTree(model);
	tree.setCellRenderer(new FileTreeRenderer());

	frame.add(new JScrollPane(tree));
	frame.pack();
	frame.setVisible(true);

	tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
	{
	    @Override
	    public void valueChanged(TreeSelectionEvent e)
	    {
		TreePath path = e.getNewLeadSelectionPath();
		
		// newLeadSelectionPath is null, if you collapse the node in which the selected node was
		if (path != null)
		{
		    File chosenFile = (File) path.getLastPathComponent();
		    System.out.printf("Chosen: %s %s (%s Bytes; Last Modified: %d).%n",
			    chosenFile.isDirectory() ? "[ ]" : "[x]", chosenFile.getAbsolutePath(),
			    chosenFile.length(), chosenFile.lastModified());
		}

	    }
	});

    }
}
