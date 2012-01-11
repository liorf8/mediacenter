package de.dhbw_mannheim.tit09a.tcom.test.authentication;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthMain
{
    public static void main(String[] args)
    {
	    try
	    {
	      Class.forName( "org.hsqldb.jdbcDriver" );
	    }
	    catch ( ClassNotFoundException e )
	    {
	      System.err.println( "Keine Treiber-Klasse!" );
	      return;
	    }

	    Connection con = null;


	    try
	    {
	      con = DriverManager.getConnection( "jdbc:hsqldb:file:E:/Java/Studienarbeit/hsqldb/MedienCenterDB; shutdown=true", "sa", "" );
	      PasswordManager pm = new PasswordManager();
	      //pm.creerTable(con);
	      
	      long start = System.currentTimeMillis();
	      //pm.createUser(con, "Tobi", "tobipw");
	      long afterCreate = System.currentTimeMillis();
	      
	      boolean auth = pm.authenticate(con, "Tobi", "tobipw");
	      long afterAuth = System.currentTimeMillis();
	      
	      System.out.println(auth);
	      System.out.println("Elapsed time (ms):");
	      System.out.println("Creation: "+(afterCreate-start));
	      System.out.println("Auth: "+(afterAuth-afterCreate));

	    }
	    catch ( SQLException e )
	    {
	      e.printStackTrace();
	    }
	    catch (NoSuchAlgorithmException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }


	    finally
	    {
	      if ( con != null )
	        try { con.close(); } catch ( SQLException e ) { e.printStackTrace(); }
	    }
    }

}
