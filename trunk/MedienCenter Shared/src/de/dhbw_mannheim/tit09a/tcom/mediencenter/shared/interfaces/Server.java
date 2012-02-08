package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;

public interface Server
{
    public static final String BIND_NAME = "server";
    public static final int REGISTRY_PORT = 22222;
    public static final String IP = "127.0.0.1"; // "192.168.2.111" - "127.0.0.1"
    
    public long serverTime() throws ServerException;

    public Session login(String user, String pw, ClientCallback callback) throws IllegalArgumentException, ServerException;
    
    public void register(String user, String pw) throws IllegalArgumentException, ServerException;
}
