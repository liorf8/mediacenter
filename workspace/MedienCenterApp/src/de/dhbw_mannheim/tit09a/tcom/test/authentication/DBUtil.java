package de.dhbw_mannheim.tit09a.tcom.test.authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtil
{

    public static boolean entryExists(Connection con, String table, String primaryKey, String value) throws SQLException
    {
	PreparedStatement ps = con.prepareStatement("SELECT "+primaryKey+" FROM "+table+" WHERE "+primaryKey+" = ?");
	ps.setString(1, value);
	return ps.executeQuery().next();
    }
}
