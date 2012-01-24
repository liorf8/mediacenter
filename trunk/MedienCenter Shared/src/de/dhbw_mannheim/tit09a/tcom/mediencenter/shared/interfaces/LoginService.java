package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

public interface LoginService
{
    public Session login(String user, ClientCallback clientCallback);
    
    public boolean register(String login, String pw);
}
