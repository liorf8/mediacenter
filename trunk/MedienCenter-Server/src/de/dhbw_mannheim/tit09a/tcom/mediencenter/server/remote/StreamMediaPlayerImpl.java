package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.awt.Color;
import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.rmi.ServerException;

import uk.co.caprica.vlcj.mrl.HttpMrl;
import uk.co.caprica.vlcj.mrl.RtpMrl;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.RemoteMediaMeta;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamMediaPlayer;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { StreamMediaPlayer.class })
public class StreamMediaPlayerImpl implements StreamMediaPlayer, Serializable, SimonUnreferenced
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long			serialVersionUID	= 4144104341377967192L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final SessionImpl			session;
	private final HeadlessMediaPlayer	headlessPlayer;
	private String						mrlForClient;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public StreamMediaPlayerImpl(SessionImpl session, HeadlessMediaPlayer headlessPlayer, String protocol, int port) throws Exception
	{
		this.headlessPlayer = headlessPlayer;
		this.session = session;
		mrlForClient = setStreamTarget(protocol, port);
		// headlessPlayer.addMediaPlayerEventListener(new StreamMediaPlayerEventListener());
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public void unreferenced()
	{
		ServerMain.INVOKE_LOGGER.info("Unreferenced {}", this);
		try
		{
			saveElapsedTime();
			headlessPlayer.stop();
			VlcManager.getInstance().returnPlayer(headlessPlayer);
		}
		catch (Exception e)
		{
			ServerMain.INVOKE_LOGGER.error("Unreferencing of " + this + " failed", e);
		}
	}

	// --------------------------------------------------------------------------------
	public String getMrlForClient()
	{
		return mrlForClient;
	}

	// --------------------------------------------------------------------------------
	@Override
	public String setStreamTarget(String protocol, int port) throws ServerException
	{
		try
		{
			String mrlForClient;
			synchronized (headlessPlayer)
			{
				// stop playback
				headlessPlayer.stop();

				String clientHost = SessionImpl.getClientInetSocketAddress(session).getHostString();
				String[] mediaOptions;
				switch (protocol)
				{
					case "http":
						mediaOptions = new String[] { formatHttpStream(clientHost, port) };
						mrlForClient = new HttpMrl().host(clientHost).port(port).value();
						break;
					case "rtp":
						mediaOptions = new String[] { formatRtpStream(clientHost, port), ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all",
								":sout-keep" };
						mrlForClient = new RtpMrl().multicastAddress(clientHost).port(port).value();
						break;
					case "rtsp":
						mediaOptions = new String[] { formatRtspStream(clientHost, port, DEFAULT_RTSP_PATH), ":no-sout-rtp-sap",
								":no-sout-standard-sap", ":sout-all", ":sout-keep" };
						mrlForClient = new RtspMrl().host(clientHost).port(port).path(StreamMediaPlayer.DEFAULT_RTSP_PATH).value();
						break;
					default:
						throw new UnsupportedOperationException("The protocol '" + protocol + "' is not supported! Valid are http, rtp, rtsp.");
				}
				headlessPlayer.setStandardMediaOptions(mediaOptions);
			}

			return mrlForClient;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void play() throws ServerException
	{
		try
		{
			System.out.println("mrl: " + headlessPlayer.mrl());
			System.out.println("isPlayable: " + headlessPlayer.isPlayable());
			if (headlessPlayer.isPlayable())
			{
				headlessPlayer.play();
			}
			else
			{
				if (headlessPlayer.mrl() != null)
					startMedia(headlessPlayer.mrl());
				else
					ServerMain.INVOKE_LOGGER.warn("mrl = null");
			}

		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void play(String path) throws FileSystemException, ServerException
	{
		try
		{
			Path absolutePath = NFileManager.getInstance().toValidatedAbsoluteServerPath(session, path, FileType.FILE, false);
			String mrl = absolutePath.toUri().toString();
			startMedia(mrl);
			ServerMain.INVOKE_LOGGER.info("Streaming {} to {} ", new Object[] { mrl, session });
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
	public void stop() throws ServerException
	{
		try
		{
			saveElapsedTime();
			headlessPlayer.stop();
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public void pause()
	{
		headlessPlayer.pause();
	}

	// --------------------------------------------------------------------------------
	@Override
	public long getLength()
	{
		return headlessPlayer.getLength();
	}

	// --------------------------------------------------------------------------------
	@Override
	public long getTime()
	{
		return headlessPlayer.getTime();
	}

	// --------------------------------------------------------------------------------
	@Override
	public float getPosition()
	{
		return headlessPlayer.getPosition();
	}

	// --------------------------------------------------------------------------------
	@Override
	public void setPosition(float pos)
	{
		headlessPlayer.setPosition(pos);
	}

	// --------------------------------------------------------------------------------
	@Override
	public void skip(long delta)
	{
		headlessPlayer.skip(delta);
	}

	// --------------------------------------------------------------------------------
	@Override
	public RemoteMediaMeta getMediaMeta()
	{
		// no media available
		if (!headlessPlayer.isPlayable())
			return null;

		if (!headlessPlayer.isMediaParsed())
			headlessPlayer.parseMedia();
		return new RemoteMediaMetaImpl(headlessPlayer.getMediaMeta());
	}

	public void doSth()
	{

	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static String formatHttpStream(String serverAddress, int serverPort)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#duplicate{dst=std{access=http,mux=ts,");
		sb.append("dst=");
		sb.append(serverAddress);
		sb.append(':');
		sb.append(serverPort);
		sb.append("}}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatRtpStream(String serverAddress, int serverPort)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatRtspStream(String serverAddress, int serverPort, String path)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{sdp=rtsp://@");
		sb.append(serverAddress);
		sb.append(':');
		sb.append(serverPort);
		sb.append(path);
		sb.append("}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private void startMedia(String mrl) throws Exception
	{
		if (!headlessPlayer.startMedia(mrl))
			throw new Exception("Media starting failed because of error: " + mrl);

		System.out.println("startMedia(): " + headlessPlayer.isPlaying());
		headlessPlayer.setMarqueeText("VLCJ is quite good");
		headlessPlayer.setMarqueeSize(60);
		headlessPlayer.setMarqueeOpacity(70);
		headlessPlayer.setMarqueeColour(Color.green);
		headlessPlayer.setMarqueeTimeout(3000);
		headlessPlayer.setMarqueeLocation(300, 400);
		headlessPlayer.enableMarquee(true);
	}

	// --------------------------------------------------------------------------------
	private void saveElapsedTime() throws Exception
	{
		if (headlessPlayer.isPlayable())
			VlcManager.getInstance().saveElapsedTime(headlessPlayer.mrl(), headlessPlayer.getTime());
		else
		{
			System.out.println("saveElapsedTime(): No media available!");
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
