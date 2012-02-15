package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class MkDirRecursivelyTest
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println(IOUtil.executeMkDirRecursively(new File("C:\\Users\\Max\\ich\\bin\\du")));
	}
}
