package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.upload;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener.FileTreeSelectionListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.upload.FileInfoTree;

public class UploadTab extends Tab
{
	private static final long	serialVersionUID	= 1L;

	private JTree				filesTree;

	public UploadTab(MainFrame parent)
	{
		super("Upload");
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

	@Override
	public String getTip()
	{
		return "Upload media files to your online media library";
	}

	@Override
	public Icon getIcon()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
