package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.util.Arrays;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;

import uk.co.caprica.vlcj.mrl.HttpMrl;
import uk.co.caprica.vlcj.mrl.RtpMrl;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;
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
	private String						streamTarget		= "";

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public StreamMediaPlayerImpl(SessionImpl session) throws Exception
	{
		this.headlessPlayer = VlcManager.getInstance().buildHeadlessPlayer();
		this.session = session;

		setAndGetStreamTarget(DEFAULT_PROTOCOL, DEFAULT_PORT);
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
			saveElapsedTimeAndStop();
			VlcManager.getInstance().releasePlayer(headlessPlayer);
		}
		catch (Exception e)
		{
			ServerMain.INVOKE_LOGGER.error("Unreferencing of " + this + " failed", e);
		}
	}

	// --------------------------------------------------------------------------------
	public String getStreamTarget()
	{
		return streamTarget;
	}

	public void setTranscodeOptions()
	{

	}

	public void setTranscode(boolean doTranscode)
	{

	}

	// --------------------------------------------------------------------------------
	@Override
	public String setAndGetStreamTarget(String protocol, int port, String vCodec, String aCodec, int vKBitRate, int aKBitRate, boolean audioSync,
			boolean deinterlace) throws ServerException
	{
		try
		{
			boolean transcode = true;
			if (vCodec == null && aCodec == null)
				transcode = false;

			protocol = protocol.toLowerCase();

			String clientHost = SessionImpl.getClientInetSocketAddress(session).getHostString();
			String[] mediaOptions;
			String tmpStreamTarget;

			switch (protocol)
			{
				case "http":
					if (transcode)
						mediaOptions = new String[] { formatHttpStream(clientHost, port, vCodec, aCodec, vKBitRate, aKBitRate, audioSync, deinterlace) };
					else
						mediaOptions = new String[] { formatHttpStream(clientHost, port) };

					tmpStreamTarget = new HttpMrl().host(clientHost).port(port).value();
					break;

				case "rtp":
					if (transcode)
					{
						mediaOptions = new String[] {
								formatRtpStream(clientHost, port, vCodec, aCodec, vKBitRate, aKBitRate, audioSync, deinterlace), ":no-sout-rtp-sap",
								":no-sout-standard-sap", ":sout-all", ":sout-keep" };
					}
					else
					{
						mediaOptions = new String[] { formatRtpStream(clientHost, port), ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all",
								":sout-keep" };
					}

					tmpStreamTarget = new RtpMrl().multicastAddress(clientHost).port(port).value();
					break;

				case "rtsp":
					if (transcode)
						throw new UnsupportedOperationException("Transcoding over RTSP not supported!");

					mediaOptions = new String[] { formatRtspStream(clientHost, port, DEFAULT_RTSP_PATH), ":no-sout-rtp-sap", ":no-sout-standard-sap",
							":sout-all", ":sout-keep" };
					tmpStreamTarget = new RtspMrl().host(clientHost).port(port).path(StreamMediaPlayer.DEFAULT_RTSP_PATH).value();
					break;

				default:
					throw new UnsupportedOperationException("The protocol '" + protocol + "' is not supported! Valid are http, rtp, rtsp.");
			}
			headlessPlayer.setStandardMediaOptions(mediaOptions);

			synchronized (streamTarget)
			{
				streamTarget = tmpStreamTarget;
			}

			ServerMain.INVOKE_LOGGER.info("{} set mediaOptions: {}, streamTarge: {}", new Object[] { session, Arrays.toString(mediaOptions),
					streamTarget });
			return streamTarget;
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public String setAndGetStreamTarget(String protocol, int port) throws ServerException
	{
		try
		{
			return setAndGetStreamTarget(protocol, port, null, null, -1, -1, false, false);
		}
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public boolean play() throws ServerException
	{
		try
		{
			// if playing
			if (headlessPlayer.isPlaying())
				return true;

			// if paused
			if (headlessPlayer.isPlayable())
			{
				headlessPlayer.play();
				return true;
			}
			// if stopped or no media yet
			else
			{
				try
				{
					startMedia(headlessPlayer.mrl());
					return true;
				}
				// is thrown if no media yet
				catch (java.lang.IllegalStateException ise)
				{
					ServerMain.INVOKE_LOGGER.warn(ise.toString());
					return false;
				}
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

			String mrl = NIOUtil.pathToUri(absolutePath);
			startMedia(mrl);
			ServerMain.INVOKE_LOGGER.info("Streaming {} to {} ({})", new Object[] { mrl, streamTarget, session });
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
	public synchronized void stop() throws ServerException
	{
		try
		{
			saveElapsedTimeAndStop();
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
	public void setTime(long time) throws ServerException
	{
		headlessPlayer.setTime(time);
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
	public MediaMeta getRemoteMediaMeta()
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
		// headlessPlayer.
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static String formatHttpStream(String dstHost, int dstPort)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#duplicate{dst=std{access=http,mux=ts,");
		sb.append("dst=");
		sb.append(dstHost);
		sb.append(':');
		sb.append(dstPort);
		sb.append("}}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatHttpStream(String dstHost, int dstPort, String vCodec, String aCodec, int vKBitRate, int aKBitRate,
			boolean audioSync, boolean deinterlace)
	{
		StringBuilder sb = new StringBuilder(150);
		sb.append(":sout=#transcode{");
		sb.append("vcodec=");
		sb.append(vCodec != null ? vCodec : "");
		sb.append(",acodec=");
		sb.append(aCodec != null ? aCodec : "");
		sb.append(",vb=");
		sb.append(vKBitRate > 1 ? vKBitRate + "" : "");
		sb.append(",ab=");
		sb.append(aKBitRate > 1 ? aKBitRate + "" : "");
		if (audioSync)
			sb.append(",audio-sync");
		if (deinterlace)
			sb.append(",deinterlace");
		sb.append("}:standard{access=http,mux=ts");
		sb.append(",dst=");
		sb.append(dstHost);
		sb.append(':');
		sb.append(dstPort);
		sb.append("}");
		return sb.toString();

		// codecs: http://wiki.videolan.org/Codec
		// e.g.: vcodec: h264, mp4v acodec: mpga, mp3, a52 (DolbyDig)
	}

	// --------------------------------------------------------------------------------
	private static String formatRtpStream(String dstHost, int dstPort)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(dstHost);
		sb.append(",port=");
		sb.append(dstPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatRtpStream(String dstHost, int dstPort, String vCodec, String aCodec, int vKBitRate, int aKBitRate, boolean audioSync,
			boolean deinterlace)
	{
		if (vCodec == null)
			vCodec = "";
		if (aCodec == null)
			aCodec = "";

		StringBuilder sb = new StringBuilder(150);
		sb.append(":sout=#transcode{");
		sb.append("vcodec=");
		sb.append(vCodec != null ? vCodec : "");
		sb.append(",acodec=");
		sb.append(aCodec != null ? aCodec : "");
		sb.append(",vb=");
		sb.append(vKBitRate > 1 ? vKBitRate + "" : "");
		sb.append(",ab=");
		sb.append(aKBitRate > 1 ? aKBitRate + "" : "");
		if (audioSync)
			sb.append(",audio-sync");
		if (deinterlace)
			sb.append(",deinterlace");
		sb.append("}:rtp{mux=ts");
		sb.append(",dst=");
		sb.append(dstHost);
		sb.append(",port=");
		sb.append(dstPort);
		sb.append("}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatRtspStream(String dstHost, int dstPort, String path)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{sdp=rtsp://@");
		sb.append(dstHost);
		sb.append(':');
		sb.append(dstPort);
		sb.append(path);
		sb.append("}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private void startMedia(String mrl) throws Exception
	{
		if (!headlessPlayer.startMedia(mrl))
			throw new Exception("Media starting failed because of error: " + mrl);
	}

	// --------------------------------------------------------------------------------
	private void saveElapsedTimeAndStop() throws Exception
	{
		if (headlessPlayer.isPlayable())
			NFileManager.getInstance().saveElapsedTime(headlessPlayer.mrl(), headlessPlayer.getTime());
		else
			ServerMain.INVOKE_LOGGER.warn("Did not save elapsed time. No media available!");

		headlessPlayer.stop();
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
