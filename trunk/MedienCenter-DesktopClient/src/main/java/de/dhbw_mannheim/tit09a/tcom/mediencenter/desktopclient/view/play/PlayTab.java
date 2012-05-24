package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.RetreiveMediaInfoAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.fileinfotree.FileInfoTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class PlayTab extends Tab
{
	private static final long		serialVersionUID	= 1L;

	private MediaComponent			mediaComponent;
	private JSplitPane				splitPane;
	private FileInfo				selectedFileInfo;

	private RetreiveMediaInfoAction	retreiveMediaInfoAction;

	public PlayTab(MainFrame mainFrame)
	{
		super(mainFrame, "Play");

		retreiveMediaInfoAction = new RetreiveMediaInfoAction(mainFrame);

		setLayout(new GridLayout(1, 1));
		add(createSplitPane());
	}

	public MediaComponent getMediaComponent()
	{
		return mediaComponent;
	}

	@Override
	public String getTip()
	{
		return "Play your media";
	}

	@Override
	public Icon getIcon()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo getSelectedFileInfo()
	{
		return selectedFileInfo;
	}

	private JSplitPane createSplitPane()
	{
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(150);
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(createFileInfoTree());
		splitPane.setRightComponent(new MediaInfoPanel());
		return splitPane;
	}

	private FileInfoTree createFileInfoTree()
	{
		final FileInfoTree tree = new FileInfoTree();
		tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			@Override
			public void valueChanged(TreeSelectionEvent e)
			{
				TreePath selectionPath = e.getNewLeadSelectionPath();

				if (selectionPath != null)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
					selectedFileInfo = (FileInfo) node.getUserObject();
					if (!selectedFileInfo.isDir())
					{
						retreiveMediaInfoAction.actionPerformed(new ActionEvent(tree, -1, "Retreive information"));
					}
				}
			}
		});

		return tree;
	}
}
