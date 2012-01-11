package de.dhbw_mannheim.tit09a.tcom.test.proxy.dynamicproxy.myproxy;

import java.io.File;

public class MainApp
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	FileController fileController = new FileController();
	Class<?>[] interfaces = new Class[] { IFileController.class };
	SessionHandler sessionHandler = new SessionHandler(fileController, "pw");
	
	IFileController fileControllerProxy = FileController.newFileControllerInstance(interfaces, sessionHandler);
	fileControllerProxy.mkDir(new File(""), new File("mydir"));
	
	fileControllerProxy.deleteDir(new File("mydir"));

    }

}
