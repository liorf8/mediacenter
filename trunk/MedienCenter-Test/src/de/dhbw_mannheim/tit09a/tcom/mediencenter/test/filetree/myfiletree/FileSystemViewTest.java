package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.myfiletree;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class FileSystemViewTest
{
    public static void main(String[] args) throws IOException
    {
	FileSystemView fsv = FileSystemView.getFileSystemView();

	String filename = "a.txt";

	File dummyFile = File.createTempFile("tmp_mediencenter", filename);
	Icon icon = fsv.getSystemIcon(dummyFile);
	String display = fsv.getSystemDisplayName(dummyFile);
	String descr = fsv.getSystemTypeDescription(dummyFile);

	dummyFile.delete();
	System.out.printf("%s (display: %s; descr:%s; path:%s)%n", icon, display, descr,
		dummyFile.getAbsolutePath());

	/*
	 * Für default icons: Hashtable icons = new Hashtable();
	 * 
	 * // so füllen icons.put( "txt", new ImageIcon( "data/txt_icon.png" ) );
	 * 
	 * // so lesen Icon icon = (Icon)icons.get( "txt" );
	 */
    }

}
