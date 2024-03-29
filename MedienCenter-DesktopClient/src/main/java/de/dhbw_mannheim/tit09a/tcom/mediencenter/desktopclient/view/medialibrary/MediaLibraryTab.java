package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.medialibrary;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.DisplayMediaInfoAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.fileinfotree.FileInfoTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class MediaLibraryTab extends Tab
{
	private static final long		serialVersionUID	= 1L;

	private JSplitPane				splitPane;
	private FileInfoTree fileInfoTree;
	private final NoMediaInfoPanel	noMediaInfoPanel;
	private final ImageInfoPanel	imageInfoPanel;
	private final AudioInfoPanel	audioInfoPanel;
	private final VideoInfoPanel	videoInfoPanel;
	private DisplayMediaInfoAction	displayMediaInfoAction;

	private FileInfo				selectedFileInfo;

	public MediaLibraryTab(MainFrame mainFrame)
	{
		super(mainFrame, "Media Library");

		displayMediaInfoAction = new DisplayMediaInfoAction(mainFrame);

		noMediaInfoPanel = new NoMediaInfoPanel(mainFrame);
		imageInfoPanel = new ImageInfoPanel(mainFrame);
		audioInfoPanel = new AudioInfoPanel(mainFrame);
		videoInfoPanel = new VideoInfoPanel(mainFrame);

		setLayout(new GridLayout(1, 1));
		add(createSplitPane());
	}

	@Override
	public String getTip()
	{
		return "Play your media";
	}

	@Override
	public Icon getIcon()
	{
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Play Tab.png");
	}

	public FileInfo getSelectedFileInfo()
	{
		return selectedFileInfo;
	}

	public NoMediaInfoPanel getNoMediaInfoPanel()
	{
		return noMediaInfoPanel;
	}

	public ImageInfoPanel getImageInfoPanel()
	{
		return imageInfoPanel;
	}

	public AudioInfoPanel getAudioInfoPanel()
	{
		return audioInfoPanel;
	}

	public VideoInfoPanel getVideoInfoPanel()
	{
		return videoInfoPanel;
	}

	public void setMediaInfoPanel(MediaInfoPanel mip)
	{
		mip.reset();
		int divLoc = splitPane.getDividerLocation();
		splitPane.setRightComponent(mip);
		splitPane.setDividerLocation(divLoc);
	}
	
	public void reloadFileInfoTree(TreePath path)
	{
		// TODO FIXME no proper way to reload the tree. The TreePaths seem to stay inconsistent, for the parent will be null after the second rename
		try
		{
			fileInfoTree.fireTreeWillCollapse(path);
			fileInfoTree.fireTreeWillExpand(path);
			fileInfoTree.clearSelection();
			DefaultTreeModel model = (DefaultTreeModel) fileInfoTree.getModel();
			model.reload((DefaultMutableTreeNode) path.getLastPathComponent());
		}
		catch (ExpandVetoException e)
		{
			// should never occur
			e.printStackTrace();
		}
	}
	private JSplitPane createSplitPane()
	{
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(200);
		// so that the splitpane only gets smaller, if there is really no space left
		//splitPane.setPreferredSize(new Dimension(100, splitPane.getPreferredSize().height));
		splitPane.setLeftComponent(createFileInfoTree());
		setMediaInfoPanel(noMediaInfoPanel);
		return splitPane;
	}

	private JScrollPane createFileInfoTree()
	{
		JScrollPane fileInfoTreeScrollPane = new JScrollPane();
		fileInfoTreeScrollPane.setMinimumSize(new Dimension(200, fileInfoTreeScrollPane.getMinimumSize().height));
		fileInfoTree = new FileInfoTree();
		fileInfoTree.addTreeSelectionListener(new TreeSelectionListener()
		{
			@Override
			public void valueChanged(TreeSelectionEvent e)
			{
				TreePath selectionPath = e.getNewLeadSelectionPath();

				if (selectionPath != null)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
					FileInfo fi = (FileInfo) node.getUserObject();
					setSelectedFileInfo(fi);
					if (!fi.isDir())
					{
						displayMediaInfoAction.actionPerformed(new ActionEvent(fileInfoTree, -1, "Display info"));
					}
				}
			}
		});

		fileInfoTreeScrollPane.setViewportView(fileInfoTree);
		return fileInfoTreeScrollPane;
	}

	private void setSelectedFileInfo(FileInfo selectedFileInfo)
	{
		this.selectedFileInfo = selectedFileInfo;
	}
}
