package de.dhbw_mannheim.tit09a.tcom.test.filetree.example.explorer;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class FileTree extends JTree implements TreeWillExpandListener
{
    private static final long serialVersionUID = -2444667977301185992L;

    private static class FileComparator implements Comparator<File>
    {

	public int compare(File f1, File f2)
	{
	    if (f1.isDirectory() ^ f2.isDirectory())
	    {
		return (f1.isDirectory() ? -1 : 1);
	    }
	    return f1.getName().compareToIgnoreCase(f2.getName());

	}
    }

    private static class DirectoryFilter implements FileFilter
    {
	public boolean accept(File f)
	{
	    return f.isDirectory();
	}
    }

    private static class AcceptAllFilter implements FileFilter
    {
	public boolean accept(File f)
	{
	    return true;
	}
    }

    private static final Comparator<File> FILE_COMPARATOR = new FileComparator();

    /** Ein FileFilter, der nur Verzeichnisse akzeptiert */
    public static final FileFilter DIRECTORY_FILTER = new DirectoryFilter();

    /** Ein FileFilter der alle Typen von Dateien und Verzeichnisse akzeptiert */
    public static final FileFilter ACCEPT_ALL_FILTER = new AcceptAllFilter();

    /** Der aktuelle FileFilter */
    protected FileFilter filter = null;

    public FileTree()
    {
	// Home Verzeichnis feststellen
	File homeDir = FileSystemView.getFileSystemView().getHomeDirectory();
	// Wurzelelement erstellen
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(homeDir);
	// Model erstellen mit root als Wurzelelement
	DefaultTreeModel model = new DefaultTreeModel(root);
	setModel(model);
	setShowsRootHandles(true);
	// TreeCellRenderer setzen.
	setCellRenderer(new FileTreeRenderer());
	// Wurzel aufklappen
	expandPath(root);
	// Listener hinzufügen
	addTreeWillExpandListener(this);
	// und so ...
	getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Erweitert einen Pfad im Tree.
     * 
     * @param d
     *            Den Parent Knoten
     */
    private void expandPath(final DefaultMutableTreeNode d)
    {
	d.removeAllChildren();
	File[] tempf = ((File) d.getUserObject()).listFiles();

	List<File> files = Arrays.asList(tempf);
	Collections.sort(files, FILE_COMPARATOR);

	DefaultMutableTreeNode temp = null;
	for (File file : files)
	{
	    if (filter != null && !filter.accept(file)) continue;
	    temp = new DefaultMutableTreeNode(file);
	    if (file.isDirectory()) temp.add(new DefaultMutableTreeNode(null));
	    d.add(temp);
	}
	((DefaultTreeModel) getModel()).reload(d);
    }

    /**
     * Setzt den FileFilter und setzt den Tree zurück.
     * 
     * @param f
     *            Der neue FileFilter
     */
    public void setFileFilter(FileFilter f)
    {
	filter = f;
	expandPath((DefaultMutableTreeNode) getModel().getRoot());
    }

    /**
     * Liefert den aktuell gesetzten FileFilter zurück
     * 
     * @return Den FileFilter
     */
    public FileFilter getFileFilter()
    {
	return filter;
    }

    public File getSelectedFile()
    {
	TreePath selectionPath = getSelectionPath();
	if (selectionPath == null) return null;
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
	return (File) node.getUserObject();
    }

    public void treeWillCollapse(TreeExpansionEvent e)
    {
	((DefaultMutableTreeNode) (e.getPath().getLastPathComponent())).removeAllChildren();
	((DefaultMutableTreeNode) (e.getPath().getLastPathComponent()))
		.add(new DefaultMutableTreeNode(null));
    }

    public void treeWillExpand(TreeExpansionEvent e)
    {
	expandPath((DefaultMutableTreeNode) (e.getPath().getLastPathComponent()));
    }

}
