package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class SimonConnectionStateConnected extends SimonConnectionState
{
	public SimonConnectionStateConnected(SimonConnectionImpl ctrl)
	{
		super(ctrl);
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
		connection.doLogin(login, pw, callback);
		connection.setState(connection.getState(SimonConnectionStateLoggedIn.class));
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
