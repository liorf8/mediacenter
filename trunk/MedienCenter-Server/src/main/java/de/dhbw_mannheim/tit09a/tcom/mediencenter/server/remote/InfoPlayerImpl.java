package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.util.List;

import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { InfoPlayer.class })
public class InfoPlayerImpl implements InfoPlayer, Serializable
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long			serialVersionUID	= -8395209740007287477L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final SessionImpl			session;
	private final HeadlessMediaPlayer	headlessPlayer;
	private volatile boolean			isValid				= true;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public InfoPlayerImpl(SessionImpl session) throws Exception
	{
		this.headlessPlayer = VlcManager.getInstance().buildHeadlessPlayer();
		this.session = session;
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public boolean isValid()
	{
		return isValid;
	}
	
	// --------------------------------------------------------------------------------
	@Override
	public synchronized long getLength(String path) throws ServerException, FileSystemException
	{
		checkValid();
		try
		{
			Path absolutePath = NFileManager.getInstance().toValidatedAbsoluteServerPath(session, path, FileType.FILE, false);
			String mrl = NIOUtil.pathToUri(absolutePath);
			headlessPlayer.startMedia(mrl);
			long length = headlessPlayer.getLength();
			headlessPlayer.stop();
			return length;
		}
		catch (FileSystemException e)
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
	public synchronized List<TrackInfo> getTrackInfo(String path) throws ServerException, FileSystemException
	{
		checkValid();
		try
		{
			Path absolutePath = NFileManager.getInstance().toValidatedAbsoluteServerPath(session, path, FileType.FILE, false);
			String mrl = NIOUtil.pathToUri(absolutePath);
			headlessPlayer.prepareMedia(mrl);
			headlessPlayer.parseMedia();
			return headlessPlayer.getTrackInfo();
		}
		catch (FileSystemException e)
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
	public synchronized MediaMeta getMediaMeta(String path) throws ServerException, FileSystemException
	{
		checkValid();
		try
		{
			// needs write access because meta data can be written
			Path absolutePath = NFileManager.getInstance().toValidatedAbsoluteServerPath(session, path, FileType.FILE, true);
			String mrl = NIOUtil.pathToUri(absolutePath);

			headlessPlayer.prepareMedia(mrl);
			headlessPlayer.parseMedia();

			return new MediaMetaRemote(headlessPlayer.getMediaMeta());
		}
		catch (FileSystemException e)
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
	public synchronized MediaDetails getMediaDetails(String path) throws ServerException, FileSystemException
	{
		checkValid();
		try
		{
			Path absolutePath = NFileManager.getInstance().toValidatedAbsoluteServerPath(session, path, FileType.FILE, false);
			String mrl = NIOUtil.pathToUri(absolutePath);
			headlessPlayer.startMedia(mrl);
			MediaDetails mediaDetails = headlessPlayer.getMediaDetails();
			headlessPlayer.stop();
			return mediaDetails;
		}
		catch (FileSystemException e)
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
	public String toString()
	{
		if (isValid)
			return super.toString();
		else
			return this.getClass().getSimpleName() + "[invalid]";
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	synchronized void invalidate()
	{
		if (isValid)
		{
			isValid = false;
			headlessPlayer.release();
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void checkValid() throws IllegalStateException
	{
		if (!isValid)
			throw new IllegalStateException(this + " is not valid anymore.");
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
