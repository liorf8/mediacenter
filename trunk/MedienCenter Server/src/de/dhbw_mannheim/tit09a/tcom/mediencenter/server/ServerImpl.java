package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.rmi.ServerException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.AuthenticationException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Server.class })
public class ServerImpl implements Server
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private static final long	serialVersionUID	= 1L;

	// --------------------------------------------------------------------------------
	// -- Instance Variable(s) --------------------------------------------------------
	// --------------------------------------------------------------------------------
	// this is where all user sessions are stored
	private List<SessionImpl>	userSessions		= new ArrayList<SessionImpl>();

	// --------------------------------------------------------------------------------
	// -- Private/Package Method(s) ---------------------------------------------------
	// --------------------------------------------------------------------------------
	/**
	 * If a session get's unreferenced, the session is removed from the list. This Method is called by the unreferenced() Method in Session.
	 * 
	 * @param userSession
	 *            The user session to remove.
	 * @return True if the Session existed.
	 */
	boolean removeUserSession(SessionImpl userSession)
	{
		boolean existed = userSessions.remove(userSession);
		ServerMain.MAIN_LOGGER.info("Removed " + userSession + " from sessionlist. " + userSessions.size() + " user(s) are online.");
		return existed;
	}

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
			ServerMain.MAIN_LOGGER.error("Invocation caught exception", t);
			throw new ServerException(t.toString());
		}
	}


	// --------------------------------------------------------------------------------
	@Override
	public long register(String login, String pw) throws ServerException
	{
		try
		{
			long id = -1L;
			Connection con = Manager.getManager(DatabaseManager.class).getConnection();
			NFileManager fileMan = Manager.getManager(NFileManager.class);
			try
			{
				// Insert user if does not exist
				id = Manager.getManager(UserManager.class).insertUser(con, login, pw);

				// Create User's Dirs
				fileMan.createUserDirs(id);

				con.commit(); // IMPORTANT! Is not done inside insertUser()!
				return id;
			}
			catch (Exception e)
			{
				ServerMain.MAIN_LOGGER.error("Rolling back...");
				con.rollback();
				throw e;
			}
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.MAIN_LOGGER.info("User "+login+" caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + login + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}
	
	// --------------------------------------------------------------------------------
	/*
	 * Here Return<Session> cannot be used, because it leads to NotSerializableException: de.root1.simon.SimonProxy. You have to return the
	 * SessionImpl directly!
	 * 
	 * (non-Javadoc)
	 * 
	 * @see de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server#login(java.lang.String, java.lang.String,
	 * de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback)
	 */
	@Override
	public Session login(String login, String pw, ClientCallback callback) throws ServerException
	{
		try
		{
			Connection con = Manager.getManager(DatabaseManager.class).getConnection();
			UserManager userMan = Manager.getManager(UserManager.class);

			// Get id. Can result in -1L if user not found
			long id = userMan.idForLogin(con, login);
			ServerMain.MAIN_LOGGER.debug("Id: " + id);

			// Authenticate the user
			if (!userMan.authenticate(con, id, pw))
			{
				throw new AuthenticationException(login, pw);
			}
			
			// Get the not exact login (user can login with Max or max or MAX or ...)
			// TODO: get the exact login with the id (idForLogin())
			login = userMan.loginForId(con, id);

			// Check if user is already logged in and log out if not on the same client.
			for (SessionImpl otherSession : userSessions)
			{
				if (otherSession.getId() == id)
				{
					boolean isSameRemote = Simon.denoteSameRemoteObjekt(callback, otherSession.getClientCallback());
					// Is the user currently logged in on the same client machine?
					if (isSameRemote)
					{
						String msg = String.format("User %s (ID: %d) already logged in on your computer (%s). Please logout first.", login, id,
								Simon.getRemoteInetSocketAddress(callback));
						callback.message(msg, JOptionPane.WARNING_MESSAGE);
						return null;
					}
					// or on another machine
					String msg = String.format("Closing session because User %s (ID: %d) is logging in on %s.", login, id,
							Simon.getRemoteInetSocketAddress(callback));
					otherSession.getClientCallback().message(msg, JOptionPane.WARNING_MESSAGE);
					otherSession.getClientCallback().releaseConnection();
					msg = String.format("Logged out previous session for User %s (ID: %d) on %s.", login, id,
							Simon.getRemoteInetSocketAddress(otherSession.getClientCallback()));
					callback.message(msg, JOptionPane.INFORMATION_MESSAGE);
				}
			}

			// If everything was correct: Create Session
			SessionImpl session = new SessionImpl(this, id, login, callback);
			userSessions.add(session);
			ServerMain.MAIN_LOGGER.info("Session created for " + session + ". Now " + userSessions.size() + " user(s) are online.");
			callback.message("Successfully logged in " + login + "!", JOptionPane.INFORMATION_MESSAGE);
			return session;
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.MAIN_LOGGER.info("User "+login+" caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + login + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void resetPw(String login) throws ServerException
	{
		try
		{
			Manager.getManager(UserManager.class).resetPw(Manager.getManager(DatabaseManager.class).getConnection(), login);

		}
		catch (IllegalArgumentException e)
		{
			ServerMain.MAIN_LOGGER.info("User "+login+" caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + login + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
