package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;


public abstract class SimonConnectionState implements SimonConnection
{
	protected SimonConnectionImpl	connection;

	// --------------------------------------------------------------------------------
	public SimonConnectionState(SimonConnectionImpl connection)
	{
		this.connection = connection;
		connection.logger.info("Build {}", this.getClass().getSimpleName());
	}

	// --------------------------------------------------------------------------------
	@Override
	public SimonConnectionState getState()
	{
		return this;
	}

	// --------------------------------------------------------------------------------
	@Override
	public SimonConnectionConfig getConfig()
	{
		return connection.cfg;
	}

	// --------------------------------------------------------------------------------
	@Override
	public void setConfig(SimonConnectionConfig cfg)
	{
		connection.cfg = cfg;
	}

	// --------------------------------------------------------------------------------
	@Override
	public Session getSession()
	{
		throw new IllegalStateException(this.getClass().getSimpleName());
	}
	
	// --------------------------------------------------------------------------------
	@Override
	public Server getServer()
	{
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	// --------------------------------------------------------------------------------
	@Override
	public void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		throw new IllegalStateException(this.getClass().getSimpleName());
	}

	// --------------------------------------------------------------------------------
	@Override
	public void login(String login, String pw, ClientCallback callback) throws ServerException
	{
		throw new IllegalStateException(this.getClass().getSimpleName());
	}
	
	// --------------------------------------------------------------------------------
	@Override
	public void disconnect()
	{
		throw new IllegalStateException(this.getClass().getSimpleName());
	}
	
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}