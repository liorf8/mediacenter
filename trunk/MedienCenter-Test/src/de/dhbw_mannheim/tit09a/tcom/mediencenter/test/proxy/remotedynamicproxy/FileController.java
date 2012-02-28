package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;
import java.util.Date;

public interface FileController extends Controller
{
    public void addFile(String sessionId, String filename) throws RemoteException, IllegalAccessException;
    
    public Date getServerTime() throws RemoteException;
}
