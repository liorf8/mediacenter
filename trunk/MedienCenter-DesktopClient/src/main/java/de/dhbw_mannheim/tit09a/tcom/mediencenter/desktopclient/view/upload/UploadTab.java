package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.upload;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.fileinfotree.FileInfoTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.fileinfotree.DefaultFileTreeSelectionListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class UploadTab extends Tab
{
	private static final long	serialVersionUID	= 1L;

	private JTree				filesTree;

	public UploadTab(MainFrame mainFrame)
	{
		super(mainFrame, "Upload");
		setLayout(new BorderLayout());
		filesTree = createTree();
		this.add(new JScrollPane(filesTree), BorderLayout.CENTER);
	}

	private JTree createTree()
	{
		final FileInfoTree tree = new FileInfoTree();
		tree.addTreeSelectionListener(new DefaultFileTreeSelectionListener());
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
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Upload Tab.png");
	}
}
