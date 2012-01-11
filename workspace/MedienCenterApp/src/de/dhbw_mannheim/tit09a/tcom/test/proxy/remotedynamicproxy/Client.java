package de.dhbw_mannheim.tit09a.tcom.test.proxy.remotedynamicproxy;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client
{
    public static void main(String[] args) throws RemoteException, NotBoundException
    {
	try
	{
	    // alternative without URL path
	    // Registry registry = LocateRegistry.getRegistry();
	    // FileController fc = (FileController) registry.lookup("rmi://localhost/fc");
	    
	    // syntax: rmi://host:port/name
	    FileController fc = (FileController) Naming.lookup("filecontroller");
	    fc.addFile("max", "newfile.txt");
	    
	    LoginController lc = (LoginController) Naming.lookup("logincontroller");
	    lc.addUser("max2", "maxpw");
	    
	}
	catch (Exception e)
	{
	    System.err.println(e.getCause().toString());
	}

    }
}