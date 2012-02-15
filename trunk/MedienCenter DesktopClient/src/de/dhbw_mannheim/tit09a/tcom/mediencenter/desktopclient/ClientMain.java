package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Lookup;
import de.root1.simon.RawChannel;
import de.root1.simon.exceptions.EstablishConnectionFailed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

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
			server.register("Donald Duck", "123");
			System.out.println(Thread.currentThread() + ": Logging in ...");
			Session session = server.login("Donald Duck", "123", clientCallbackImpl);
			System.out.println(Arrays.toString(session.listFileInfos("")));
			session.mkDir("Pictures", "neues_verzeichnis");
			System.out.println(Arrays.toString(session.listFileInfos("Pictures")));
			//session.copyFile("Pictures/txt.txt", "Videos/", true);
			// System.out.println(Arrays.toString(mySession.listFiles("Pictures")));
			//session.renameFile("Videos/meinedatei_copy2.txt", "meinedatei_copy2_renamed");
			// System.out.println(Arrays.toString(mySession.listFiles("Videos")));
			uploadFile(session, new File("D:\\mhertram\\Downloads\\eclipse-jee-indigo-SR1-win32-x86_64.zip"), "");
			System.out.println("ende");
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

	private static void uploadFile(Session session, File file, String destDirUri) throws IOException, ServerException
	{
		// get a RawChannel Token from server. This is needed to open the
		// RawChannel
		int token = session.openFileChannel(destDirUri, file.getName(), file.length());

		// with the remote object and token, tell SIMON that you need a
		// RawChannel
		RawChannel rawChannel = Simon.openRawChannel(token, session);

		// first, we open a FileChannel. This is thanks to Java NIO faster
		// than normal file operation
		FileChannel fileChannel = new FileInputStream(file).getChannel();

		// we send the file in 512byte packages through the RawChannel
		ByteBuffer data = ByteBuffer.allocate(8 * 1024);
		while (fileChannel.read(data) != -1)
		{
			// System.out.println("Read " + data.limit());
			rawChannel.write(data);
			data.clear();
		}

		// all data written. Now we can close the FileChannel
		fileChannel.close();

		// ... and also the RawChannel
		rawChannel.close();
	}
}