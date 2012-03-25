package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class SimonConnectionStateLoggedIn extends SimonConnectionState
{
	public SimonConnectionStateLoggedIn(SimonConnectionImpl ctrl)
	{
		super(ctrl);
	}

	// --------------------------------------------------------------------------------
	@Override
	public Session getSession()
	{
		return connection.session;
	}

	// --------------------------------------------------------------------------------
	@Override
	public Server getServer()
	{
		return connection.server;
	}

	// --------------------------------------------------------------------------------
	@Override
	public void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		// do nothing
	}

	// --------------------------------------------------------------------------------
	@Override
	public void login(String login, String pw, ClientCallback callback) throws ServerException
	{
		// do nothing
	}

	// --------------------------------------------------------------------------------
	@Override
	public void disconnect()
	{
		connection.doDisconnect();
		connection.setState(connection.getState(SimonConnectionStateDisconnected.class));
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
