package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.root1.simon.ClosedListener;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public class ServerConnectionImpl implements ServerConnection, ClosedListener
{

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public Logger								logger				= LoggerFactory.getLogger(this.getClass());

	Lookup										lookup;
	Server										server;
	Session										session;
	InfoPlayer									infoPlayer;
	StreamPlayer								streamPlayer;
	String										serverHost;
	int											serverRegistryPort;
	String										serverBindname;

	private ServerConnectionState				state;
	private ServerConnectionStateDisconnected	stateDisconnected	= new ServerConnectionStateDisconnected(this);
	private ServerConnectionStateConnected		stateConnected		= new ServerConnectionStateConnected(this);
	private ServerConnectionStateLoggedIn		stateLoggedIn		= new ServerConnectionStateLoggedIn(this);

	// CopyOnWriteArrayList: "A thread-safe variant of ArrayList" -> no sync needed on add/remove
	// "This is ordinarily too costly, but may be more efficient than alternatives when traversal operations vastly outnumber mutations" -> on
	// listeners this is the case
	private List<ServerConnectionStateListener>	listeners			= new CopyOnWriteArrayList<>();

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public ServerConnectionImpl()
	{
		logger.info("Init {}", this.getClass().getSimpleName());

		setState(stateDisconnected);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void addStateListener(ServerConnectionStateListener l)
	{
		listeners.add(l);
	}

	// --------------------------------------------------------------------------------
	public void removeStateListener(ServerConnectionStateListener l)
	{
		listeners.remove(l);
	}

	// --------------------------------------------------------------------------------
	public ServerConnectionState getState()
	{
		return state.getState();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void setServerHost(String serverHost)
	{
		state.setServerHost(serverHost);
	}

	// --------------------------------------------------------------------------------
	@Override
	public String getServerHost()
	{
		return state.getServerHost();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void setServerRegistryPort(int serverRegistryPort)
	{
		state.setServerRegistryPort(serverRegistryPort);
	}

	// --------------------------------------------------------------------------------
	@Override
	public int getServerRegistryPort()
	{
		return state.getServerRegistryPort();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void setServerBindname(String serverBindname)
	{
		state.setServerBindname(serverBindname);
	}

	// --------------------------------------------------------------------------------
	@Override
	public String getServerBindname()
	{
		return state.getServerBindname();
	}

	// --------------------------------------------------------------------------------
	@Override
	public Session getSession()
	{
		return state.getSession();
	}

	// --------------------------------------------------------------------------------
	@Override
	public InfoPlayer getInfoPlayer()
	{
		return state.getInfoPlayer();
	}

	// --------------------------------------------------------------------------------
	@Override
	public StreamPlayer getStreamPlayer()
	{
		return state.getStreamPlayer();
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
		setState(getState(ServerConnectionStateDisconnected.class));
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	<S extends ServerConnectionState> S getState(Class<S> stateClass)
	{
		if (stateClass == ServerConnectionStateDisconnected.class)
			return (S) stateDisconnected;

		if (stateClass == ServerConnectionStateConnected.class)
			return (S) stateConnected;

		if (stateClass == ServerConnectionStateLoggedIn.class)
			return (S) stateLoggedIn;

		else
			return null;
	}

	// --------------------------------------------------------------------------------
	void setState(ServerConnectionState newState)
	{
		ServerConnectionState oldState = state;
		// state has to be set before the listeners are notified, because they might call this instance immediately and then the state may be old
		state = newState;
		for (ServerConnectionStateListener l : listeners)
		{
			l.stateChanged(oldState, newState);
		}
	}

	// --------------------------------------------------------------------------------
	void doDisconnect()
	{
		lookup.removeClosedListener(server, this);
		lookup.release(server);

		lookup = null;
		server = null;
		session = null;
		infoPlayer = null;
		streamPlayer = null;
	}

	// --------------------------------------------------------------------------------
	void doConnect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed
	{
		lookup = Simon.createNameLookup(serverHost, serverRegistryPort);
		server = (Server) lookup.lookup(serverBindname);
		lookup.addClosedListener(server, this);
	}

	// --------------------------------------------------------------------------------
	void doLogin(String login, String pw, ClientCallback callback)
	{
		session = server.login(login, pw, callback);
		infoPlayer = session.getInfoPlayer();
		streamPlayer = session.getStreamPlayer();
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
