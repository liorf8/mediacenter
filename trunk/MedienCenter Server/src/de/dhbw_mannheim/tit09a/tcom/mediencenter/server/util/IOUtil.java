package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.IOTask;

public class IOUtil
{
    private IOUtil()
    {

    }

    public static <T> T executeIOTask(ThreadPoolExecutor executor, IOTask<T> task)
	    throws IOException
    {
	ServerMain.serverLogger.finer(String.format(
		"Submitting %s to %s. Active: %d/%d. Queued: %d (free slots: %d).", task.getClass()
			.getSimpleName(), "IOExecutor", executor.getActiveCount(), executor
			.getMaximumPoolSize(), executor.getQueue().size(), executor.getQueue()
			.remainingCapacity()));
	try
	{
	    return executor.submit(task).get();
	}
	catch (Throwable e)
	{
	    e.printStackTrace();
	    Throwable cause = e;
	    if (e.getCause() != null) cause = e.getCause();
	    ServerMain.serverLogger.warning(task.getClass().getSimpleName() + " caused: "
		    + cause.toString());
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

    public static void ensureValidString(String filename, char[] forbiddenChars)
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

    public static String omitRoot(String filePath, String rootPath)
    {
	return filePath.replaceFirst(Pattern.quote(rootPath), "");
    }
    
    public static String concatToRoot(String rootPath, String filePath)
    {
	return rootPath + File.separator + filePath;
    }

    public static void close(Closeable closeable) throws IOException
    {
	if (closeable != null) closeable.close();
    }

    // wenn replace = true, überschreibt er einfach
    // wenn false returned er, wenn File schon da
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
	    // int maxCount = (64 * 1024 * 1024) - (32 * 1024);
	    int maxCount = 64 * 1024; // 64kb is faster
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
	    IOUtil.close(srcChannel);
	    IOUtil.close(destChannel);
	}
    }
}
