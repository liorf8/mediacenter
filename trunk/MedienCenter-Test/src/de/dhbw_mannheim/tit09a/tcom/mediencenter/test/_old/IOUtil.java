package de.dhbw_mannheim.tit09a.tcom.mediencenter.test._old;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class IOUtil
{
	private IOUtil()
	{

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
		if (!file.isDirectory())
			return true;
		return file.list().length == 0;
	}

	public static List<File> listAllFiles(File file)
	{
		if (file.isDirectory())
		{
			List<File> allFiles = new ArrayList<File>();
			Deque<File> allDirs = new ArrayDeque<File>();
			allDirs.push(file);
			while (!allDirs.isEmpty())
			{
				File[] files = file.listFiles();
				if (files != null) // .lnk files return null
				{
					for (File oneFile : file.listFiles())
					{
						// System.out.println("Found " + oneFile);
						allFiles.add(oneFile);

						if (oneFile.isDirectory())
						{
							// System.out.println("Found dir " + oneFile);
							allDirs.push(oneFile);
						}
					}
				}
				file = allDirs.poll();
			}
			return allFiles;
		}
		return Collections.emptyList();
	}

	public static long fullFileSize(File file) throws IOException
	{
		long fullFileSize = file.length();
		if (file.isDirectory())
		{
			for(File oneFile : listAllFiles(file))
			{
				fullFileSize += oneFile.length();
			}
		}
		return fullFileSize;
	}

	public static int deleteAllFiles(File file) throws IOException
	{
		int deletedFiles = 0;
		if (file.isDirectory())
		{
			List<File> allFiles = listAllFiles(file);
			// delete all non directories...

			for (File oneFile : allFiles)
			{
				if (!oneFile.isDirectory())
				{
					executeDelete(oneFile);
					allFiles.remove(oneFile);
					deletedFiles++;
				}
			}
			// delete all other files (directories)
			for (File oneFile : allFiles)
			{
				executeDelete(oneFile);
				deletedFiles++;
			}
		}
		else
		{
			executeDelete(file);
			deletedFiles++;
		}

		return deletedFiles;
	}

	public static void ensureExists(File file) throws IOException
	{
		if (!file.exists())
			throw new IOException("File does not exist: " + file.getAbsolutePath());
	}

	public static void ensureDoesNotExist(File file) throws IOException
	{
		if (file.exists())
			throw new IOException("File already exists: " + file.getAbsolutePath());
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
		if (!file.delete())
			throw new IOException("Could not delete: " + file.getAbsolutePath());
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

	public static int executeMkFullDirPath(File dir) throws IOException
	{
		if (dir.exists())
			return 0;
		Deque<File> parentFilesToCreate = new ArrayDeque<File>();
		parentFilesToCreate.push(dir);
		while (!dir.getParentFile().exists())
		{
			parentFilesToCreate.push(dir.getParentFile());
			dir = dir.getParentFile();
		}
		int createdDirs = 0;
		while (!parentFilesToCreate.isEmpty())
		{
			executeMkDir(parentFilesToCreate.pop());
			createdDirs++;
		}
		return createdDirs;
	}

	public static void executeMkDirs(File[] dirs) throws IOException
	{
		for (File oneDir : dirs)
		{
			IOUtil.executeMkDir(oneDir);
		}
	}

	public static void executeMkFullDirsPaths(File[] dirs) throws IOException
	{
		for (File oneDir : dirs)
		{
			IOUtil.executeMkFullDirPath(oneDir);
		}
	}

	public static void executeRenameTo(File file, File dest) throws IOException
	{
		if (!file.renameTo(dest))
			throw new IOException("Could not rename file '" + file.getAbsolutePath() + "' to '" + dest.getAbsolutePath() + "'");
	}

	public static void executeCreateNewFile(File file) throws IOException
	{
		if (!file.createNewFile())
			throw new IOException("Could create  file: " + file.getAbsolutePath());
	}

	public static void close(Closeable closeable) throws IOException
	{
		if (closeable != null)
			closeable.close();
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
			file = new File(filePath.substring(0, filePath.lastIndexOf('.')) + " (" + (i++) + ")"
					+ filePath.substring(filePath.lastIndexOf('.'), filePath.length()));
		}
		return file.getAbsolutePath();
	}

	public static String convertStreamToString(InputStream is) throws IOException
	{
		/*
		 * To convert the InputStream to String we use the Reader.read(char[] buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to produce the string.
		 */
		if (is != null)
		{
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			}
			finally
			{
				is.close();
			}
			return writer.toString();
		}
		else
		{
			return "";
		}
	}

	// --------------------------------------------------------------------------------
	public static String resourceToString(String path) throws IOException
	{
		return convertStreamToString(IOUtil.class.getResourceAsStream(path));
	}

}
