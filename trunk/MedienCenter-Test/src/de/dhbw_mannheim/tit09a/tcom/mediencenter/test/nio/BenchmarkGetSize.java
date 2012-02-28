package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;

public class BenchmarkGetSize
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		Path path = Paths.get("C:\\Users\\Max\\Downloads\\himym.avi");
		long start;
		
		System.out.println("Filesize:");
		start = System.nanoTime();
		NIOUtil.sizePerAttr(path);
		System.out.println(System.nanoTime()-start);
	
		
		start = System.nanoTime();
		NIOUtil.sizePerChannel(path);
		System.out.println(System.nanoTime()-start);
		
		start = System.nanoTime();
		NIOUtil.sizePerFile(path);
		System.out.println(System.nanoTime()-start);
		
		
		System.out.println("lastModified:");
		start = System.nanoTime();
		NIOUtil.lastModifiedPerAttr(path);
		System.out.println(System.nanoTime()-start);
		
		start = System.nanoTime();
		NIOUtil.lastModifiedPerFile(path);
		System.out.println(System.nanoTime()-start);
		
		
		System.out.println("isDirectory:");
		start = System.nanoTime();
		NIOUtil.isDirPerPaths(path);
		System.out.println(System.nanoTime()-start);
		
		start = System.nanoTime();
		NIOUtil.isDirPerFile(path);
		System.out.println(System.nanoTime()-start);
		
		start = System.nanoTime();
		System.out.println(System.nanoTime()-start);
	}

}
