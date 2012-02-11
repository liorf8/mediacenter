package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.awt.Point;
import java.io.File;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class Testtest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File file = new File("D:\\mhertram\\Downloads\\Personalausweis.pdf");
		System.out.println(IOUtil.isFileOrEmptyDir(file));

		System.out.println(new Point(100, 200));
		
		System.out.println(0xFFFF);
		System.out.println(1024*64);

		
		System.out.println(IOUtil.nonExistingFilePath(new File("C:\\Users\\Max\\Downloads\\himym.avi")));
	}

}
