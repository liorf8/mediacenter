package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.io.InputStream;
import java.rmi.RemoteException;

public interface StreamController extends Controller
{
    public InputStream getStream(String sessionId, String filePath) throws RemoteException, IllegalAccessException;
}
