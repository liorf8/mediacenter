package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.Serializable;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.FileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.FileManager.Role;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MiscUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ReturnObj;
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
	private final Role				role;

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
		this.role = Role.USER;
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
	Role getRole()
	{
		return role;
	}

	// --------------------------------------------------------------------------------
	// -- Public Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// Overriding SimonUnreferenced
	@Override
	public void unreferenced()
	{
		ServerMain.logger.fine(Thread.currentThread() + ": Unreferenced: " + login + "@" + this);
		server.removeUserSession(this);
	}

	// Overriding Session
	// --------------------------------------------------------------------------------
	@Override
	public ReturnObj<Void> changePw(String newPw, String currentPw) throws IllegalArgumentException, ServerException
	{
		try
		{
			MiscUtil.ensureValidString(newPw, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			short returnCode = Authenticator.getInstance().changePw(DatabaseManager.getInstance().getConnection(), id, newPw, currentPw);
			return new ReturnObj<Void>(returnCode);
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
	public ReturnObj<Void> changeLogin(String newLogin, String pw) throws IllegalArgumentException, ServerException
	{
		try
		{
			MiscUtil.ensureValidString(newLogin, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			short returnCode = Authenticator.getInstance().changeLogin(DatabaseManager.getInstance().getConnection(), id, newLogin, pw);
			return new ReturnObj<Void>(returnCode);
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
	public ReturnObj<Void> deleteFile(String uri) throws IllegalArgumentException, ServerException
	{
		try
		{
			File fileOrDir = FileManager.getInstance().uriToUserFile(this, uri, FileType.FILE_OR_DIR, true);
			// Check args
			int filesDeleted = IOUtil.deleteAllFiles(fileOrDir);
			return new ReturnObj<Void>(ReturnObj.SUCCESS, "Deleted " +filesDeleted+ "files.");
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
	public ReturnObj<Void> renameFile(String uri, String newName) throws IllegalArgumentException, ServerException
	{
		try
		{
			// Check Arguments
			MiscUtil.ensureValidString(newName, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			File fileOrDir = FileManager.getInstance().uriToUserFile(this, uri, FileType.FILE_OR_DIR, true);
			File dest = new File(fileOrDir.getParent(), newName);
			if (dest.exists())
			{
				return new ReturnObj<Void>(null, ReturnObj.CONFLICT, "A file with this name already exists: " + uri + "/" + newName);
			}
			else
			{
				IOUtil.executeRenameTo(fileOrDir, dest);
			}
			return new ReturnObj<Void>(null, ReturnObj.SUCCESS, null);
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
	public ReturnObj<Void> copyFile(String srcURI, String destDirURI, boolean replace) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean actuallyCopied;
			File srcFileOrDir = FileManager.getInstance().uriToUserFile(this, srcURI, FileType.FILE_OR_DIR, true);
			File destDir = FileManager.getInstance().uriToUserFile(this, destDirURI, FileType.DIR, false);
			File destFileOrDir = new File(destDir, srcFileOrDir.getName());
			if (srcFileOrDir.isDirectory())
			{
				throw new IllegalArgumentException("Copy of directories not supported yet!");
			}
			else
			{
				actuallyCopied = IOUtil.copyFile(srcFileOrDir, destFileOrDir, replace);
			}
			return new ReturnObj<Void>(actuallyCopied ? ReturnObj.SUCCESS : ReturnObj.SUCCESS_NOT_REPLACED);
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
	public ReturnObj<Void> mkDir(String parentDirUri, String newDirName) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean actuallyMadeDir;
			MiscUtil.ensureValidString(newDirName, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			File parentDir = FileManager.getInstance().uriToUserFile(this, parentDirUri, FileType.DIR, false);
			actuallyMadeDir = IOUtil.executeMkDir(new File(parentDir, newDirName));
			return new ReturnObj<Void>(actuallyMadeDir ? ReturnObj.SUCCESS : ReturnObj.SUCCESS_NOT_REPLACED);
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
	public ReturnObj<FileInfo[]> listFileInfos(String dirURI) throws IllegalArgumentException, ServerException
	{
		try
		{
			File dir = FileManager.getInstance().uriToUserFile(this, dirURI, FileType.DIR, false);
			return new ReturnObj<FileInfo[]>(FileManager.getInstance().listFileInfos(this, dir, FileManager.getInstance().getUserRootDir(login)));
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
	public ReturnObj<Integer> openFileChannel(String destDirUri, String filename, long fileSize) throws IllegalArgumentException, ServerException
	{
		try
		{
			MiscUtil.ensureValidString(filename, FileManager.ILLEGAL_CHARS_IN_FILENAME);
			File destDir = FileManager.getInstance().uriToUserFile(this, destDirUri, FileType.DIR, false);
			File destFile = new File(destDir, filename);
			return new ReturnObj<Integer>(Simon.prepareRawChannel(new FileReceiver(destFile, fileSize), this));
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
	public String toString()
	{
		return String.format("SessionImpl[%d,%s,%s,%s]", id, login, role.toString(), Simon.getRemoteInetSocketAddress(callback));
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
