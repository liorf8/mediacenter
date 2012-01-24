package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.filetransfer;

import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server
{

    public static void main(String[] args)
    {

	try
	{

	    Registry registry = Simon.createRegistry(InetAddress.getByName("0.0.0.0"), 2000);

	    RawChannelServerImpl rcsi = new RawChannelServerImpl();
	    registry.bind(RawChannelServer.BIND_NAME, rcsi);

	    System.out.println(Thread.currentThread() + ": Server up and running!");

	    // Server is now running. If you whish to shutdown, call this lines:
	    // registry.stop();
	    // registry.unbind(RawChannelServer.BIND_NAME);

	}
	catch (UnknownHostException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
	catch (NameBindingException ex)
	{
	    ex.printStackTrace();
	}

    }

}