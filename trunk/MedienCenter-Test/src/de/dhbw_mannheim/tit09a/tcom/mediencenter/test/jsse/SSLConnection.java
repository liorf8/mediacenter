package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.jsse;

import java.io.*;
import javax.net.ssl.*;

public class SSLConnection
{
    public static void main(String[] args) throws IOException
    {
	int port = 1234;
	String hostName = "localhost";
	SSLSocketFactory sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
	SSLSocket socket = (SSLSocket) sslFact.createSocket(hostName, port);
	InputStream in = socket.getInputStream();
	OutputStream out = socket.getOutputStream();
	// Nun sicher lesen und schreiben
	in.close();
	out.close();
    }
}