package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

public class LoginController
{
    private static LoginController instance;

    private LoginController()
    {

    }

    static synchronized LoginController getInstance()
    {
	if (instance == null) instance = new LoginController();
	return instance;
    }

    String validateSession(String login, String pw) throws IllegalAccessException
    {
	if (login == null || pw == null)
	{
	    throw new IllegalAccessException("Null argument!");
	}
	else if (login.equals("max") && pw.equals("max"))
	{
	    System.out.printf("validateSession() validated [%s]%n", login);
	    return "sessionid";
	}
	else
	{
	    throw new IllegalAccessException("Invalid login password combination.");
	}
    }

}
