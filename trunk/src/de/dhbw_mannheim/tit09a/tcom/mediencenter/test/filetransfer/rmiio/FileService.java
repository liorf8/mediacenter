package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer.rmiio;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.healthmarketscience.rmiio.RemoteInputStream;

public interface FileService extends Remote
{
    boolean uploadFile(File filePath, RemoteInputStream remoteFileData) throws RemoteException, IOException;
}
