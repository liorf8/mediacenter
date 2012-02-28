package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.jsse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SSLSocketServer
{
    public static void main(String[] args)
    {
	try
	{
	    // create socket
	    ServerSocket servsock = new ServerSocket(1234);
	    while (true)
	    {

		// waiting for connection request
		System.out.println("Waiting...");
		Socket sock = servsock.accept();
		System.out.println("Accepted connection : " + sock);

		sock.close();

		System.out.println("Connection closed!");
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
}
