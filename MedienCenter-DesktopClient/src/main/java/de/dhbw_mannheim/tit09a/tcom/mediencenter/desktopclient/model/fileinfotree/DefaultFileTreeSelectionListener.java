package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.fileinfotree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class DefaultFileTreeSelectionListener implements TreeSelectionListener
{
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		TreePath selectionPath = e.getNewLeadSelectionPath();

		if (selectionPath != null)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
			FileInfo fi = (FileInfo) node.getUserObject();
			System.out.println("Selected: " + fi);
		}
	}
}
