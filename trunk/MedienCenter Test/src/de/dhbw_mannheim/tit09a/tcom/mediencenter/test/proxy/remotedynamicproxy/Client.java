package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client
{
    public static void main(String[] args) throws RemoteException, NotBoundException
    {
	try
	{
	    // Get the RMI registry
	    Registry registry = LocateRegistry.getRegistry("localhost");
	    //Registry registry = LocateRegistry.getRegistry("192.168.2.111");
	    
	    // Get the Controllers
	    FileController fc = (FileController) registry.lookup(FileController.class.getSimpleName());
	    System.out.println(fc.getServerTime());
	    fc.addFile("max", "newfile.txt");
	    
	    LoginController lc = (LoginController) registry.lookup(LoginController.class.getSimpleName());
	    
	    String hash = lc.login("maxhertrampf", "123");
	    System.out.println(hash + "("+hash.length()+")");
	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
}