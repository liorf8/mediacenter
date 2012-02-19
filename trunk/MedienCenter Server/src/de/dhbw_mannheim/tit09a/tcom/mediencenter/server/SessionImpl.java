package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.rmi.ServerException;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.NFileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.root1.simon.Simon;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Session.class })
public class SessionImpl implements Session, SimonUnreferenced, Serializable
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long		serialVersionUID	= 4608946221610753415L;

	// --------------------------------------------------------------------------------
	// -- Instance Variable(s) --------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final long				id;
	private final String			login;
	private final ServerImpl		server;
	private final ClientCallback	callback;
	private final String			sessionId;

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	SessionImpl(ServerImpl server, long id, String login, ClientCallback callback)
	{
		this.server = server;
		this.id = id;
		this.login = login;
		this.callback = callback;
		this.sessionId = "default_session_id";
	}

	// --------------------------------------------------------------------------------
	// -- Private/Packet Method(s) ----------------------------------------------------
	// --------------------------------------------------------------------------------
	long getId()
	{
		return id;
	}

	// --------------------------------------------------------------------------------
	String getLogin()
	{
		return login;
	}

	// --------------------------------------------------------------------------------
	ClientCallback getClientCallback()
	{
		return callback;
	}

	// --------------------------------------------------------------------------------
	String getSessionId()
	{
		return sessionId;
	}

	// --------------------------------------------------------------------------------
	// -- Public Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// Overriding SimonUnreferenced
	@Override
	public void unreferenced()
	{
		ServerMain.MAIN_LOGGER.debug(Thread.currentThread() + ": Unreferenced: " + this);
		server.removeUserSession(this);
	}

	// Overriding Session
	// --------------------------------------------------------------------------------
	@Override
	public void changeLogin(String newLogin, String pw) throws ServerException
	{
		try
		{
			Manager.getManager(UserManager.class).changeLogin(Manager.getManager(DatabaseManager.class).getConnection(), id, newLogin, pw);
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void changePw(String newPw, String currentPw) throws ServerException
	{
		try
		{
			Manager.getManager(UserManager.class).changePw(Manager.getManager(DatabaseManager.class).getConnection(), id, newPw, currentPw);
		}
		catch (IllegalArgumentException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public int deleteFile(String uri, boolean deleteNotEmptyDir) throws FileSystemException, ServerException
	{
		try
		{
			Path fileOrDir = Manager.getManager(NFileManager.class).uriStringToPath(this, uri, FileType.FILE_OR_DIR, true);
			return NIOUtil.delete(fileOrDir, deleteNotEmptyDir);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void renameFile(String uri, String newName) throws FileSystemException, ServerException
	{
		try
		{
			// Check Arguments
			Path fileOrDir = Manager.getManager(NFileManager.class).uriStringToPath(this, uri, FileType.FILE_OR_DIR, true);
			NIOUtil.rename(fileOrDir, newName);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	@Override
	public int moveFile(String srcUri, String targetDirUri, boolean replace) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = Manager.getManager(NFileManager.class);
			Path srcFileOrDir = fileMan.uriStringToPath(this, srcUri, FileType.FILE_OR_DIR, true);
			Path targetDir = fileMan.uriStringToPath(this, targetDirUri, FileType.DIR, false);
			return NIOUtil.copyMove(srcFileOrDir, targetDir, replace, false);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public int copyFile(String srcUri, String targetDirUri, boolean replace) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = Manager.getManager(NFileManager.class);
			Path srcFileOrDir = fileMan.uriStringToPath(this, srcUri, FileType.FILE_OR_DIR, true);
			Path targetDir = fileMan.uriStringToPath(this, targetDirUri, FileType.DIR, false);
			return NIOUtil.copyMove(srcFileOrDir, targetDir, replace, false);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void createDir(String parentDirUri, String dirName) throws FileSystemException, ServerException
	{
		try
		{
			Path parentDir = Manager.getManager(NFileManager.class).uriStringToPath(this, parentDirUri, FileType.DIR, false);
			NIOUtil.createDir(parentDir, dirName);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public List<FileInfo> listFileInfos(String dirURI) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = Manager.getManager(NFileManager.class);
			Path dir = fileMan.uriStringToPath(this, dirURI, FileType.DIR, false);
			return fileMan.listFileInfos(this, dir);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public int prepareRawChannel(String destDirUri, String filename, long fileSize) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = Manager.getManager(NFileManager.class);
			Path destDir = fileMan.uriStringToPath(this, destDirUri, FileType.DIR, false);
			Path destFile = destDir.resolve(filename);

			return Simon.prepareRawChannel(fileMan.new FileReceiver(destFile, fileSize), this);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void downloadFile(String uri) throws FileSystemException, ServerException
	{
		try
		{
			NFileManager fileMan = Manager.getManager(NFileManager.class);
			Path file = fileMan.uriStringToPath(this, uri, FileType.FILE, true);
			fileMan.sendFile(callback, file);
		}
		catch (IllegalArgumentException | FileSystemException e)
		{
			ServerMain.MAIN_LOGGER.info("User " + this + " caused throw of:", e);
			throw e;
		}
		catch (Throwable t)
		{
			ServerMain.MAIN_LOGGER.error("Invocation by user " + this + " caught exception", t);
			throw new ServerException(t.toString());
		}
	}

	// --------------------------------------------------------------------------------
	public String toString()
	{
		return String.format("SessionImpl[%d,%s,%s]", id, login, Simon.getRemoteInetSocketAddress(callback));
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
