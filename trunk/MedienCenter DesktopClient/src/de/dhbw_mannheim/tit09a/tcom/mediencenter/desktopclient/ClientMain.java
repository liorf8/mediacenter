package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Lookup;
import de.root1.simon.exceptions.EstablishConnectionFailed;

import java.io.IOException;
import java.nio.file.Paths;

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

			// 'lookup' the server object
			// 127.0.0.1
			nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
			server = (Server) nameLookup.lookup(Server.BIND_NAME);

			// create a callback object
			ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl(nameLookup, server);

			// use the serverobject as it would exist on your local machine
			System.out.println(Thread.currentThread() + ": Registering ...");
			//server.register("Donald Duck", "123");
			System.out.println(Thread.currentThread() + ": Logging in ...");
			Session session = server.login("Donald Duck", "123", clientCallbackImpl);
			//System.out.println(session.listFileInfos(""));
			//session.createDir("Pictures", "neues_verzeichnis");
			//System.out.println(session.listFileInfos("Pictures"));
			//session.copyFile("Pictures/txt.txt", "Videos/", true);
			// System.out.println(Arrays.toString(mySession.listFiles("Pictures")));
			//session.renameFile("Videos/meinedatei_copy2.txt", "meinedatei_copy2_renamed");
			// System.out.println(Arrays.toString(mySession.listFiles("Videos")));
			clientCallbackImpl.sendFile(session, Paths.get("D:\\Downloads\\txt.txt"), "");
			System.out.println("ende1");
			//session.downloadFile("");
			System.out.println("ende2");
			// ...

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