package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.filetransfer;

import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.exceptions.SimonRemoteException;

@SimonRemote(value = { RawChannelServer.class })
public class RawChannelServerImpl implements RawChannelServer
{
    public int openFileChannel(String filename, long fileSize) throws SimonRemoteException
    {
	int token = Simon.prepareRawChannel(new FileReceiver(filename, fileSize), this);
	System.out.println(Thread.currentThread() +": Returning token " +token + " for file " +filename);
	return token;
    }

    @Override
    public byte[] getFileBytes(String filename) throws SimonRemoteException
    {
	throw new UnsupportedOperationException("Please use openFileChannel() instead!");
    }
}