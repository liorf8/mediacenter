package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.rmi.ServerException;
import java.util.List;
import java.util.NoSuchElementException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.UserManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Simon;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Session.class })
public class SessionImpl implements Session, SimonUnreferenced, Serializable
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 4608946221610753415L;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static InetSocketAddress getClientInetSocketAddress(SessionImpl session)
	{
		return Simon.getRemoteInetSocketAddress(session.getClientCallback());
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final long				id;
	private final String			login;
	private final ClientCallback	callback;
	private final String			sessionId;
	private final ServerImpl		server;
	private StreamMediaPlayerImpl	streamPlayer;

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	SessionImpl(long id, String login, String sessionId, ClientCallback callback, ServerImpl server)
	{
		this.id = id;
		this.login = login;
		this.callback = callback;
		this.sessionId = sessionId;
		this.server = server;
	}

	// --------------------------------------------------------------------------------
	// -- Public Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// Overriding SimonUnreferenced
	@Override
	public void unreferenced()
	{
		ServerMain.INVOKE_LOGGER.info("Unreferenced {}", this);
		try
		{
			// important to let the server do this, because he gets closed
			server.removeUserSession(this);
		}
		catch (Exception e)
		{
			ServerMain.INVOKE_LOGGER.error("Unreferencing of " + this + " failed", e);
		}
	}

	// Overriding Session
	// --------------------------------------------------------------------------------
	@Override
	public long getId()
	{
		return id;
	}

	// --------------------------------------------------------------------------------
	@Override
	public String getLogin()
	{
		return login;
	}

	// --------------------------------------------------------------------------------
	@Override
	public String getSessionId()
	{
		return sessionId;
	}

	// --------------------------------------------------------------------------------
	ClientCallback getClientCallback()
	{
		return callback;
	}

	// --------------------------------------------------------------------------------
	@Override
	public void changeLogin(String newLogin, String pw) throws ServerException
	{
		try
		{
			UserManager.getInstance().changeLogin(DatabaseManager.getInstance().getConnection(), id, newLogin, pw);
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
	public void changePw(String newPw, String currentPw) throws ServerException
	{
		try
		{
			UserManager.getInstance().changePw(DatabaseManager.getInstance().getConnection(), id, newPw, currentPw);
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
	public int deleteFile(String uri, boolean deleteNotEmptyDir) throws FileSystemException, ServerException
	{
		try
		{
			Path fileOrDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, uri, FileType.FILE_OR_DIR, true);
			return NIOUtil.delete(fileOrDir, deleteNotEmptyDir);
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public String renameFile(String uri, String newName) throws FileSystemException, ServerException
	{
		try
		{
			// Check Arguments
			Path fileOrDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, uri, FileType.FILE_OR_DIR, true);
			return NFileManager.getInstance().toRelativeUserPath(NIOUtil.rename(fileOrDir, newName), id).toString();
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.INVOKE_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	@Override
	public int moveFile(String srcUri, String targetDirUri, boolean replace) throws FileSystemException, ServerException
	{
		try
		{
			Path srcFileOrDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, srcUri, FileType.FILE_OR_DIR, true);
			Path targetDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, targetDirUri, FileType.DIR, false);
			return NIOUtil.move(srcFileOrDir, targetDir, replace);
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public int copyFile(String srcUri, String targetDirUri, boolean replace) throws FileSystemException, ServerException
	{
		try
		{
			Path srcFileOrDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, srcUri, FileType.FILE_OR_DIR, true);
			Path targetDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, targetDirUri, FileType.DIR, false);
			return NIOUtil.copy(srcFileOrDir, targetDir, replace);
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public String createDir(String parentDirUri, String dirName) throws FileSystemException, ServerException
	{
		try
		{
			Path parentDir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, parentDirUri, FileType.DIR, false);
			return NFileManager.getInstance().toRelativeUserPath(NIOUtil.createDir(parentDir, dirName), id).toString();
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public List<FileInfo> listFileInfos(String dirURI) throws FileSystemException, ServerException
	{
		try
		{
			Path dir = NFileManager.getInstance().toValidatedAbsoluteServerPath(this, dirURI, FileType.DIR, false);
			return NFileManager.getInstance().listFileInfos(this, dir);
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public int prepareRawChannel(String destDirUri, String filename, long fileSize) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = NFileManager.getInstance();
			Path destDir = fileMan.toValidatedAbsoluteServerPath(this, destDirUri, FileType.DIR, false);
			Path destFile = destDir.resolve(filename);

			return Simon.prepareRawChannel(fileMan.new FileReceiver(destFile, fileSize), this);
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public void downloadFile(String uri) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = NFileManager.getInstance();
			Path file = fileMan.toValidatedAbsoluteServerPath(this, uri, FileType.FILE, true);
			fileMan.sendFile(callback, file);
		}
		catch (IllegalArgumentException | FileSystemException e)
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
	public StreamMediaPlayer getRemoteMediaPlayer(String protocol, int port) throws NoSuchElementException, ServerException
	{
		try
		{
			if (streamPlayer == null)
			{
				streamPlayer = new StreamMediaPlayerImpl(this, VlcManager.getInstance().borrowPlayer(), protocol, port);
			}
			return streamPlayer;
		}
		catch (NoSuchElementException nse)
		{
			throw nse;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	public String toString()
	{
		return String.format("SessionImpl[%d,%s,%s,%s]", id, login, sessionId, Simon.getRemoteInetSocketAddress(callback));
	}

	// --------------------------------------------------------------------------------
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof SessionImpl)
			return ((SessionImpl) obj).sessionId.equals(sessionId);
		return false;
	}

	// --------------------------------------------------------------------------------
	public int hashCode()
	{
		int hashCode = 0;
		for (char oneChar : sessionId.toCharArray())
		{
			hashCode += oneChar;
			hashCode *= -31;
		}
		return hashCode;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
