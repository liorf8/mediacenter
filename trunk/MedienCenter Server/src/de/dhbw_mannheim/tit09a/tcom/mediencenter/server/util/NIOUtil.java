package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;

public class NIOUtil

{

    private NIOUtil()
    {

    }

    public static void copyFile(Path src, Path target, boolean replace) throws IOException
    {
	if (replace)
	{
	    Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS);
	}
	else
	{
	    Files.copy(src, target, LinkOption.NOFOLLOW_LINKS);
	}
    }
    
    public static void rename(Path src, String newName) throws IOException
    {
	Files.move(src, src.resolveSibling(newName));
    }
    
    
    public static void moveFile(Path src, Path target, boolean replace) throws IOException
    {
	if (replace)
	{
	    Files.move(src, target, StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS);
	}
	else
	{
	    Files.move(src, target, LinkOption.NOFOLLOW_LINKS);
	}
    }
    
    public static void createDir(Path dir) throws IOException
    {
	Files.createDirectory(dir, new FileAttribute<?>[0]);
    }
    
    public static void createDirs(Path dir) throws IOException
    {
	Files.createDirectories(dir, new FileAttribute<?>[0]);
    }
    
    public static void deleteIfExists(Path path) throws IOException
    {
	Files.deleteIfExists(path);
    }
    
    public static String probeContentType(Path path) throws IOException
    {
	return Files.probeContentType(path);
    }
}
