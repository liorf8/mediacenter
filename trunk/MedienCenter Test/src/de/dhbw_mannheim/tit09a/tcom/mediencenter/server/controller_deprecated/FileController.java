package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.io.File;
import java.io.IOException;


public class FileController
{
    private static FileController instance;

    private FileController()
    {

    }

    public static synchronized FileController getInstance()
    {
	if(instance == null)
	    instance = new FileController();
	return instance;
    }

    void createBasicDirs(String login) throws IOException
    {
	System.out.println("Created basic directories!");
    }

    File getUsersRoot()
    {
	return new File("C:\\Users");
    }

    File getUserRoot(String login) throws IOException
    {
	File userRoot = new File(getUsersRoot().getAbsoluteFile(), login);
	if (!userRoot.exists())
	{
	    if (!userRoot.mkdir())
		throw new IOException("Could not create user root for " + login + "!");
	}
	return userRoot;
    }
}
