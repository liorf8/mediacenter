package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class ServerConnectionStateLoggedIn extends ServerConnectionState
{
	public ServerConnectionStateLoggedIn(ServerConnectionImpl ctrl)
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
	public InfoPlayer getInfoPlayer()
	{
		return connection.infoPlayer;
	}

	// --------------------------------------------------------------------------------
	@Override
	public StreamPlayer getStreamPlayer()
	{
		return connection.streamPlayer;
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
		connection.setState(connection.getState(ServerConnectionStateDisconnected.class));
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
