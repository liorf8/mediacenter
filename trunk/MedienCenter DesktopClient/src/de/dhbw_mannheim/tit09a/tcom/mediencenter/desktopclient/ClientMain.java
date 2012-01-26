package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Lookup;
import de.root1.simon.RawChannel;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.SimonRemoteException;

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

    public static void main(String[] args) throws IOException, LookupFailedException,
	    EstablishConnectionFailed
    {
	Lookup nameLookup = null;
	LoginService server = null;
	try
	{
	    // create a callback object
	    ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl();

	    // 'lookup' the server object
	    nameLookup = Simon.createNameLookup("127.0.0.1", 22222);
	    server = (LoginService) nameLookup.lookup(LoginService.BIND_NAME);

	    // use the serverobject as it would exist on your local machine
	    System.out.println(Thread.currentThread() + ": Logging in ...");
	    server.register("Donald Duck", "123");
	    Session mySession = server.login("Donald Duck", clientCallbackImpl);
	    System.out.println(Arrays.toString(mySession.listFiles("")));
	    mySession.mkDir("Pictures", "neues_verzeichnis");
	    System.out.println(Arrays.toString(mySession.listFiles("Pictures")));
	    //mySession.copyFile("Pictures\\meinedatei.txt", "Videos\\meinedatei_copy2.txt", true);
	    System.out.println(Arrays.toString(mySession.listFiles("Pictures")));
	    System.out.println(Arrays.toString(mySession.listFiles("Videos")));
	    uploadFile(mySession, new File("C:\\Users\\Max\\Downloads\\himym.avi"), "neuedatei.avi");
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

    private static void uploadFile(Session session, File file, String destPath) throws IllegalStateException, SimonRemoteException, IOException
    {
	// get a RawChannel Token from server. This is needed to open the
	// RawChannel
	int token = session.openFileChannel(destPath, file.length());

	// with the remote object and token, tell SIMON that you need a
	// RawChannel
	RawChannel rawChannel = Simon.openRawChannel(token, session);

	// first, we open a FileChannel. This is thanks to Java NIO faster
	// than normal file operation
	FileChannel fileChannel = new FileInputStream(file).getChannel();

	// we send the file in 512byte packages through the RawChannel
	ByteBuffer data = ByteBuffer.allocate(0xFFFF);
	while (fileChannel.read(data) != -1)
	{
	    rawChannel.write(data);
	    data.clear();
	}


	    // all data written. Now we can close the FileChannel
	    fileChannel.close();

	    // ... and also the RawChannel
	    rawChannel.close();
    }
}