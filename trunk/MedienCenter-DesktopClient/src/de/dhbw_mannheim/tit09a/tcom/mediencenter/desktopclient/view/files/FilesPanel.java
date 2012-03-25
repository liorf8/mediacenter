package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.files;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.FileTreeSelectionListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.files.FileInfoTree;

public class FilesPanel extends JPanel
{
	private static final long	serialVersionUID	= 1L;

	private JTree				filesTree;

	public FilesPanel(MainFrame parent)
	{
		setLayout(new BorderLayout());
		filesTree = createTree();
		this.add(new JScrollPane(filesTree), BorderLayout.CENTER);
	}

	private JTree createTree()
	{
		final FileInfoTree tree = new FileInfoTree();
		tree.addTreeSelectionListener(new FileTreeSelectionListener());
		return tree;
	}

}
