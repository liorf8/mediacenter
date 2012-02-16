package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MiscUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ReturnObj;
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
		ServerMain.logger.info("Removed " + userSession + " from sessionlist. " + userSessions.size() + " user(s) are online.");
		return existed;
	}

	// --------------------------------------------------------------------------------
	// -- Public Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public ReturnObj<Long> serverTime() throws ServerException
	{
		try
		{
			return new ReturnObj<Long>(System.currentTimeMillis());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName() + ": " + e.getMessage());
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
	public Session login(String login, String pw, ClientCallback callback) throws IllegalArgumentException, ServerException
	{
		try
		{
			MiscUtil.ensureValidString(login, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			MiscUtil.ensureValidString(pw, FileManager.ILLEGAL_CHARS_IN_FILENAME);

			Connection con = DatabaseManager.getInstance().getConnection();
			Authenticator auth = Authenticator.getInstance();

			// Get id. Can result in -1L if user not found
			long id = auth.idForLogin(con, login);
			ServerMain.logger.fine("Id: " + id);

			// Authenticate the user
			if (!auth.authenticate(con, id, pw))
			{
				String msg = "Login and password combination incorrect!";
				// return new Return<Session>(Return.ACCESS_DENIED, msg);
				callback.message(msg, JOptionPane.WARNING_MESSAGE);
				return null;
			}

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
						// return new Return<Session>(Return.CONFLICT, msg);
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
			ServerMain.logger.info("Session created for " + session + ". Now " + userSessions.size() + " user(s) are online.");
			callback.message("Successfully logged in " + login + "!", JOptionPane.INFORMATION_MESSAGE);
			// return new Return<Session>(session);
			return session;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public ReturnObj<Long> register(String login, String pw) throws IllegalArgumentException, ServerException
	{
		try
		{
			// Check preconditions
			MiscUtil.ensureValidString(login, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			MiscUtil.checkStringLength(login, 1, Authenticator.MAX_LENGTH);
			
			MiscUtil.ensureValidString(pw, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			MiscUtil.checkStringLength(pw, 1, Authenticator.MAX_LENGTH);

			// Insert user if does not exist
			ReturnObj<Long> returnValue;
			Connection con = DatabaseManager.getInstance().getConnection();
			Authenticator auth = Authenticator.getInstance();
			try
			{
				// Insert in Database
				returnValue = auth.insertUser(con, login, pw);

				// Create User's Dirs
				FileManager.getInstance().createUserDirs(login);
				con.commit(); // IMPORTANT! Is not done inside insertUser()!
				return returnValue;
			}
			catch (Exception e)
			{
				System.err.println("Rolling back due to: " + e.getClass().getName() + ": " + e.getMessage());
				con.rollback();
				System.err.println("Deleting directories of " + login);
				IOUtil.deleteAllFiles(FileManager.getInstance().getUserRootDir(login));
				throw e;
			}
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
