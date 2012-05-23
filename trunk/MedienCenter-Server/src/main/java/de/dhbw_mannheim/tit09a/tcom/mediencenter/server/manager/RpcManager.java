package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.net.InetAddress;
import java.util.logging.Level;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote.ServerImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.SimonRegistryStatistics;

public class RpcManager extends Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public volatile static RpcManager	instance;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static RpcManager getInstance() throws Exception
	{
		if (instance == null)
		{
			synchronized (RpcManager.class)
			{
				if (instance == null)
				{
					instance = new RpcManager();
				}
			}
		}
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Registry	registry;
	private ServerImpl	server;
	private int			port;
	private String		bindname;

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected RpcManager() throws Exception
	{
		super(Level.ALL);
	}

	@Override
	protected void onStart() throws Exception
	{
		server = new ServerImpl();

		port = Server.REGISTRY_PORT;
		bindname = Server.BIND_NAME;

		registry = Simon.createRegistry(port);
		logger.info("SIMON registry @ {}:{}", InetAddress.getLocalHost(), port);

		registry.bind(bindname, server);
		logger.info("Interface of {} as '{}' available in registry", server, bindname);
	}

	@Override
	protected void onShutdown() throws Exception
	{
		// Shut down Simon Registry
		logger.info("Shutting down SIMON");
		SimonRegistryStatistics stats = registry.getStatistics();
		logger.info(ServerUtil.remoteStatsToString(stats));
		logger.info(ServerUtil.registryStatsToString(stats));

		registry.unbind(bindname);
		registry.stop();
	}

	public ServerImpl getServer()
	{
		return server;
	}

	public int getPort()
	{
		return port;
	}

	public String getBindname()
	{
		return bindname;
	}
}
