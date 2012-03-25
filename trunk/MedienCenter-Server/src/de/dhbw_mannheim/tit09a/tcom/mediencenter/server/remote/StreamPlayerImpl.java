package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;

import uk.co.caprica.vlcj.mrl.HttpMrl;
import uk.co.caprica.vlcj.mrl.RtpMrl;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager.FileType;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.StreamMediaPlayerEventListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TranscodeOptions;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { StreamPlayer.class })
public class StreamPlayerImpl implements StreamPlayer, Serializable
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long			serialVersionUID	= 4144104341377967192L;
	private static final String			DEFAULT_RTSP_PATH	= "/mediencenter";		// "/mediencenter"

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final SessionImpl			session;
	private final HeadlessMediaPlayer	headlessPlayer;

	private String						streamTarget;

	private volatile boolean			isValid				= true;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public StreamPlayerImpl(SessionImpl session) throws Exception
	{
		this.headlessPlayer = VlcManager.getInstance().buildHeadlessPlayer();
		this.session = session;

		setAndGetStreamTarget(DEFAULT_PROTOCOL, DEFAULT_PORT, null);
		headlessPlayer.addMediaPlayerEventListener(new StreamMediaPlayerEventListener());
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
	public synchronized String setAndGetStreamTarget(String protocol, int port, TranscodeOptions transcodeOptions)
	{
		checkValid();
		protocol = protocol.toLowerCase();
		String clientHost = SessionImpl.getClientInetSocketAddress(session).getHostString();

		String[] mediaOptions = formatStreamMediaOptions(protocol, clientHost, port, transcodeOptions);
		ServerMain.INVOKE_LOGGER.info("{} set MediaOptions: {}", session, Arrays.toString(mediaOptions));

		headlessPlayer.setStandardMediaOptions(mediaOptions);
		streamTarget = formatStreamTarget(protocol, clientHost, port);

		return streamTarget;
	}

	@Override
	// --------------------------------------------------------------------------------
	public String getStreamTarget()
	{
		checkValid();
		return streamTarget;
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized boolean play() throws ServerException
	{
		checkValid();
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
		catch (Throwable t)
		{
			throw ServerUtil.logUserThrowable(this.toString(), t);
		}
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void play(String path) throws FileSystemException, ServerException
	{
		checkValid();
		try
		{
			Path absolutePath = NFileManager.getInstance().toValidatedAbsoluteServerPath(session, path, FileType.FILE, false);
			String mrl = NIOUtil.pathToUri(absolutePath);
			saveElapsedTimeAndStop();
			startMedia(mrl);

			ServerMain.INVOKE_LOGGER.info("{} streams {} to {}", new Object[] { session, mrl, streamTarget });
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
		checkValid();
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
	public synchronized void pause()
	{
		checkValid();
		headlessPlayer.pause();
	}

	// --------------------------------------------------------------------------------
	@Override
	public long getLength()
	{
		checkValid();
		return headlessPlayer.getLength();
	}

	// --------------------------------------------------------------------------------
	@Override
	public long getTime()
	{
		checkValid();
		return headlessPlayer.getTime();
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void setTime(long time) throws ServerException
	{
		checkValid();
		headlessPlayer.setTime(time);
	}

	// --------------------------------------------------------------------------------
	@Override
	public float getPosition()
	{
		checkValid();
		return headlessPlayer.getPosition();
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void setPosition(float pos)
	{
		checkValid();
		headlessPlayer.setPosition(pos);
	}

	// --------------------------------------------------------------------------------
	@Override
	public synchronized void skip(long delta)
	{
		checkValid();
		headlessPlayer.skip(delta);
	}

	// --------------------------------------------------------------------------------
	@Override
	public MediaDetails getMediaDetails() throws ServerException
	{
		checkValid();
		return headlessPlayer.getMediaDetails();
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
		// if not already was released
		if (isValid)
		{
			try
			{
				isValid = false;
				saveElapsedTimeAndStop();
				headlessPlayer.release();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private String[] formatStreamMediaOptions(String streamProtocol, String streamTargetHost, int streamTargetPort, TranscodeOptions transcodeOptions)
	{
		List<String> mediaOptions = new ArrayList<>();

		String[] rtpOptions = new String[] { ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all", ":sout-keep" };
		switch (streamProtocol)
		{
			case "http":
				if (transcodeOptions != null)
					mediaOptions.add(formatHttpStream(streamTargetHost, streamTargetPort, transcodeOptions));
				else
					mediaOptions.add(formatHttpStream(streamTargetHost, streamTargetPort));
				break;

			case "rtp":
				if (transcodeOptions != null)
					mediaOptions.add(formatRtpStream(streamTargetHost, streamTargetPort, transcodeOptions));
				else
					mediaOptions.add(formatRtpStream(streamTargetHost, streamTargetPort));
				// add the rtp options
				mediaOptions.addAll(Arrays.asList(rtpOptions));
				break;

			case "rtsp":
				if (transcodeOptions != null)
					throw new UnsupportedOperationException("Transcoding over rtsp not supported. Choose http or rtp!");
				else
					mediaOptions.add(formatRtspStream(streamTargetHost, streamTargetPort, DEFAULT_RTSP_PATH));
				// add the rtp options
				mediaOptions.addAll(Arrays.asList(rtpOptions));
				break;
			default:
				throw new UnsupportedOperationException("The protocol '" + streamProtocol + "' is not supported! Valid are http, rtp and rtsp.");
		}

		return mediaOptions.toArray(new String[mediaOptions.size()]);
	}

	// --------------------------------------------------------------------------------
	private String formatStreamTarget(String protocol, String clientHost, int clientPort)
	{
		switch (protocol)
		{
			case "http":
				return new HttpMrl().host(clientHost).port(clientPort).value();
			case "rtp":
				return new RtpMrl().multicastAddress(clientHost).port(clientPort).value();
			case "rtsp":
				return new RtspMrl().host(clientHost).port(clientPort).path(DEFAULT_RTSP_PATH).value();
			default:
				throw new UnsupportedOperationException("The protocol '" + protocol + "' is not supported! Valid are http, rtp and rtsp.");
		}
	}

	// --------------------------------------------------------------------------------
	private static String formatHttpStream(String dstHost, int dstPort)
	{
		StringBuilder sb = new StringBuilder(50);
		sb.append(":sout=#duplicate{dst=std{access=http,mux=ts,");
		sb.append("dst=");
		sb.append(dstHost);
		sb.append(':');
		sb.append(dstPort);
		sb.append("}}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatHttpStream(String dstHost, int dstPort, TranscodeOptions transcodeOptions)
	{
		StringBuilder sb = new StringBuilder(150);
		
		sb.append(":sout=#transcode{");
		sb.append("vcodec=");
		sb.append(transcodeOptions.videoCodec != null ? transcodeOptions.videoCodec : "");
		sb.append(",acodec=");
		sb.append(transcodeOptions.audioCodec != null ? transcodeOptions.audioCodec : "");
		sb.append(",vb=");
		sb.append(transcodeOptions.videoKBitRate > 1 ? transcodeOptions.videoKBitRate + "" : "");
		sb.append(",ab=");
		sb.append(transcodeOptions.audioKBitRate > 1 ? transcodeOptions.audioKBitRate + "" : "");
		if (transcodeOptions.audioSync)
			sb.append(",audio-sync");
		if (transcodeOptions.deinterlace)
			sb.append(",deinterlace");
		sb.append("}:standard{access=http,mux=ts");
		sb.append(",dst=");
		sb.append(dstHost);
		sb.append(':');
		sb.append(dstPort);
		sb.append("}");
		
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatRtpStream(String dstHost, int dstPort)
	{
		StringBuilder sb = new StringBuilder(50);
		sb.append(":sout=#rtp{dst=");
		sb.append(dstHost);
		sb.append(",port=");
		sb.append(dstPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	private static String formatRtpStream(String dstHost, int dstPort, TranscodeOptions transcodeOptions)
	{
		StringBuilder sb = new StringBuilder(150);

		sb.append(":sout=#transcode{");
		sb.append("vcodec=");
		sb.append(transcodeOptions.videoCodec != null ? transcodeOptions.videoCodec : "");
		sb.append(",acodec=");
		sb.append(transcodeOptions.audioCodec != null ? transcodeOptions.audioCodec : "");
		sb.append(",vb=");
		sb.append(transcodeOptions.videoKBitRate > 1 ? transcodeOptions.videoKBitRate + "" : "");
		sb.append(",ab=");
		sb.append(transcodeOptions.audioKBitRate > 1 ? transcodeOptions.audioKBitRate + "" : "");
		if (transcodeOptions.audioSync)
			sb.append(",audio-sync");
		if (transcodeOptions.deinterlace)
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
			ServerMain.INVOKE_LOGGER.info("Did not save elapsed time. No media available!");

		headlessPlayer.stop();
	}

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
