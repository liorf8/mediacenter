package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;
import java.util.Date;

public class FileControllerImpl extends ControllerUtil implements FileController
{
    @Override
    public void addFile(String sessionId, String filename) throws RemoteException
    {
	System.out.printf("File '%s' added!%n", filename);
    }
    
    @Override
    public Date getServerTime() throws RemoteException
    {
	return new Date();
    }
}
