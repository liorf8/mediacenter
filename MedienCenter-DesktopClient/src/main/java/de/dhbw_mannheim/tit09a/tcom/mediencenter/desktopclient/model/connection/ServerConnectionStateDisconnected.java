package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection;

import java.net.UnknownHostException;

import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class ServerConnectionStateDisconnected extends ServerConnectionState
{
	public ServerConnectionStateDisconnected(ServerConnectionImpl connectionImpl)
	{
		super(connectionImpl);
	}

	// --------------------------------------------------------------------------------
	@Override
	public void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		connection.doConnect();
		connection.setState(connection.getState(ServerConnectionStateConnected.class));
	}
	
	// --------------------------------------------------------------------------------
	@Override
	public void disconnect()
	{
		// nothing to do
	}
	
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
