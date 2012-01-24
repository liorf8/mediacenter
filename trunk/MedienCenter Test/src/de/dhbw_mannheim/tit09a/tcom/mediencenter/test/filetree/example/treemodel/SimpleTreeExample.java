package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.example.treemodel;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class SimpleTreeExample
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Wurzel");

	for (int nodeCnt = 0; nodeCnt < 4; nodeCnt++)
	{
	    DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode("Knoten " + nodeCnt);
	    root.add(dmtn);

	    for (int leafCnt = 1; leafCnt < 4; leafCnt++)
		dmtn.add(new DefaultMutableTreeNode("Blatt " + (nodeCnt * 3 + leafCnt)));
	}

	JTree tree = new JTree(root);
	
	frame.add(new JScrollPane(tree));
	frame.pack();
	frame.setVisible(true);

	tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
	{
	    @Override
	    public void valueChanged(TreeSelectionEvent e)
	    {
		TreePath path = e.getNewLeadSelectionPath();
		System.out.println(path);
	    }
	});

    }

}
