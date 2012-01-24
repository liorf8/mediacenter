package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remoteproxy.rmisslexample;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello
{
    private static final long serialVersionUID = -7551867853371248789L;
    private static final int PORT = 2019;

    public HelloImpl() throws Exception
    {
	super(PORT, new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
    }

    public String sayHello()
    {
	return "Hello World!";
    }

    public static void main(String args[])
    {

	// Create and install a security manager
	if (System.getSecurityManager() == null)
	{
	    System.setSecurityManager(new RMISecurityManager());
	}

	// Server
	// VM-arg: -Djava.security.policy=D:\mhertram\policy
	System.out.println(System.getProperty("java.security.policy"));
	System.setProperty("javax.net.ssl.keyStore", "D:\\mhertram\\keystore");
	System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

	try
	{
	    // Create SSL-based registry
	    Registry registry = LocateRegistry.createRegistry(PORT,
		    new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());

	    HelloImpl obj = new HelloImpl();

	    // Bind this object instance to the name "HelloServer"
	    registry.bind("HelloServer", obj);

	    System.out.println("HelloServer bound in registry");
	}
	catch (Exception e)
	{
	    System.out.println("HelloImpl err: " + e.getMessage());
	    e.printStackTrace();
	}
    }
}
