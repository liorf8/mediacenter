package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;

public class LoginControllerImpl extends AbstractController implements LoginController
{
    @Override
    public void registerUser(String login, String password) throws RemoteException
    {
	System.out.printf("registerUser executed!");
    }

    @Override
    public String login(String login, String pw) throws RemoteException
    {
	try
	{
	    SecureRandom random;
	    random = SecureRandom.getInstance("SHA1PRNG");
	    byte[] bSalt = new byte[8];
	    random.nextBytes(bSalt);
	    return new String(Hex.encodeHex(bSalt));
	}
	catch (NoSuchAlgorithmException e)
	{
	    e.printStackTrace();
	}

	System.out.printf("login executed!");

	return null;
    }

    @Override
    public void unregisterUser(String sessionId, String login) throws RemoteException,
	    IllegalAccessException
    {
	System.out.printf("unregisterUser executed!");

    }

    static boolean validateSession(Object sessionId) throws IllegalAccessException
    {
	System.out.printf("validateSession() [%s]%n", sessionId);
	if (sessionId == null)
	{
	    throw new IllegalAccessException("Illegal session ID '" + sessionId + "': null");
	}
	else if (!(sessionId instanceof String))
	{
	    throw new IllegalAccessException("Illegal session ID '" + sessionId + "': no String");
	}
	else if (sessionId.equals("max"))
	{
	    System.out.printf("validateSession() validated [%s]%n", sessionId);
	    return true;
	}
	else
	{
	    throw new IllegalAccessException("Illegal session ID '" + sessionId + "': not known");
	}
    }

}
