package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.RetrieveMediaInfoAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.fileinfotree.FileInfoTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class PlayTab extends Tab
{
	private static final long		serialVersionUID	= 1L;

	private JSplitPane				splitPane;

	private final NoMediaInfoPanel	noMediaInfoPanel;
	private final ImageInfoPanel	imageInfoPanel;
	private final AudioInfoPanel	audioInfoPanel;
	private final VideoInfoPanel	videoInfoPanel;

	private RetrieveMediaInfoAction	retreiveMediaInfoAction;

	public PlayTab(MainFrame mainFrame)
	{
		super(mainFrame, "Play");

		retreiveMediaInfoAction = new RetrieveMediaInfoAction(mainFrame);

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
		return MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Play Tab.png");
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

	private JSplitPane createSplitPane()
	{
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(150);
		splitPane.setOneTouchExpandable(true);
		splitPane.setPreferredSize(new Dimension(100, splitPane.getPreferredSize().height)); // so that the splitpane only gets smaller, if there is really no space left
		splitPane.setLeftComponent(createFileInfoTree());
		setMediaInfoPanel(noMediaInfoPanel);
		return splitPane;
	}

	private JScrollPane createFileInfoTree()
	{
		JScrollPane fileInfoTreeScrollPane = new JScrollPane();
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
					FileInfo selectedFileInfo = (FileInfo) node.getUserObject();
					if (!selectedFileInfo.isDir())
					{
						retreiveMediaInfoAction.setSelectedFileInfo(selectedFileInfo);
						retreiveMediaInfoAction.actionPerformed(new ActionEvent(tree, -1, "Retrieve information"));
					}
				}
			}
		});

		fileInfoTreeScrollPane.setViewportView(tree);
		return fileInfoTreeScrollPane;
	}

}
