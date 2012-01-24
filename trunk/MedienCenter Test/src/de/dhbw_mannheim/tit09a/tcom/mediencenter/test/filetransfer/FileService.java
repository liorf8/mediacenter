package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileService
{
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    FileService(int port, int poolSize) throws IOException
    {
	serverSocket = new ServerSocket(port);
	pool = Executors.newFixedThreadPool(poolSize);
    }

    void serve(File file)
    {
	try
	{
	    for (;;)
	    {
		System.out.println("Serving...");
		pool.execute(new Handler(serverSocket.accept(), file));
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
    private final File file;

    Handler(Socket socket, File file)
    {
	this.socket = socket;
	this.file = file;
    }

    public void run()
    {
	System.out.println(Thread.currentThread());
	System.out.println(socket + " requested " + file);
    }
}