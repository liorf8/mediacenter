package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.root1.simon.Lookup;
import de.root1.simon.exceptions.EstablishConnectionFailed;

import java.io.IOException;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.LookupFailedException;

public class ClientMain
{
	public static void main(String[] args) throws IOException, LookupFailedException, EstablishConnectionFailed
	{
		Lookup nameLookup = null;
		Server server = null;
		try
		{
			nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
			server = (Server) nameLookup.lookup(Server.BIND_NAME);
			
			server.login("Max", "pw", new DefaultClientCallback(null));
			server.login("Karo", "pw", new DefaultClientCallback(null));
			while(true)
			{
				
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			// and finally 'release' the serverobject to release to connection to the server
			nameLookup.release(server);
		}
	}
}