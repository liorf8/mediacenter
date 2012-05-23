package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;

public class SettingsTab extends Tab implements TreeSelectionListener
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long			serialVersionUID			= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Settings					settings;

	private JSplitPane					splitPane;
	private JScrollPane					categoryTreePane;

	// List just to iterate to search by name
	private List<CategoryPanel>			categoryPanels				= new ArrayList<>();
	private ServerCategoryPanel			serverCategoryPanel			= new ServerCategoryPanel();
	private UICategoryPanel				uiCategoryPanel				= new UICategoryPanel();
	private PlayerCategoryPanel			playerCategoryPanel			= new PlayerCategoryPanel();
	private TranscodingCategoryPanel	transcodingCategoryPanel	= new TranscodingCategoryPanel();
	private StreamingCategoryPanel		streamingCategoryPanel		= new StreamingCategoryPanel();

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public SettingsTab(MainFrame mainFrame)
	{
		super(mainFrame, "Settings");

		settings = MainController.getInstance().getSettings();

		categoryPanels.add(serverCategoryPanel);
		categoryPanels.add(uiCategoryPanel);
		categoryPanels.add(playerCategoryPanel);
		categoryPanels.add(streamingCategoryPanel);
		categoryPanels.add(transcodingCategoryPanel);

		setLayout(new GridLayout(1, 1));
		add(createSplitPane());
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public String getTip()
	{
		return "Alter your settings";
	}

	// --------------------------------------------------------------------------------
	@Override
	public Icon getIcon()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// --------------------------------------------------------------------------------
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		// Returns the last path element of the selection.
		// This method is useful only when the selection model allows a single selection.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
		if (node == null)
			return; // Nothing is selected.

		CategoryPanel categoryPanel = (CategoryPanel) node.getUserObject();
		setChosenCategory(categoryPanel);
	}

	// --------------------------------------------------------------------------------
	public CategoryPanel getChosenCategory()
	{
		return (CategoryPanel) splitPane.getRightComponent();
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private JSplitPane createSplitPane()
	{
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(settings.getPropertyAsInt(Settings.KEY_SETTINGS_DIVIDER_LOCATION));
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(createCategoryTreePane());
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				settings.setProperty(Settings.KEY_SETTINGS_DIVIDER_LOCATION, (int) evt.getNewValue());
			}
		});
		return splitPane;
	}

	// --------------------------------------------------------------------------------
	private JScrollPane createCategoryTreePane()
	{
		categoryTreePane = new JScrollPane(createCategoryTree());

		return categoryTreePane;
	}

	// --------------------------------------------------------------------------------
	private JTree createCategoryTree()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Settings");
		createNodes(root);
		JTree categoryTree = new JTree(root);
		categoryTree.setRootVisible(false);
		categoryTree.setExpandsSelectedPaths(true);
		categoryTree.setShowsRootHandles(true);
		categoryTree.setToggleClickCount(2);
		categoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		categoryTree.setExpandsSelectedPaths(true);
		categoryTree.addTreeSelectionListener(this);

		// set initial selection path according to the settings
		CategoryPanel selectedCategory = searchCategoryPanel(settings.getProperty(Settings.KEY_SETTINGS_SELECTED_CATEGORY));
		if (selectedCategory != null)
		{
			TreePath path = getTreePathOfUserObject(categoryTree, selectedCategory);
			categoryTree.setSelectionPath(path);

		}
		return categoryTree;
	}

	// --------------------------------------------------------------------------------
	private void createNodes(DefaultMutableTreeNode top)
	{
		DefaultMutableTreeNode firstLevelCategory = null;
		DefaultMutableTreeNode secondLevelCategory = null;

		firstLevelCategory = new DefaultMutableTreeNode(serverCategoryPanel);
		top.add(firstLevelCategory);

		firstLevelCategory = new DefaultMutableTreeNode(uiCategoryPanel);
		top.add(firstLevelCategory);

		firstLevelCategory = new DefaultMutableTreeNode(playerCategoryPanel);
		top.add(firstLevelCategory);

		firstLevelCategory = new DefaultMutableTreeNode(streamingCategoryPanel);
		secondLevelCategory = new DefaultMutableTreeNode(transcodingCategoryPanel);
		firstLevelCategory.add(secondLevelCategory);
		top.add(firstLevelCategory);

		// // original Tutorial
		// book = new DefaultMutableTreeNode(new String("The Java Tutorial: A Short Course on the Basics"));
		// category.add(book);
		//
		// // Tutorial Continued
		// book = new DefaultMutableTreeNode(new String("The Java Tutorial Continued: The Rest of the JDK"));
		// category.add(book);
		//
		// // Swing Tutorial
		// book = new DefaultMutableTreeNode(new String("The Swing Tutorial: A Guide to Constructing GUIs"));
		// category.add(book);
		//
		// // ...add more books for programmers...
		//
		// category = new DefaultMutableTreeNode("Books for Java Implementers");
		// top.add(category);
		//
		// // VM
		// book = new DefaultMutableTreeNode(new String("The Java Virtual Machine Specification"));
		// category.add(book);
		//
		// // Language Spec
		// book = new DefaultMutableTreeNode(new String("The Java Language Specification"));
		// category.add(book);
	}

	// --------------------------------------------------------------------------------
	private void setChosenCategory(CategoryPanel panel)
	{
		// keep the divider location
		int divLoc = splitPane.getDividerLocation();
		splitPane.setRightComponent(panel);
		splitPane.setDividerLocation(divLoc);
		settings.setProperty(Settings.KEY_SETTINGS_SELECTED_CATEGORY, panel.getName());
	}

	// --------------------------------------------------------------------------------
	private CategoryPanel searchCategoryPanel(String name)
	{
		for (CategoryPanel panel : categoryPanels)
		{
			if (panel.getName().equals(name))
				return panel;
		}
		return null;
	}

	// --------------------------------------------------------------------------------
	public static TreePath getTreePathOfUserObject(JTree tree, Object obj)
	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		return new TreePath(model.getPathToRoot(searchUserObject(tree, obj)));
	}

	// --------------------------------------------------------------------------------
	public static DefaultMutableTreeNode searchUserObject(JTree tree, Object obj)
	{
		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		Enumeration<?> e = root.breadthFirstEnumeration();
		while (e.hasMoreElements())
		{
			node = (DefaultMutableTreeNode) e.nextElement();
			if (obj.equals(node.getUserObject()))
			{
				return node;
			}
		}
		return null;
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
