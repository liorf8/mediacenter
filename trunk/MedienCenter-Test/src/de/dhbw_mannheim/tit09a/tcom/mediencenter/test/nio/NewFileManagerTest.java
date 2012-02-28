package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.nio;

import java.nio.file.Path;
import java.nio.file.Paths;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;

public class NewFileManagerTest
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		Path src = Paths.get("C:\\Users\\Max\\Downloads\\s.txt");
		Path targetDir = Paths.get("C:\\Users\\Max\\Downloaddddd");
		System.out.println(NIOUtil.move(src, targetDir, true));
	}
}
