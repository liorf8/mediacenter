package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.filetransfer;

import de.root1.simon.Lookup;
import de.root1.simon.RawChannel;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import de.root1.simon.exceptions.SimonRemoteException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class Client
{

    private static final String TEMPDIR = System.getProperty("java.io.tmpdir");
    private static final String TESTFILE_RECEIVER = TEMPDIR + "TestFileForReceiver.dat";
    private static final String TESTFILE_SENDER = TEMPDIR + "TestFile.dat";

    public static void main(String[] args)
    {

	// create some testfiles
	createTestFile();

	try
	{
	    File file = new File(TESTFILE_SENDER);
	    long filesize = file.length();
	    
	    // Get connection to server^
	    Lookup nameLookup = Simon.createNameLookup("192.168.2.111", 2000);
	    //Lookup nameLookup = Simon.createNameLookup(InetAddress.getLocalHost(), 2000);
	    RawChannelServer rcs = (RawChannelServer) nameLookup.lookup(RawChannelServer.BIND_NAME);

	    // get a RawChannel Token from server. This is needed to open the
	    // RawChannel
	    int token = rcs.openFileChannel(TESTFILE_RECEIVER, filesize);

	    // with the remote object and token, tell SIMON that you need a
	    // RawChannel
	    RawChannel rawChannel = Simon.openRawChannel(token, rcs);

	    // first, we open a FileChannel. This is thanks to Java NIO faster
	    // than normal file operation
	    FileChannel fileChannel = new FileInputStream(file).getChannel();

	    // we send the file in 512byte packages through the RawChannel
	    ByteBuffer data = ByteBuffer.allocate(512);
	    while (fileChannel.read(data) != -1)
	    {
		rawChannel.write(data);
		data.clear();
	    }

	    // all data written. Now we can close the FileChannel
	    fileChannel.close();

	    // ... and also the RawChannel
	    rawChannel.close();

	    nameLookup.release(rcs);

	}
	catch (UnknownHostException ex)
	{
	    ex.printStackTrace();
	}
	catch (LookupFailedException ex)
	{
	    ex.printStackTrace();
	}
	catch (SimonRemoteException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
	catch (EstablishConnectionFailed e)
	{
	    e.printStackTrace();
	}

	// finally we clean up our temporary testfiles
	cleanUpFiles();
    }

    /**
     * Generates a test file
     */
    private static void createTestFile()
    {
	try
	{
	    FileChannel fc = new FileOutputStream(new File(TESTFILE_SENDER)).getChannel();
	    Random r = new Random(System.currentTimeMillis());
	    byte[] data = new byte[1000];
	    for (int i = 0; i < 10; i++)
	    {
		r.nextBytes(data);
		fc.write(ByteBuffer.wrap(data));
	    }
	    fc.close();
	}
	catch (FileNotFoundException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}
    }

    /**
     * Cleans up testfiles
     */
    private static void cleanUpFiles()
    {
	File f1 = new File(TESTFILE_SENDER);
	f1.delete();
	File f2 = new File(TESTFILE_RECEIVER);
	f2.delete();
    }
}