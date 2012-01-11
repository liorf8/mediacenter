package de.dhbw_mannheim.tit09a.tcom.test.proxy.remotedynamicproxy;

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
	    // alternative without URL path
	    Registry registry = LocateRegistry.getRegistry("192.168.2.111");
	    FileController fc = (FileController) registry.lookup("FileController");
	    System.out.println(fc.getServerTime());
	    
	    
//	    // syntax: rmi://host:port/name
//	    FileController fc = (FileController) Naming.lookup("filecontroller");
//	    fc.addFile("max", "newfile.txt");
//	    System.out.println(fc.getServerTime());
//	    
//	    LoginController lc = (LoginController) Naming.lookup("logincontroller");
//	    lc.addUser("max2", "maxpw");
	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
}