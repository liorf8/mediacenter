package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class TestListAllFiles
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		File file = new File("D:\\mhertram\\Java\\Studienarbeit\\workspace\\MedienCenter Server");
		List<File> allFiles = IOUtil.listAllFiles(file);
		for (File oneFile : allFiles)
			System.out.println(oneFile);
				
		long fullFileSize = IOUtil.fullFileSize(file);
		System.out.println(fullFileSize);
	}
}
