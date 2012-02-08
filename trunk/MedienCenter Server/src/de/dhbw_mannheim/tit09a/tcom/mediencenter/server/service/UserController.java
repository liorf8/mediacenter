package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

public class UserController
{
    public static String changeAttr(String user, String attr, String newValue)
    {
	return String.format("Attribute '%s' of user '%s' changed to '%s'.", attr, user, newValue);
    }
}
