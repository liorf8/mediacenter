package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;

import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class SimonConnectionStateDisconnected extends SimonConnectionState
{
	public SimonConnectionStateDisconnected(SimonConnectionImpl connectionImpl)
	{
		super(connectionImpl);
	}

	// --------------------------------------------------------------------------------
	@Override
	public void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		connection.doConnect();
		connection.setState(connection.getState(SimonConnectionStateConnected.class));
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
