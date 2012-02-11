package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.UserFiles.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.UserFiles.Role;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MiscUtil;
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
	private final String			user;
	private final ServerImpl		server;
	private final ClientCallback	callback;
	private final String			sessionId;
	private final Role				role;

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	SessionImpl(ServerImpl server, String user, ClientCallback callback)
	{
		this.server = server;
		this.user = user;
		this.callback = callback;
		this.sessionId = "default_session_id";
		this.role = Role.USER;
	}

	// --------------------------------------------------------------------------------
	// -- Private/Packet Method(s) ----------------------------------------------------
	// --------------------------------------------------------------------------------
	String getUser()
	{
		return user;
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
		ServerMain.logger.fine(Thread.currentThread() + ": Unreferenced: " + user + "@" + this);
		server.removeUserSession(this);
	}

	// Overriding Session
	// --------------------------------------------------------------------------------
	@Override
	public void putAttr(String key, String newValue) throws IllegalArgumentException, ServerException
	{
		try
		{
			callback.message("Method putAttr() not implemented yet!", JOptionPane.WARNING_MESSAGE);
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public Map<String, String> getAllAttrs() throws IllegalArgumentException, ServerException
	{
		try
		{
			callback.message("Method getAllAttrs() not implemented yet!", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public String getAttr(String key) throws IllegalArgumentException, ServerException
	{
		try
		{
			callback.message("Method getAttr() not implemented yet!", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean deleteFile(String uri, boolean deleteNotEmptyDir) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean actuallyDeleted = false;
			File fileOrDir = UserFiles.uriToUserFile(this, uri, FileType.FILE_OR_DIR, true);
			// Check args
			if (IOUtil.isFileOrEmptyDir(fileOrDir))
			{
				IOUtil.executeDelete(fileOrDir);
				actuallyDeleted = true;
			}
			else
			{
				if (deleteNotEmptyDir)
					throw new IllegalArgumentException("Deletion of non empty directories not yet supported!");
			}
			return actuallyDeleted;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean renameFile(String uri, String newName) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean actuallyRenamed = false;
			// Check Arguments
			MiscUtil.ensureValidString(newName, UserFiles.ILLEGAL_CHARS_IN_FILENAME);
			File fileOrDir = UserFiles.uriToUserFile(this, uri, FileType.FILE_OR_DIR, true);
			File dest = new File(fileOrDir.getParent(), newName);
			if (dest.exists())
			{
				callback.message("Conflict: A file with this name already exists: " + uri + "/" + newName, JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				IOUtil.executeRenameTo(fileOrDir, dest);
			}
			actuallyRenamed = true;
			return actuallyRenamed;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean copyFile(String srcURI, String destDirURI, boolean replace) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean actuallyCopied = false;
			File srcFileOrDir = UserFiles.uriToUserFile(this, srcURI, FileType.FILE_OR_DIR, true);
			File destDir = UserFiles.uriToUserFile(this, destDirURI, FileType.DIR, false);
			File destFileOrDir = new File(destDir, srcFileOrDir.getName());
			if (srcFileOrDir.isDirectory())
			{
				throw new IllegalArgumentException("Copy of directories not supported yet!");
			}
			else
			{
				actuallyCopied = IOUtil.copyFile(srcFileOrDir, destFileOrDir, replace);
			}
			return actuallyCopied;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean mkDir(String parentDirUri, String newDirName) throws IllegalArgumentException, ServerException
	{
		try
		{
			boolean actuallyMadeDir = false;
			MiscUtil.ensureValidString(newDirName, UserFiles.ILLEGAL_CHARS_IN_FILENAME);
			File parentDir = UserFiles.uriToUserFile(this, parentDirUri, FileType.DIR, false);
			actuallyMadeDir = IOUtil.executeMkDir(new File(parentDir, newDirName));
			return actuallyMadeDir;
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public FileInfo[] listFileInfos(String dirURI) throws IllegalArgumentException, ServerException
	{
		try
		{
			File dir = UserFiles.uriToUserFile(this, dirURI, FileType.DIR, false);
			return UserFiles.listFileInfos(this, dir, UserFiles.getUserRootDir(user));
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public int openFileChannel(String destDirUri, String filename, long fileSize) throws IllegalArgumentException, ServerException
	{
		try
		{
			MiscUtil.ensureValidString(filename, UserFiles.ILLEGAL_CHARS_IN_FILENAME);
			File destDir = UserFiles.uriToUserFile(this, destDirUri, FileType.DIR, false);
			File destFile = new File(destDir, filename);
			return Simon.prepareRawChannel(new FileReceiver(destFile, fileSize), this);
		}
		catch (IllegalArgumentException iae)
		{
			throw iae;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ServerException(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------
	public String toString()
	{
		return String.format("SessionImpl[%s,%s,%s]", user, role.toString(), Simon.getRemoteInetSocketAddress(callback));
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
