package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.regex.Pattern;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

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

    public static boolean isEmptyDir(File dir)
    {
	return dir.list().length == 0;
    }

    public static void mkDir(File dir) throws IOException
    {
	IOUtil.executeMkDir(dir);
    }

    public static void mkDirs(File[] dirs) throws IOException
    {
	for (File oneDir : dirs)
	{
	    IOUtil.executeMkDir(oneDir);
	}
    }

    public static boolean deleteDir(File dir, boolean deleteNotEmptyDir) throws IOException
    {
	if (IOUtil.isEmptyDir(dir))
	{
	    IOUtil.executeDelete(dir);
	    return true;
	}
	else
	{
	    if (deleteNotEmptyDir)
	    {
		System.out.println("NOT SUPPORTED YET!");
		return false;
		// later return true; (when is implemented)
	    }
	    return false;
	}
    }

    /**
     * @param dir
     * @param parentDirToOmit
     *            Can be "" so that nothing is omitted.
     * @return
     * @throws IOException
     */
    public static FileInfo[] listFileInfos(File dir, File parentDirToOmit) throws IOException
    {
	IOUtil.ensureExists(dir);
	IOUtil.ensureIsDir(dir);

	File[] files = dir.listFiles();
	FileInfo[] fileInfos = new FileInfo[files.length];
	File oneFile = null;
	for (int i = 0; i < files.length; i++)
	{
	    oneFile = files[i];
	    fileInfos[i] = new FileInfo(oneFile, parentDirToOmit);
	}
	return fileInfos;
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

    public static void ensureIsInParentDir(File parent, File file) throws IOException
    {
	if(!isInParentDir(parent, file))
	{
	    throw new IOException(parent+ " is not the parent of " +file);
	}
    }
    
    public static boolean isInParentDir(File parent, File file) throws IOException
    {
	if (file.getCanonicalPath().startsWith(parent.getCanonicalPath()))
	{
	    return true;
	}
	return false;
    }
}
