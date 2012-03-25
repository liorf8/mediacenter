package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class SimonConnectionImpl implements SimonConnection, ClosedListener
{

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public Logger								logger				= LoggerFactory.getLogger(this.getClass());

	SimonConnectionConfig						cfg					= new SimonConnectionConfig();
	Lookup										lookup;
	Server										server;
	Session										session;

	private SimonConnectionState				state;
	private SimonConnectionStateDisconnected	stateDisconnected	= new SimonConnectionStateDisconnected(this);
	private SimonConnectionStateConnected		stateConnected		= new SimonConnectionStateConnected(this);
	private SimonConnectionStateLoggedIn		stateLoggedIn		= new SimonConnectionStateLoggedIn(this);

	// CopyOnWriteArrayList: "A thread-safe variant of ArrayList" -> no sync needed on add/remove
	// "This is ordinarily too costly, but may be more efficient than alternatives when traversal operations vastly outnumber mutations" -> on
	// listeners this is the case
	private List<SimonConnectionStateListener>	listeners			= new CopyOnWriteArrayList<>();

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public SimonConnectionImpl()
	{
		logger.info("Init {}", this.getClass().getSimpleName());

		setState(stateDisconnected);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void addStateListener(SimonConnectionStateListener l)
	{
		listeners.add(l);
	}

	// --------------------------------------------------------------------------------
	public void removeStateListener(SimonConnectionStateListener l)
	{
		listeners.remove(l);
	}

	// --------------------------------------------------------------------------------
	public SimonConnectionState getState()
	{
		return state.getState();
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void setConfig(SimonConnectionConfig cfg)
	{
		state.setConfig(cfg);
	}

	// --------------------------------------------------------------------------------
	@Override
	public SimonConnectionConfig getConfig()
	{
		return state.getConfig();
	}

	// --------------------------------------------------------------------------------
	@Override
	public Session getSession()
	{
		return state.getSession();
	}

	// --------------------------------------------------------------------------------
	@Override
	public Server getServer()
	{
		return state.getServer();
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		state.connect();
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void login(String login, String pw, ClientCallback callback) throws ServerException
	{
		state.login(login, pw, callback);
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void disconnect()
	{
		state.disconnect();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void closed()
	{
		setState(getState(SimonConnectionStateDisconnected.class));
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	<S extends SimonConnectionState> S getState(Class<S> stateClass)
	{
		if (stateClass == SimonConnectionStateDisconnected.class)
			return (S) stateDisconnected;

		if (stateClass == SimonConnectionStateConnected.class)
			return (S) stateConnected;

		if (stateClass == SimonConnectionStateLoggedIn.class)
			return (S) stateLoggedIn;

		else
			return null;
	}

	// --------------------------------------------------------------------------------
	void setState(SimonConnectionState newState)
	{
		SimonConnectionState oldState = state;
		// state has to be set before the listeners are notified, because they might call this instance immediately and then the state may be old
		state = newState;
		for (SimonConnectionStateListener l : listeners)
		{
			l.stateChanged(oldState, newState);
		}
	}

	// --------------------------------------------------------------------------------
	void doDisconnect()
	{
		lookup.removeClosedListener(server, this);
		lookup.release(server);
	}

	// --------------------------------------------------------------------------------
	void doConnect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		lookup = Simon.createNameLookup(cfg.serverIP, cfg.serverRegistryPort);
		server = (Server) lookup.lookup(cfg.serverBindname);
		lookup.addClosedListener(server, this);
	}

	// --------------------------------------------------------------------------------
	void doLogin(String login, String pw, ClientCallback callback)
	{
		session = server.login(login, pw, callback);
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
