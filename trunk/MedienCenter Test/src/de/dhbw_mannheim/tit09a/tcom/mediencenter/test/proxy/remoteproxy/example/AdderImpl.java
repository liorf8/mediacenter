package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remoteproxy.example;

import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class AdderImpl implements Adder 
{ 
  public int add( int x, int y ) 
  { 
	try
	{
	    System.out.println("Clienthost: " +RemoteServer.getClientHost());
	}
	catch (ServerNotActiveException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    return x + y; 
  } 
}