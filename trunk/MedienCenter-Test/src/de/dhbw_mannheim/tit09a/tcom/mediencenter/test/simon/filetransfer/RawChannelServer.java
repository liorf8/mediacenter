package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.filetransfer;

import de.root1.simon.exceptions.SimonRemoteException;

public interface RawChannelServer
{

    public static final String BIND_NAME = "RawChannelFileTransfer";

    public int openFileChannel(String filename, long fileSize) throws SimonRemoteException;

    public byte[] getFileBytes(String filename) throws SimonRemoteException;

}