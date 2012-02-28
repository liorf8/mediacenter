package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class NIOUtil

{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static final FileSystem	FILE_SYSTEM	= FileSystems.getDefault();

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static String probeContentType(Path path) throws IOException
	{
		return Files.probeContentType(path);
	}

	// --------------------------------------------------------------------------------
	/**
	 * Much slower than per File.
	 */
	@Deprecated
	public static long sizePerAttr(Path path) throws IOException
	{
		return (Long) Files.getAttribute(path, "size", LinkOption.NOFOLLOW_LINKS);
	}

	// --------------------------------------------------------------------------------
	/**
	 * Slower than per File.
	 */
	@Deprecated
	public static long sizePerChannel(Path path) throws IOException
	{
		return Files.newByteChannel(path, StandardOpenOption.READ).size();
	}

	// --------------------------------------------------------------------------------
	public static long sizePerFile(Path path)
	{
		return path.toFile().length();
	}

	// --------------------------------------------------------------------------------
	/**
	 * Slower than per File.
	 */
	@Deprecated
	public static long lastModifiedPerAttr(Path path) throws IOException
	{
		return ((FileTime) Files.getAttribute(path, "lastModifiedTime", LinkOption.NOFOLLOW_LINKS)).toMillis();
	}

	// --------------------------------------------------------------------------------
	public static long lastModifiedPerFile(Path path)
	{
		return path.toFile().lastModified();
	}

	// --------------------------------------------------------------------------------
	/**
	 * Slower than per File.
	 */
	@Deprecated
	public static boolean isDirPerPaths(Path path)
	{
		return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
	}

	// --------------------------------------------------------------------------------
	public static boolean isDirPerFile(Path path)
	{
		return path.toFile().isDirectory();
	}

	// --------------------------------------------------------------------------------
	public static boolean isRegularFile(Path path)
	{
		try
		{
			return (Boolean) Files.getAttribute(path, "isRegularFile", LinkOption.NOFOLLOW_LINKS);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	// --------------------------------------------------------------------------------
	public static Path rename(Path path, String newName) throws IOException
	{
		return rename(path, newName, true);
	}

	// --------------------------------------------------------------------------------
	public static Path rename(Path path, String newName, boolean autoRename) throws IOException
	{
		try
		{
			return Files.move(path, path.resolveSibling(newName));
		}
		catch (FileAlreadyExistsException e)
		{
			if (autoRename)
				return Files.move(path, autoRename(path.resolveSibling(newName)));
			else
				throw e;
		}
	}

	// --------------------------------------------------------------------------------
	public static Path createDir(Path parentDir, String dirName) throws IOException
	{
		return createDir(parentDir, dirName, true);
	}

	// --------------------------------------------------------------------------------
	public static Path createDir(Path parentDir, String dirName, boolean autoRename) throws IOException
	{
		try
		{
			return Files.createDirectory(parentDir.resolve(dirName), new FileAttribute<?>[0]);
		}
		catch (FileAlreadyExistsException e)
		{
			if (autoRename)
				return Files.createDirectory(autoRename(parentDir.resolve(dirName)), new FileAttribute<?>[0]);
			else
				throw e;
		}
	}

	// --------------------------------------------------------------------------------
	public static Path createAllDirs(Path dir) throws IOException
	{
		return Files.createDirectories(dir, new FileAttribute<?>[0]);
	}

	public static int copy(Path src, Path targetDir, boolean replace) throws IOException
	{
		CopyOption[] copyOptions;
		if (replace)
			copyOptions = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS };
		else
			copyOptions = new CopyOption[] { LinkOption.NOFOLLOW_LINKS };

		// target is a file with the same name as src within the targetDir
		Path target = targetDir.resolve(src.getFileName());
		if (!Files.isDirectory(src, LinkOption.NOFOLLOW_LINKS))
		{
			Files.copy(src, target, copyOptions);
			return 1;
		}
		else
		{
			CopyFileTreeVisitor visitor = new CopyFileTreeVisitor(src, target, copyOptions);
			Files.walkFileTree(src, visitor);
			return visitor.getFilesCopied();
		}
	}

	// --------------------------------------------------------------------------------
	public static int move(Path src, Path targetDir, boolean replace) throws IOException
	{
		CopyOption[] copyOptions;
		if (replace)
			copyOptions = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS };
		else
			copyOptions = new CopyOption[] { LinkOption.NOFOLLOW_LINKS };

		// target is a file with the same name as src within the targetDir
		Path target = targetDir.resolve(src.getFileName());
		if (!Files.isDirectory(src, LinkOption.NOFOLLOW_LINKS))
		{
			Files.move(src, target, copyOptions);
			return 1;
		}
		else
		{
			MoveFileTreeVisitor visitor = new MoveFileTreeVisitor(src, target, copyOptions);
			Files.walkFileTree(src, visitor);
			return visitor.getFilesMoved();
		}
	}

	// --------------------------------------------------------------------------------
	public static int delete(Path path, boolean deleteNotEmptyDir) throws IOException
	{
		try
		{
			if (Files.deleteIfExists(path))
				return 1;
			else
				return 0;
		}
		catch (DirectoryNotEmptyException e)
		{
			if (deleteNotEmptyDir)
			{
				DeleteFileTreeVisitor visitor = new DeleteFileTreeVisitor();
				Files.walkFileTree(path, visitor);
				return visitor.getDeletedFiles();
			}
			else
				throw e;
		}
	}

	// --------------------------------------------------------------------------------
	public static long fileTreeSize(Path dir) throws IOException
	{
		if (!Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS))
			return dir.toFile().length();
		CumulateLengthsVisitor visitor = new CumulateLengthsVisitor();
		Files.walkFileTree(dir, visitor);
		return visitor.getCumulatedLength();
	}

	// --------------------------------------------------------------------------------
	/**
	 * Cannot list the children of roots (f.i. C:\) or files of other users, because sometimes it cannot get the {@link BasicFileAttributes}. Then use
	 * File.listFiles() instead.
	 * 
	 * @param dir
	 * @param maxDepth
	 * @return
	 * @throws IOException
	 */
	public static List<Path> listChildren(Path dir, int maxDepth) throws IOException
	{
		ListChildrenVisitor visitor = new ListChildrenVisitor(dir);
		Files.walkFileTree(dir, EnumSet.noneOf(FileVisitOption.class), maxDepth, visitor);
		return visitor.getPaths();
	}

	// --------------------------------------------------------------------------------
	// -- FileVisitors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static class ListChildrenVisitor extends SimpleFileVisitor<Path>
	{
		private List<Path>	paths;
		private final Path	parent;

		public ListChildrenVisitor(Path parent)
		{
			this.parent = parent;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			if (paths == null)
				paths = new ArrayList<>();
			paths.add(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
		{
			if (paths == null)
				paths = new ArrayList<>();
			if (!dir.equals(parent)) // do not add the parent directory
				paths.add(dir);
			return FileVisitResult.CONTINUE;
		}

		public List<Path> getPaths()
		{
			if (paths == null)
				return Collections.emptyList();
			else
				return paths;
		}
	}

	// --------------------------------------------------------------------------------
	private static class CumulateLengthsVisitor extends SimpleFileVisitor<Path>
	{
		private long	cumulatedLength	= 0L;

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			cumulatedLength += file.toFile().length();
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
		{
			cumulatedLength += dir.toFile().length();
			return FileVisitResult.CONTINUE;
		}

		public long getCumulatedLength()
		{
			return cumulatedLength;
		}
	}

	// --------------------------------------------------------------------------------
	private static class CopyFileTreeVisitor extends SimpleFileVisitor<Path>
	{
		private final Path			source;
		private final Path			target;
		private final CopyOption[]	copyOptions;
		private int					filesCopied	= 0;

		public CopyFileTreeVisitor(Path source, Path target, CopyOption... copyOptions)
		{
			this.source = source;
			this.target = target;
			this.copyOptions = copyOptions;
			System.out.println("Copy: " + source + " to " + target + ", " + copyOptions);
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
		{
			System.out.println("Visiting dir: " + dir);
			Path targetdir = target.resolve(source.relativize(dir));
			System.out.println("targetdir: " + targetdir);
			try
			{
				Files.copy(dir, targetdir, copyOptions);
			}
			catch (FileAlreadyExistsException e)
			{
				if (!Files.isDirectory(targetdir))
					throw e;
			}
			filesCopied++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			System.out.println("Visiting file: " + file);
			Files.copy(file, target.resolve(source.relativize(file)), copyOptions);
			filesCopied++;
			return FileVisitResult.CONTINUE;
		}

		public int getFilesCopied()
		{
			return filesCopied;
		}
	}

	// --------------------------------------------------------------------------------
	private static class MoveFileTreeVisitor extends SimpleFileVisitor<Path>
	{
		private final Path			source;
		private final Path			target;
		private final CopyOption[]	copyOptions;
		private int					filesMoved	= 0;

		public MoveFileTreeVisitor(Path source, Path target, CopyOption... copyOptions)
		{
			this.source = source;
			this.target = target;
			this.copyOptions = copyOptions;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
		{
			Files.createDirectories(target.resolve(source.relativize(dir)), new FileAttribute<?>[0]);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			Files.move(file, target.resolve(source.relativize(file)), copyOptions);
			filesMoved++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
		{
			Files.delete(dir);
			filesMoved++;
			return FileVisitResult.CONTINUE;
		}

		public int getFilesMoved()
		{
			return filesMoved;
		}
	}

	// --------------------------------------------------------------------------------
	private static class DeleteFileTreeVisitor extends SimpleFileVisitor<Path>
	{
		private int	deletedFiles	= 0;

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
		{
			Files.delete(file);
			deletedFiles++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException
		{
			if (e == null)
			{
				Files.delete(dir);
				deletedFiles++;
				return FileVisitResult.CONTINUE;
			}
			else
			{
				// directory iteration failed
				throw e;
			}
		}

		public int getDeletedFiles()
		{
			return deletedFiles;
		}
	}

	// --------------------------------------------------------------------------------
	public static Path autoRename(Path path)
	{
		int i = 1;
		String name = path.normalize().toString();
		while (Files.exists(path, LinkOption.NOFOLLOW_LINKS))
		{
			if (name.indexOf('.') > 0) // has file ending
				path = Paths
						.get(name.substring(0, name.lastIndexOf('.')) + " (" + (i++) + ")" + name.substring(name.lastIndexOf('.'), name.length()));
			else
				path = Paths.get(name + " (" + (i++) + ")");
		}
		return path;
	}

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private NIOUtil()
	{

	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
