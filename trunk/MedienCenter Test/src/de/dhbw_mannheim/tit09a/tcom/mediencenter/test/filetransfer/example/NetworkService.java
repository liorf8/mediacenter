package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class NetworkService
{
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    public NetworkService(int port, int poolSize) throws IOException
    {
	serverSocket = new ServerSocket(port);
	pool = Executors.newFixedThreadPool(poolSize);
    }

    public void serve()
    {
	try
	{
	    for (;;)
	    {
		pool.execute(new Handler(serverSocket.accept()));
	    }
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	    pool.shutdown();
	}
    }
}

class Handler implements Runnable
{
    private final Socket socket;

    Handler(Socket socket)
    {
	this.socket = socket;
    }

    public void run()
    {
	// read and service request
	System.out.println("Request from socket: "+socket);
    }
}