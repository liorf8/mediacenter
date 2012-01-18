package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.net.*;
import java.io.*;

public class FileServer
{

    private static void sendFile(Socket sock, File file) throws IOException
    {
	// read file
	byte[] sendBuffer = new byte[sock.getSendBufferSize()];
	FileInputStream fis = new FileInputStream(file);
	BufferedInputStream bis = new BufferedInputStream(fis);

	// send file
	System.out.printf("Sending File '%s' (%d Bytes)...%n", file.getAbsolutePath(), file.length());
	OutputStream os = sock.getOutputStream();
	while (bis.read(sendBuffer) > -1)
	{
	    try
	    {
		Thread.sleep(10);
		os.write(sendBuffer);
	    }
	    catch (InterruptedException ignore)
	    {
	    }
	}

	// flush and close
	System.out.println("Sending complete.");
	os.flush();
	os.close();
    }

    public static void main(String[] args) throws IOException
    {
	// create socket
	ServerSocket servsock = new ServerSocket(13267);
	while (true)
	{
	    // waiting for connection request
	    System.out.println("Waiting...");
	    Socket sock = servsock.accept();
	    System.out.println("Accepted connection : " + sock);
	    
	    sendFile(sock, new File("C:\\Users\\Max\\Downloads\\himym.avi"));

	    sock.close();
	}
    }
}
