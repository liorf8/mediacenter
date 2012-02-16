package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class RegisterTest
{
	private static String user = "Max";
	private static String pw = "pw";
	/**
	 * @param args
	 * @throws UnknownHostException 
	 * @throws EstablishConnectionFailed 
	 * @throws LookupFailedException 
	 * @throws ServerException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws UnknownHostException, LookupFailedException, EstablishConnectionFailed, IllegalArgumentException, ServerException
	{
		long start;
		start = System.currentTimeMillis();
		Lookup nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
		Server server = (Server) nameLookup.lookup(Server.BIND_NAME);
		System.out.println("Gotten server in " + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		for(int i=0; i<100; i++)
		{
			server.register(user+i, pw);
		}
		System.out.println("Registered in " + (System.currentTimeMillis() - start));

	}

}
