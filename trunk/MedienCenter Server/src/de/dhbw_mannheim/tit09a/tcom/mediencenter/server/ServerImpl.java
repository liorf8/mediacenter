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
	public long serverTime() throws ServerException
	{
		try
		{
			return System.currentTimeMillis();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName()+": "+e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public Session login(String login, String pw, ClientCallback callback) throws IllegalArgumentException, ServerException
	{
		try
		{
			MiscUtil.ensureValidString(login, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			MiscUtil.ensureValidString(pw, FileManager.ILLEGAL_CHARS_IN_FILENAME);

			// check if user is already logged in
			for (SessionImpl oneSession : userSessions)
			{
				if (oneSession.getLogin().equals(login))
				{
					boolean isSameRemote = Simon.denoteSameRemoteObjekt(callback, oneSession.getClientCallback());
					// Is the user currently logged in on the same clientmachine?
					if (isSameRemote)
					{
						callback.message("You are already logged in on your computer. Please logout first.", JOptionPane.INFORMATION_MESSAGE);
						return null;
					}
					// or on another machine
					oneSession.getClientCallback().releaseConnection();
				}
			}

			// Check pw
			Connection con = DatabaseManager.getInstance().getConnection();
			Authenticator auth = Authenticator.getInstance();
			if (!auth.authenticate(con, login, pw))
			{
				callback.message("Login or password incorrect!", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			
			// Get ID
			long id = auth.idForLogin(con, login);
			if(id == -1L)
				throw new ServerException("Illegal id: "+id);
			
			// Create Session
			SessionImpl session = new SessionImpl(this, id, login, callback);
			userSessions.add(session);
			ServerMain.logger.info("Session created for " + session + ". Now " + userSessions.size() + " user(s) are online.");
			callback.message("Successfully logged in " + login + "!", JOptionPane.INFORMATION_MESSAGE);
			return session;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName()+": "+e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean register(String login, String pw) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean wasNotRegisteredYet = false;
			MiscUtil.ensureValidString(login, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			MiscUtil.ensureValidString(pw, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			
			Connection con = DatabaseManager.getInstance().getConnection();
			Authenticator auth = Authenticator.getInstance();
			if (!auth.userExists(con, login))
			{
				ServerMain.logger.finer("User does not  exist. Creating dirs and inser into Database ...");
				FileManager.getInstance().createUserDirs(login);
				if(!auth.insertUser(con, login, pw))
					throw new IllegalStateException("User was tested for not existing, but apparently he existed already!");
				wasNotRegisteredYet = true;
			}
			else
			{
				ServerMain.logger.fine("User already exists: " +login);
			}
			return wasNotRegisteredYet;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName()+": "+e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean unregister(String login, String pw) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean wasRegistered = false;
			if (FileManager.getInstance().getUserRootDir(login).exists())
			{
				wasRegistered = true;
				if (!IOUtil.deleteDir(FileManager.getInstance().getUserRootDir(login), true))
				{
					ServerMain.logger.warning("Deletion of user root dir failed: " + FileManager.getInstance().getUserRootDir(login));
					throw new ServerException("Could not unregister user: " + login);
				}
			}
			return wasRegistered;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getClass().getName()+": "+e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
