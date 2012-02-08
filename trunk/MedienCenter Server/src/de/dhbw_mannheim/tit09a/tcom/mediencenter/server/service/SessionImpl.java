package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
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
    private static final long serialVersionUID = 4608946221610753415L;
    // os: C:\\Users\\mhertram\\USERS_ROOT_DIR
    // max: C:\\Users\\Max\\USERS_ROOT_DIR
    public static final String USERS_ROOT_DIR = "C:\\Users\\Max\\USERS_ROOT_DIR";
    public static final char[] ILLEGAL_CHARS_IN_FILENAME = "\\/:*?<>|%&".toCharArray();

    public static enum BASIC_DIRS
    {
	Music, Pictures, Videos
    };

    // --------------------------------------------------------------------------------
    // -- Static Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    static void createUserDirs(String user) throws IllegalArgumentException, IOException,
	    ServerException
    {
	File[] dirs = { new File(getUserRootDir(user)),
		new File(getUserBasicDir(user, BASIC_DIRS.Music)),
		new File(getUserBasicDir(user, BASIC_DIRS.Pictures)),
		new File(getUserBasicDir(user, BASIC_DIRS.Videos)) };
	IOUtil.mkDirs(dirs);
    }

    // --------------------------------------------------------------------------------
    static String getUserRootDir(String user)
    {
	return USERS_ROOT_DIR + File.separator + user;
    }

    // --------------------------------------------------------------------------------
    static String getUserBasicDir(String user, BASIC_DIRS dir)
    {
	return getUserRootDir(user) + File.separator + dir.toString();
    }

    // --------------------------------------------------------------------------------
    static String uriToCanonicalUserPath(String uri, String user) throws IllegalArgumentException,
	    ServerException
    {
	String path = getUserRootDir(user) + File.separator + FileInfo.uriPathToFilePath(uri);

	try
	{
	    // To be sure, users do not get access to directories other than their own.
	    // f.i. via session.listFiles("..") -> would point at the parent directory.
	    if (!IOUtil.isInParentDir(new File(getUserRootDir(user)), new File(path)))
	    {
		ServerMain.serverLogger.warning(String.format("User '%s' tried to access %s", user,
			new File(path).getCanonicalPath()));
		throw new IllegalArgumentException("Access to path '" + uri + "' for user " + user
			+ " denied. Access to other directories than your own is not allowed!");
	    }
	    return path;
	}
	catch (IllegalArgumentException iae)
	{
	    throw iae;
	}
	catch (IOException e)
	{
	    ServerMain.serverLogger.severe(e.toString());
	    e.printStackTrace();
	    throw new ServerException();
	}
    }

    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private final String user;
    private final ServerImpl server;
    private final ClientCallback callback;
    private final String sessionId;

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    SessionImpl(String user, ServerImpl server, ClientCallback callback)
    {
	this.user = user;
	this.server = server;
	this.callback = callback;
	this.sessionId = "default_session_id";
    }

    // --------------------------------------------------------------------------------
    // -- Private/Packet Method(s) ----------------------------------------------------
    // --------------------------------------------------------------------------------
    String getUser()
    {
	return user;
    }

    // --------------------------------------------------------------------------------
    String getSessionId()
    {
	return sessionId;
    }

    // --------------------------------------------------------------------------------
    ClientCallback getClientCallback()
    {
	return callback;
    }

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // Overriding SimonUnreferenced
    @Override
    public void unreferenced()
    {
	ServerMain.serverLogger.fine(Thread.currentThread() + ": Unreferenced: " + user + "@"
		+ this);
	server.removeUserSession(this);
    }

    // Overriding Session
    // --------------------------------------------------------------------------------
    @Override
    public void changeAttr(String key, String newValue)
    {
	System.out.println("NOCH NICHT IMPLEMENTIERT!");
    }

    // --------------------------------------------------------------------------------
    @Override
    public Map<String, String> getAttrs() throws ServerException
    {
	System.out.println("NOCH NICHT IMPLEMENTIERT!");
	return null;
    }

    // --------------------------------------------------------------------------------
    @Override
    public String getAttr(String key) throws IllegalArgumentException, ServerException
    {
	System.out.println("NOCH NICHT IMPLEMENTIERT!");
	return null;
    }

    // --------------------------------------------------------------------------------
    @Override
    public void deleteFile(String uri, boolean deleteNotEmptyDir) throws IllegalArgumentException,
	    ServerException
    {
	try
	{
	    File file = new File(uriToCanonicalUserPath(uri, user));
	    IOUtil.ensureExists(file);
	    if (!file.isDirectory()) IOUtil.executeDelete(file);
	}
	catch (IOException e)
	{
	    throw new ServerException(e.getMessage());
	}

    }

    // --------------------------------------------------------------------------------
    @Override
    public void renameFile(String uri, String newName) throws IllegalArgumentException,
	    ServerException
    {
	try
	{
	    IOUtil.ensureValidString(newName, ILLEGAL_CHARS_IN_FILENAME);
	    File file = new File(uriToCanonicalUserPath(uri, user));
	    File dest = new File(file.getParent() + File.separator + newName);
	    IOUtil.ensureExists(file);
	    if(dest.exists())
	    {
		callback.message("existsssssss");
	    }
	    IOUtil.executeRenameTo(file, dest);
	}
	catch (IOException e)
	{
	    throw new ServerException(e.getMessage());
	}
    }

    // --------------------------------------------------------------------------------
    @Override
    public void copyFile(String srcURI, String destDirURI, boolean replace)
	    throws IllegalArgumentException, ServerException
    {
	try
	{
	    File src = new File(uriToCanonicalUserPath(srcURI, user));
	    File destDir = new File(uriToCanonicalUserPath(destDirURI, user));

	    IOUtil.ensureExists(src);
	    if (src.isDirectory())
	    {
		throw new ServerException("Copying of directories not supported yet: " + srcURI);
	    }
	    IOUtil.copyFile(src, destDir, replace);
	}
	catch (IOException e)
	{
	    throw new ServerException(e.getMessage());
	}
    }

    // --------------------------------------------------------------------------------
    @Override
    public void mkDir(String uri) throws IllegalArgumentException, ServerException
    {
	try
	{
	    IOUtil.mkDir(new File(uriToCanonicalUserPath(uri, user)));
	}
	catch (IOException e)
	{
	    throw new ServerException(e.getMessage());
	}
    }

    // --------------------------------------------------------------------------------
    @Override
    public FileInfo[] listFileInfos(String dirURI) throws IllegalArgumentException, ServerException
    {
	try
	{
	    return IOUtil.listFileInfos(new File(uriToCanonicalUserPath(dirURI, user)), new File(
		    getUserRootDir(user)));
	}
	catch (IOException e)
	{
	    throw new ServerException(e.getMessage());
	}
    }

    // --------------------------------------------------------------------------------
    @Override
    public int openFileChannel(String destURI, long fileSize) throws IllegalArgumentException,
	    ServerException
    {
	return Simon.prepareRawChannel(new FileReceiver(uriToCanonicalUserPath(destURI, user),
		fileSize), this);
    }


    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
