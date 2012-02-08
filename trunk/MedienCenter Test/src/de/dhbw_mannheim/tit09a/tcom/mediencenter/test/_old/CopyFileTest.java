package de.dhbw_mannheim.tit09a.tcom.mediencenter.test._old;

import java.io.File;
import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
public class CopyFileTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	File src = new File("D:\\mhertram\\Downloads\\eclipse-jee-indigo-SR1-win32-x86_64.zip");
	File dest = new File("D:\\mhertram\\Downloads\\eclipse-jee-indigo-SR1-win32-x86_64.zip.bak");

	long start = 0L;
	try
	{
//	    start = System.currentTimeMillis();
//	    NIOUtil.copyFile(src.toPath(), dest.toPath(), true);
//	    System.out.println("Duration Files.copy: " + (System.currentTimeMillis() - start));
	    
	    start = System.currentTimeMillis();
//	    start = System.currentTimeMillis();
//	    NIOUtil.copyFile(src.toPath(), dest.toPath(), true);
//	    System.out.println("Duration Files.copy: " + (System.currentTimeMillis() - start));
//	    
	    start = System.currentTimeMillis();
	    FileCopy.copyFileByteBuffer(src, dest, true);
	    System.out.println("Duration(copyFileByteBuffer): " + (System.currentTimeMillis() - start));

	    start = System.currentTimeMillis();
	    IOUtil.copyFile(src, dest, true);
	    System.out.println("Duration transferInto (64kb): " + (System.currentTimeMillis() - start));
	    
	    start = System.currentTimeMillis();
	    FileCopy.copyFileTransferWhole(src, dest, true);
	    System.out.println("Duration(copyFileTransferWhole): " + (System.currentTimeMillis() - start));
	    

	    
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	/*
	 * Ergebnis Laptop Arbeit
	 * Duration3: 3682
	 * Duration2: 14384
	 * Duration: 5820
	 * 
	 * Duration: 3962
	 * Duration3: 4026
	 * Duration2: 12996
	 * 
	 * Duration2: 12808
	 * Duration: 4478
	 * Duration3: 2700
	 * 
	 * 
	 * Ergebnis mein Laptop
	 * 
	 *  Duration2: 9091
	 *  Duration: 1505
	 *  Duration3: 491
	 *  
	 *  Duration3: 746
	 *  Duration2: 2882
	 *  Duration: 449
	 *  
	 *  Duration: 501
	 *  Duration3: 823
	 *  Duration2: 2681

	 */
	
	
    }

}
