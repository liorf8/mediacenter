package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

public class IOUtil
{
    private IOUtil()
    {

    }

    public static String omitRoot(String filePath, String rootPath)
    {
	return filePath.replaceFirst(Pattern.quote(rootPath), "");
    }

    public static String concatToRoot(String rootPath, String filePath)
    {
	return rootPath + File.separator + filePath;
    }

    /**
     * @param src
     * @param dest
     * @param replace
     *            wenn replace = true, überschreibt er einfach. wenn false returned er, wenn File schon da
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File src, File dest, boolean replace) throws IOException
    {
	// do nothing if the dest exists and replace is false
	if (!replace && dest.exists())
	{
	    return false;
	}
	executeFileCopy(src, dest);
	return true;
    }

    public static boolean isFileOrEmptyDir(File file)
    {
	if (!file.isDirectory()) return true;
	return file.list().length == 0;
    }

    public static boolean deleteDir(File dir, boolean deleteNonEmptyDir) throws IOException
    {
	if (IOUtil.isFileOrEmptyDir(dir))
	{
	    IOUtil.executeDelete(dir);
	    return true;
	}
	else
	{
	    if (deleteNonEmptyDir)
	    {
		System.out.println("Deletion of non empty directories is not supported yet!");
		return false;
		// later return true; (when is implemented)
	    }
	    return false;
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

    public static boolean executeMkDir(File dir) throws IOException
    {
	if (!dir.exists())
	{
	    if (!dir.mkdir())
		throw new IOException("Could not create dir: " + dir.getAbsolutePath());
	    return true;
	}
	return false;
    }

    public static void executeMkDirs(File[] dirs) throws IOException
    {
	for (File oneDir : dirs)
	{
	    IOUtil.executeMkDir(oneDir);
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

    public static void close(Closeable closeable) throws IOException
    {
	if (closeable != null) closeable.close();
    }

    public static void executeFileCopy(File src, File dest) throws IOException
    {
	FileChannel srcChannel = null;
	FileChannel destChannel = null;
	try
	{
	    // inChannel.transferTo(0, inChannel.size(), outChannel);
	    // original -- apparently has trouble
	    // copying large files on Windows
	    // magic number for Windows, (64Mb - 32Kb)
	    srcChannel = new FileInputStream(src).getChannel();
	    destChannel = new FileOutputStream(dest).getChannel();
	    // int maxCount = (64 * 1024 * 1024) - (32 * 1024);
	    int maxCount = 0xFFFF; // 64kb-1 is faster
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

    public static boolean isOrIsInParentDir(File parentDir, File file) throws IOException
    {
	if (file.getCanonicalPath().startsWith(parentDir.getCanonicalPath()))
	{
	    return true;
	}
	return false;
    }

    public static void ensureIsOrIsInParentDir(File parent, File file) throws IOException
    {
	if (!isOrIsInParentDir(parent, file))
	{
	    throw new IOException(parent + " is not the parent of " + file);
	}
    }

    public static String nonExistingFilePath(File file)
    {
	int i = 1;
	String filePath = file.getAbsolutePath();
	while (file.exists())
	{
	    file = new File(filePath.substring(0, filePath.lastIndexOf('.')) + " (" + (i++)
		    + ")" + filePath.substring(filePath.lastIndexOf('.'), filePath.length()));
	}
	return file.getAbsolutePath();
    }
}
