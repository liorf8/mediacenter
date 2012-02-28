package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remoteproxy.rmisslexample;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient
{

    private static final int PORT = 2019;

    public static void main(String args[])
    {
	try
	{	    
	    
	    //Client
	    System.setProperty("javax.net.ssl.trustStore", "D:\\mhertram\\truststore");
	    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
	    
	    // Make reference to SSL-based registry
	    Registry registry = LocateRegistry
		    .getRegistry(InetAddress.getLocalHost().getHostName(), PORT,
			    new RMISSLClientSocketFactory());

	    // "obj" is the identifier that we'll use to refer
	    // to the remote object that implements the "Hello"
	    // interface
	    Hello obj = (Hello) registry.lookup("HelloServer");

	    String message = "blank";
	    message = obj.sayHello();
	    System.out.println(message + "\n");
	}
	catch (Exception e)
	{
	    System.out.println("HelloClient exception: " + e.getMessage());
	    e.printStackTrace();
	}
    }
}
