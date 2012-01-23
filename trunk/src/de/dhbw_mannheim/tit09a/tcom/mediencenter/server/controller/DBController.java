package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.sql.Connection;

public interface DBController
{
    Connection getConnection();
}
