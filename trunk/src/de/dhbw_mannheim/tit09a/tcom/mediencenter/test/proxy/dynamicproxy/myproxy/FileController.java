package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.dynamicproxy.myproxy;

import java.io.File;
import java.lang.reflect.Proxy;

public class FileController extends Controller implements IFileController
{

    public static IFileController newFileControllerInstance(Class<?>[] interfaces,
	    SessionHandler sessionHandler)
    {
	ClassLoader loader = FileController.class.getClassLoader();
	return (IFileController) Proxy.newProxyInstance(loader, interfaces, sessionHandler);
    }

    @Override
    public boolean mkDir(File parentDir, File dir)
    {
	System.out.println(String.format("Making new directory '%s' in '%s'.",
		parentDir.getAbsolutePath(), dir.getAbsolutePath()));
	return true;
    }

    public boolean deleteDir(File dir)
    {
	System.out.println(String.format("Deleting directory '%s'.", dir.getAbsolutePath()));
	return true;
    }

}
