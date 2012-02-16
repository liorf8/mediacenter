package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.Authenticator;

public class TestGenPw
{

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException
	{
		String genPw =  Authenticator.generatePw("SHA1PRNG", 10);
		System.out.println(genPw);
	}

}
