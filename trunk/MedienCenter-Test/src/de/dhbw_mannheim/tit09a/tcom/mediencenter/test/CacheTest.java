package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.FileInfoCache;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.PathFileInfo;

public class CacheTest
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		FileInfoCache cache = new FileInfoCache(64);
		Path path = Paths.get("max23");
		long start = System.nanoTime();
		for (int i = 0; i < 100; i++)
		{
			cache.insert(new PathFileInfo(Paths.get("max" + i), "folder", true, i, i, true));
		}
		System.out.println(System.nanoTime() - start);

		System.out.println(cache.lookup(path));

		System.out.println(System.nanoTime() - start);
	}

}
