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
		Path src = Paths.get("C:\\Users\\Max\\MedienCenter\\USER_FILES\\1\\Music\\dir");
		Path targetDir = Paths.get("C:\\Users\\Max\\MedienCenter\\USER_FILES\\1");
		System.out.println(NIOUtil.copyMove(src, targetDir, true, false));

	}
}
