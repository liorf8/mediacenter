package de.dhbw_mannheim.tit09a.tcom.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;

public interface FileController extends Controller
{
    public void addFile(String sessionId, String filename) throws RemoteException;
}
