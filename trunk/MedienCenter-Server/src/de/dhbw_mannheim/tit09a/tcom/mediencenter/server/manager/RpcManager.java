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

	protected RpcManager() throws Exception
	{
		super(Level.ALL);
	}

	@Override
	protected void onStart() throws Exception
	{
		server = new ServerImpl();

		registry = Simon.createRegistry(InetAddress.getByName(Server.IP), Server.REGISTRY_PORT);
		logger.info("SIMON registry @ {}:{}", Server.IP, Server.REGISTRY_PORT);

		registry.bind(Server.BIND_NAME, server);
		logger.info("Bound {} as '{}' to registry", server, Server.BIND_NAME);
	}

	@Override
	protected void onShutdown() throws Exception
	{
		// Shut down Simon Registry
		logger.info("Shutting down SIMON");
		SimonRegistryStatistics stats = registry.getStatistics();
		logger.info(ServerUtil.remoteStatsToString(stats));
		logger.info(ServerUtil.registryStatsToString(stats));

		registry.unbind(ServerImpl.BIND_NAME);
		registry.stop();
	}

	public ServerImpl getServer()
	{
		return server;
	}
}
