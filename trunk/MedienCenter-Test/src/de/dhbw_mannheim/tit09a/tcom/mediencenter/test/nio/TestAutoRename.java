package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;

public class TestAutoRename
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		Path existingPath = Paths.get("C:\\Users\\Max\\Downloads");
		System.out.println(NIOUtil.autoRename(existingPath));
		
		Path newDir =  NIOUtil.createDir(existingPath, "jDownloader");
		System.out.println(newDir);
	}
}
