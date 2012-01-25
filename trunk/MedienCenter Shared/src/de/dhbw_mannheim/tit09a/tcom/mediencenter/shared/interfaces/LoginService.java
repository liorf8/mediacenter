package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.io.IOException;

public interface LoginService extends Service
{
    public Session login(String user, ClientCallback clientCallback);
    
    public void register(String login, String pw) throws IllegalArgumentException, IOException;
}
