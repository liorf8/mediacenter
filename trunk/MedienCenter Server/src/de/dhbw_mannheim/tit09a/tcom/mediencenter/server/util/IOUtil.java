package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.IOTask;

public class IOUtil
{
    private IOUtil()
    {

    }

    public static <T> T executeIOTask(ExecutorService executor, IOTask<T> task) throws IOException
    {
	try
	{
	    return executor.submit(task).get();
	}
	catch (Throwable e)
	{
	    Throwable cause = e;
	    if (e.getCause() != null) cause = e.getCause();
	    if (cause instanceof IOException)
	    {
		throw (IOException) cause;
	    }
	    else
	    {
		e.printStackTrace();
		return null;
	    }
	}
    }

    public static void ensureValidFilename(String filename, char[] forbiddenChars)
	    throws IllegalArgumentException
    {
	for (char oneChar : forbiddenChars)
	{
	    if (filename.indexOf(oneChar) > -1)
		throw new IllegalArgumentException("Filename '" + filename
			+ "' contains illegal char '" + oneChar + "'. Illegal chars: "
			+ Arrays.toString(forbiddenChars));
	}
    }

    public static void ensureExists(File file) throws IOException
    {
	if (!file.exists())
	    throw new IOException("File does not exist: " + file.getAbsolutePath());
    }

    public static void ensureDoesNotExist(File file) throws IOException
    {
	if (file.exists()) throw new IOException("File already exists: " + file.getAbsolutePath());
    }

    public static void ensureIsDir(File file) throws IOException
    {
	if (!file.isDirectory())
	    throw new IOException("File is no directory: " + file.getAbsolutePath());
    }

    public static void ensureIsNoDir(File file) throws IOException
    {
	if (file.isDirectory())
	    throw new IOException("File is a directory: " + file.getAbsolutePath());
    }

    public static void ensureEmptyDir(File dir) throws IOException
    {
	if (dir.listFiles().length > 0)
	    throw new IOException("Directory is not empty: " + dir.getAbsolutePath());
    }

    public static void executeDelete(File file) throws IOException
    {
	if (!file.delete()) throw new IOException("Could not delete: " + file.getAbsolutePath());
    }

    public static void executeMkDir(File dir) throws IOException
    {
	if (!dir.exists())
	{
	    if (!dir.mkdir())
		throw new IOException("Could not create dir: " + dir.getAbsolutePath());
	}
    }

    public static void executeRenameTo(File file, File dest) throws IOException
    {
	if (!file.renameTo(dest))
	    throw new IOException("Could not rename file '" + file.getAbsolutePath() + "' to '"
		    + dest.getAbsolutePath() + "'");
    }

    public static void executeCreateNewFile(File file) throws IOException
    {
	if (!file.createNewFile())
	    throw new IOException("Could create  file: " + file.getAbsolutePath());
    }

    public static String omitRoot(String filePath, String root)
    {
	return filePath.substring(filePath.indexOf(root));
    }

    public static void close(Closeable closeable) throws IOException
    {
	if (closeable != null) closeable.close();
    }

    // wenn replace = true, überschreibt er einfach
    // wenn false
    public static void copyFile(File src, File dest, boolean replace) throws IOException
    {
	// do nothing if the dest exists and replace is false
	if (!replace && dest.exists())
	{
	    return;
	}

	FileChannel srcChannel = null;
	FileChannel destChannel = null;
	try
	{
	    // inChannel.transferTo(0, inChannel.size(), outChannel); // original -- apparently has trouble
	    // copying large files on Windows
	    // magic number for Windows, (64Mb - 32Kb)
	    srcChannel = new FileInputStream(src).getChannel();
	    destChannel = new FileOutputStream(dest).getChannel();
	    //int maxCount = (64 * 1024 * 1024) - (32 * 1024);
	    int maxCount = 64*1024; // 64kb is faster
	    long size = srcChannel.size();
	    long position = 0;
	    while (position < size)
	    {
		position += srcChannel.transferTo(position, maxCount, destChannel);
	    }
	}
	finally
	{
	    // all data written. Now we can close the channels
	    close(srcChannel);
	    close(destChannel);
	}
    }
}
