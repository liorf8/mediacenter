package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class NIOBasicTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	String contentType;
	try
	{
	    FileSystem fs = FileSystems.getDefault();
	    for(Path path : fs.getRootDirectories())
	    {
		System.out.println(path);
	    }
	    contentType = Files.probeContentType(new File(
		    "D:\\mhertram\\Downloads\\eclipse-jee-indigo-SR1-win32-x86_64.zip").toPath());
	    System.out.println(contentType);
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
