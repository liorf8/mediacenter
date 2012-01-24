package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.session;


public interface LoginService
{
    public Session login(String user, ClientCallback clientCallback);

}