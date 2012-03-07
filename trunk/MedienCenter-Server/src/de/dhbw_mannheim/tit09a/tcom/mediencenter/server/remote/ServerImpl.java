package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.net.InetSocketAddress;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.UserManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.AuthenticationException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Server.class })
public class ServerImpl implements Server
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private static final long			serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance --------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	/**
	 * This is where all user sessions are stored. It is highly important that the ServerImpl does this itself because of the way SIMON works. This is
	 * the only way, unreferencing works 100% correctly.
	 */
	private final Vector<SessionImpl>	userSessions		= new Vector<SessionImpl>();

	// --------------------------------------------------------------------------------
	// -- Public Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public long serverTime() throws ServerException
	{
		try
		{
			return System.currentTimeMillis();
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public long register(String login, String pw) throws ServerException
	{
		try
		{
			long id = -1L;
			Connection con = DatabaseManager.getInstance().getClientConnection();
			NFileManager fileMan = NFileManager.getInstance();
			try
			{
				// Insert user if does not exist
				id = UserManager.getInstance().insertUser(con, login, pw);

				// Create User's Dirs
				fileMan.createUserDirs(id);

				con.commit(); // IMPORTANT! Is not done inside insertUser()!
				return id;
			}
			catch (Exception e)
			{
				ServerMain.INVOKE_LOGGER.error("Rolling back...");
				DatabaseManager.rollback(con);
				throw e;
			}
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.INVOKE_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public Session login(String login, String pw, ClientCallback callback) throws ServerException
	{
		try
		{
			Connection con = DatabaseManager.getInstance().getClientConnection();
			UserManager userMan = UserManager.getInstance();

			// Get id. Can result in -1L if user not found
			long id = userMan.idForLogin(con, login);
			ServerMain.INVOKE_LOGGER.debug("Id:{}", id);

			// Authenticate the user
			if (!userMan.authenticate(con, id, pw))
			{
				throw new AuthenticationException(login, pw);
			}

			// Get the exact login (user can login with Max or max or MAX or ...)
			// TODO get that earlier with the id
			login = userMan.loginForId(con, id);

			// Check if user is already logged in and log out if not on the same client.
			List<InetSocketAddress> addresses = getClientInetSocketAddresses(id);
			if (!addresses.isEmpty())
			{
				String msg = "You are currently also logged in on " + addresses + ".\nYou may want to log yourself out on that computers.";
				callback.message(msg, JOptionPane.INFORMATION_MESSAGE);
			}

			// If everything was correct: Create Session
			String sessionId = UserManager.generateSessionId();
			SessionImpl session = new SessionImpl(id, login, sessionId, callback, this);
			addUserSession(session);
			callback.message("Successfully logged in " + login + "!", JOptionPane.INFORMATION_MESSAGE);
			return session;
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.INVOKE_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void resetPw(String login) throws ServerException
	{
		try
		{
			UserManager.getInstance().resetPw(DatabaseManager.getInstance().getClientConnection(), login);
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.INVOKE_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Package Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	boolean addUserSession(SessionImpl userSession)
	{
		boolean changed = userSessions.add(userSession);
		ServerMain.INVOKE_LOGGER.info("Added session {}. Now {} user(s) are online.", userSession, userSessions.size());
		return changed;
	}

	// --------------------------------------------------------------------------------
	/**
	 * If a session get's unreferenced, the session is removed from the vector. This Method is called by the unreferenced() Method in Session.
	 * 
	 * @param userSession
	 *            The user session to remove.
	 * @return True if the Session existed.
	 */
	boolean removeUserSession(SessionImpl userSession)
	{
		boolean existed = userSessions.remove(userSession);
		ServerMain.INVOKE_LOGGER.info("Removed {} from session list. Now {} user(s) are online.", userSession, userSessions.size());
		return existed;
	}

	// --------------------------------------------------------------------------------
	// TODO maybe never used in the end
	@SuppressWarnings("unused")
	private SessionImpl getSession(String sessionId)
	{
		ServerMain.INVOKE_LOGGER.debug("ENTRY {}", sessionId);
		synchronized (userSessions)
		{
			ServerMain.INVOKE_LOGGER.debug("userSessions: {}", userSessions);
			for (SessionImpl oneSession : userSessions)
			{
				// ServerMain.INVOKE_LOGGER.trace("oneSession: {}", oneSession);
				if (sessionId.equals(oneSession.getSessionId()))
				{
					ServerMain.INVOKE_LOGGER.debug("EXIT {}", oneSession);
					return oneSession;
				}
			}
		}
		ServerMain.INVOKE_LOGGER.debug("EXIT null");
		return null;
	}
	
	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private List<InetSocketAddress> getClientInetSocketAddresses(long id)
	{
		ServerMain.INVOKE_LOGGER.debug("ENTRY {}", id);
		List<InetSocketAddress> addresses = null;
		synchronized (userSessions)
		{
			for (SessionImpl oneSession : userSessions)
			{
				if (oneSession.getId() == id)
				{
					if (addresses == null)
						addresses = new ArrayList<>();
					addresses.add(SessionImpl.getClientInetSocketAddress(oneSession));
				}
			}
		}
		if (addresses == null)
			addresses = Collections.emptyList();
		ServerMain.INVOKE_LOGGER.debug("EXIT {}", addresses);
		return addresses;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
