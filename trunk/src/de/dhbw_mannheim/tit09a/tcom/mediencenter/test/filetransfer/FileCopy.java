package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.io.*;

import javax.swing.ProgressMonitorInputStream;

public class FileCopy
{
    static void copy(InputStream fis, OutputStream fos)
    {

    }

    static void copyFile(File src, File dest)
    {
	final long fileLength = src.length();
	final byte[] buffer = new byte[0xFFFF]; // 64KB (65535 Bytes)
	long bytesRead = 0;
	InputStream fis = null;
	FileOutputStream fos = null;
	float perc = 0.0f;
	try
	{
	    fis= new BufferedInputStream(new ProgressMonitorInputStream(null,"Reading " + src, new FileInputStream(src)));
	    
	    fos = new FileOutputStream(dest);
	    for (int len; (len = fis.read(buffer)) != -1;)
	    {
		// Count not higher than fileLength (last buffer is not filled fully)
		bytesRead = Math.min(bytesRead + buffer.length, fileLength);
		perc = bytesRead * 100.0f / fileLength;
		System.out.printf("%d/%d Bytes (%.2f%%) gelesen...%n", bytesRead, fileLength, perc);
		fos.write(buffer, 0, len);
	    }
	}
	catch (IOException e)
	{
	    System.err.println(e);
	}
	finally
	{
	    if (fis != null) try
	    {
		fis.close();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	    if (fos != null) try
	    {
		fos.close();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}

    }

    public static void main(String[] args)
    {
	String srcFile = "D:\\mhertram\\LuaForWindows_v5.1.4-45.exe";
	args = new String[] { srcFile, srcFile + ".bak" };
	if (args.length != 2)
	    System.err.println("Usage: java FileCopy <src> <dest>");
	else
	    copyFile(new File(args[0]), new File(args[1]));
    }
}