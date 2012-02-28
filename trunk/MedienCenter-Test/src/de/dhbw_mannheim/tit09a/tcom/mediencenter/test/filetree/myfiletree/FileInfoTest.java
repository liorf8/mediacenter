package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.myfiletree;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Date;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.PathFileInfo;

public class FileInfoTest
{

	/**
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException
	{
		PathFileInfo fi = new PathFileInfo(Paths.get("mhertram"), "bla", true, 0L, System.currentTimeMillis(), true);
		printFileInfo(fi);

		fi = new PathFileInfo(Paths.get("Donald.txt"), "bla", true, 10L, 0, true);
		printFileInfo(fi);
		System.out.println(new File("mhertram\\"));
	}

	public static void printFileInfo(PathFileInfo fi)
	{
		System.out.println("------------------");
		System.out.println(fi.getPath());
		System.out.println(fi.isDir());
		System.out.println(fi.getSize());
		System.out.println(new Date(fi.getLastModified()));
		System.out.println(fi.getName());
		System.out.println(fi);
	}

}
