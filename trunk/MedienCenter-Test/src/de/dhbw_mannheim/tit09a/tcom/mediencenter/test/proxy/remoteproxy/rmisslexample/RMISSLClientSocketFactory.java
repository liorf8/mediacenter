package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remoteproxy.rmisslexample;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class RMISSLClientSocketFactory implements RMIClientSocketFactory, Serializable
{

    private static final long serialVersionUID = 4354944192338382586L;

    public Socket createSocket(String host, int port) throws IOException
    {
	SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
	SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
	return socket;
    }

    public int hashCode()
    {
	return getClass().hashCode();
    }

    public boolean equals(Object obj)
    {
	if (obj == this)
	{
	    return true;
	}
	else if (obj == null || getClass() != obj.getClass())
	{
	    return false;
	}
	return true;
    }
}
