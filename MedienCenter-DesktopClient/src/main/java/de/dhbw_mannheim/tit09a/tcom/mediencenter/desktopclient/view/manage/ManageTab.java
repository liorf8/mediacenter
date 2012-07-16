package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.manage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.CreateDirAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.DeleteAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.RenameAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.UploadFileAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.fileinfotree.FileInfoTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.filesystemtree.FileTree;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;

public class ManageTab extends Tab implements TreeSelectionListener
{
	private static final long	serialVersionUID	= 1L;

	private DateFormat			dateFormat			= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private JSplitPane			splitPane;

	private FileInfoTree		serverFileTree;
	private JLabel				lblServerFileInfo;
	private JButton				btnCreateDir;
	private JButton				btnRename;
	private JButton				btnDelete;

	private FileTree			clientFileTree;
	private JLabel				lblClientFileInfo;
	private JButton				btnUpload;
	private JLabel				lblChosenDestinationFolder;
	private JTextField			txtFldChosenDestinationFolder;
	private JLabel				lblChosenFileToUpload;
	private JTextField			txtFldChosenFileToUpload;

	public ManageTab(MainFrame mainFrame)
	{
		super(mainFrame, "Manage");
		initGUI();
	}

	@Override
	public String getTip()
	{
		return "Manage your media files";
	}

	@Override
	public Icon getIcon()
	{
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Manage Tab.png");
	}

	public DefaultMutableTreeNode getSelectedServerNode()
	{
		return (DefaultMutableTreeNode) serverFileTree.getSelectionPath().getLastPathComponent();
	}

	public DefaultMutableTreeNode getSelectedClientNode()
	{
		return (DefaultMutableTreeNode) clientFileTree.getSelectionPath().getLastPathComponent();
	}

	public String getChosenDestinationFolder()
	{
		return txtFldChosenDestinationFolder.getText();
	}

	public String getChosenFileToUpload()
	{
		return txtFldChosenFileToUpload.getText();
	}

	public void reloadServerFileTree(TreePath path)
	{
		// TODO FIXME no proper way to reload the tree. The TreePaths seem to stay inconsistent, for the parent will be null after the second rename
		try
		{
			serverFileTree.fireTreeWillCollapse(path);
			serverFileTree.fireTreeWillExpand(path);
			serverFileTree.clearSelection();
			DefaultTreeModel model = (DefaultTreeModel) serverFileTree.getModel();
			model.reload((DefaultMutableTreeNode) path.getLastPathComponent());
		}
		catch (ExpandVetoException e)
		{
			// should never occur
			e.printStackTrace();
		}
	}

	private void initGUI()
	{
		setLayout(new BorderLayout());

		this.add(createSplitPane(), BorderLayout.CENTER);
		this.add(createSouthPanel(), BorderLayout.SOUTH);
	}

	private JSplitPane createSplitPane()
	{
		splitPane = new JSplitPane();
		splitPane.setLeftComponent(createServerFilePanel());
		splitPane.setRightComponent(createClientFileTree());
		splitPane.setDividerLocation(0.5);
		return splitPane;
	}

	private JPanel createServerFilePanel()
	{
		JPanel serverFileTreePanel = new JPanel();
		serverFileTreePanel.setLayout(new BorderLayout());
		serverFileTreePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><u>MediaScrub server</u></html>"));

		lblServerFileInfo = new JLabel(" ");
		serverFileTreePanel.add(lblServerFileInfo, BorderLayout.NORTH);

		JScrollPane serverFileTreeScrollPane = new JScrollPane();
		serverFileTreeScrollPane.setPreferredSize(new Dimension(200, serverFileTreeScrollPane.getMinimumSize().height));
		serverFileTree = new FileInfoTree();
		serverFileTree.addTreeSelectionListener(this);
		serverFileTreeScrollPane.setViewportView(serverFileTree);
		serverFileTreePanel.add(serverFileTreeScrollPane, BorderLayout.CENTER);

		JPanel btnsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		btnCreateDir = new JButton(new CreateDirAction(mainFrame));
		btnCreateDir.setEnabled(false);
		btnsPanel.add(btnCreateDir);

		btnRename = new JButton(new RenameAction(mainFrame));
		btnRename.setEnabled(false);
		btnsPanel.add(btnRename);

		btnDelete = new JButton(new DeleteAction(mainFrame));
		btnDelete.setEnabled(false);
		btnsPanel.add(btnDelete);

		serverFileTreePanel.add(btnsPanel, BorderLayout.SOUTH);

		return serverFileTreePanel;
	}

	private JPanel createClientFileTree()
	{
		JPanel clientFileTreePanel = new JPanel();
		clientFileTreePanel.setLayout(new BorderLayout());
		clientFileTreePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><u>Your Computer</u></html>"));

		lblClientFileInfo = new JLabel(" ");
		clientFileTreePanel.add(lblClientFileInfo, BorderLayout.NORTH);

		clientFileTree = new FileTree();
		clientFileTree.addTreeSelectionListener(this);
		clientFileTree.setFileFilter(FileTree.ACCEPT_ALL_FILTER);
		clientFileTreePanel.add(new JScrollPane(clientFileTree), BorderLayout.CENTER);

		return clientFileTreePanel;
	}

	private JPanel createSouthPanel()
	{
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		southPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><u>Upload</u></html>"));

		btnUpload = new JButton();
		btnUpload.setAction(new UploadFileAction(mainFrame));
		btnUpload.setEnabled(false);

		lblChosenDestinationFolder = new JLabel("Destination folder:");
		txtFldChosenDestinationFolder = new JTextField("-");
		txtFldChosenDestinationFolder.setEditable(false);
		txtFldChosenDestinationFolder.setColumns(20);

		lblChosenFileToUpload = new JLabel("Source file:");
		txtFldChosenFileToUpload = new JTextField("-");
		txtFldChosenFileToUpload.setEditable(false);
		txtFldChosenFileToUpload.setColumns(20);

		southPanel.add(lblChosenDestinationFolder);
		southPanel.add(txtFldChosenDestinationFolder);
		southPanel.add(btnUpload);
		southPanel.add(lblChosenFileToUpload);
		southPanel.add(txtFldChosenFileToUpload);

		return southPanel;
	}

	// TreeSelectionListener
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		// set the modifying buttons disabled
		btnCreateDir.setEnabled(false);
		btnRename.setEnabled(false);
		btnDelete.setEnabled(false);
		btnUpload.setEnabled(false);

		TreePath selectionPath = e.getNewLeadSelectionPath();
		if (e.getSource().equals(serverFileTree))
		{
			txtFldChosenDestinationFolder.setText("-");
			if (selectionPath != null)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();

				FileInfo selectedFI = (FileInfo) node.getUserObject();
				String fileInfoText = formatFileInfos(selectedFI.getSize(), selectedFI.getContentType(), selectedFI.getLastModified());
				lblServerFileInfo.setText(fileInfoText);

				// new dir can be created on any node
				btnCreateDir.setEnabled(true);

				if (selectedFI.isModifiable())
				{
					btnRename.setEnabled(true);
					btnDelete.setEnabled(true);
				}

				// set the upload / create dir button enabled/disabled
				if (selectedFI.isDir())
				{
					txtFldChosenDestinationFolder.setText(selectedFI.getPath());
					if (!txtFldChosenFileToUpload.getText().equals("-"))
						btnUpload.setEnabled(true);
				}
			}
		}
		else if (e.getSource().equals(clientFileTree))
		{
			txtFldChosenFileToUpload.setText("-");
			if (selectionPath != null)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();

				File file = (File) node.getUserObject();
				String fileInfoText = formatFileInfos(file.length(), NIOUtil.probeContentType(file), file.lastModified());
				lblClientFileInfo.setText(fileInfoText);

				if (!file.isDirectory())
				{
					txtFldChosenFileToUpload.setText(file.getAbsolutePath());
					if (!txtFldChosenDestinationFolder.getText().equals("-"))
						btnUpload.setEnabled(true);
				}
			}
		}
	}

	private String formatFileInfos(long fileSize, String contentType, long lastModified)
	{
		return ByteValue.bytesToString(fileSize) + " | " + contentType + " | " + dateFormat.format(new Date(lastModified));
	}
}
