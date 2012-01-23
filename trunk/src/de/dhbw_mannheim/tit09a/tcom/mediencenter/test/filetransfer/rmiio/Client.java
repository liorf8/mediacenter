package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer.rmiio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.healthmarketscience.rmiio.RemoteInputStreamMonitor;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;

public class Client
{
    public class MyRemoteInputStreamMonitor extends RemoteInputStreamMonitor
    {
	@Override
	public void bytesMoved(RemoteInputStreamServer stream, int numBytes, boolean isReattempt)
	{
	    //System.out.printf("%d (is reattempt:%s)%n", numBytes, isReattempt);
	}

	@Override
	public void localBytesMoved(RemoteInputStreamServer stream, int numBytes)
	{
	   // System.out.printf("LOCAL: %d%n", numBytes);
	}
    }

    public static void main(String[] args)
    {
	Registry registry;
	try
	{
	    registry = LocateRegistry.getRegistry();
	    FileService fileService = (FileService) registry.lookup("FileService");

	    File file = new File("D:\\mhertram\\Downloads\\eclipse-jee-indigo-SR1-win32-x86_64.zip");
	    InputStream fileData = new FileInputStream(file);
	    RemoteInputStreamServer remoteFileData = new SimpleRemoteInputStream(fileData,
		    new Client().new MyRemoteInputStreamMonitor());
	    System.out.println("Successfull: "
		    + fileService.uploadFile(file, remoteFileData.export()));
	}
	catch (RemoteException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (NotBoundException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
