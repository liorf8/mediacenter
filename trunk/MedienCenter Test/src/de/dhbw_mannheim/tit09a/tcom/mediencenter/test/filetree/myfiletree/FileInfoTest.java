package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.myfiletree;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class FileInfoTest
{

	/**
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException
	{
		FileInfo fi = new FileInfo("mhertram", true, 0L, System.currentTimeMillis(), true);
		printFileInfo(fi);

		fi = new FileInfo("Donald.txt", true, 10L, 0, true);
		printFileInfo(fi);
		System.out.println(new File("mhertram\\"));
	}

	public static void printFileInfo(FileInfo fi)
	{
		System.out.println("------------------");
		System.out.println(fi.getURIPath());
		System.out.println(fi.isDir());
		System.out.println(fi.getSize());
		System.out.println(new Date(fi.getLastModified()));
		System.out.println(fi.getName());
		System.out.println(fi);
	}

}
