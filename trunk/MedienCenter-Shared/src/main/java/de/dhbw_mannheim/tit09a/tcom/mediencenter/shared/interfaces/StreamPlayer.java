package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileSystemException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TranscodeOptions;

import uk.co.caprica.vlcj.player.MediaDetails;

/**
 * Um die Wiedergabe bei der letzten Position fortzusetzen:
 * 
 * <pre>
 * StreamMediaPlayer player = session.getRemoteMediaPlayer(...);
 * 
 * FileInfo fi = ...;
 * long elapsedTime = fi.getElapsedTime();
 * 
 * player.play("himym.avi");
 * player.setTime(elapsedTime);
 * </pre>
 * 
 * 
 * @author mhertram
 * 
 */
public interface StreamPlayer
{
	public static final String	PROTOCOL_HTTP		= "http";
	public static final String	PROTOCOL_RTP		= "rtp";
	public static final String	PROTOCOL_RTSP		= "rtsp";

	public static final String	DEFAULT_PROTOCOL	= PROTOCOL_RTP;
	public static final int		DEFAULT_PORT		= 5555;

	public boolean isValid();
	
	/**
	 * @param protocol
	 *            The protocol that should be used for streaming to your PC. One of "http", "rtp" or "rtsp".
	 * @param port
	 *            The port on your PC over which you want to receive the stream.
	 * @param transcodeOptions
	 *            The transcode options which specify how the server should transcode the media file before it is streamed. Can be null to indicate
	 *            that no transcoding should happen.
	 * @return
	 */
	String setAndGetStreamTarget(String protocol, int port, TranscodeOptions transcodeOptions);

	/**
	 * @return Die URL, die der Client aufrufen muss, um den Stream zu empfangen.
	 * @throws ServerException
	 */
	public String getStreamTarget() throws ServerException;

	/**
	 * Spielt eine Mediendatei ab.
	 * 
	 * @param path
	 *            Der Pfad zur Datei.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public void play(String path) throws FileSystemException, ServerException;

	/**
	 * Spielt ab der aktuellen Position weiter.
	 * 
	 * @return False, wenn noch keine Mediadatei mit dem MediaPlayer assoziiert ist. Um dies zu tun, einfach {@link #play(String)} aufrufen.
	 * @throws ServerException
	 */
	public boolean play() throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#stop()}. Die abgelaufene Zeit wird gespeichert, sodass wieder an der gestoppten Stelle
	 * fortgefahren werden kann.
	 * 
	 * @throws ServerException
	 */
	public void stop() throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#pause()}.
	 * 
	 * @throws ServerException
	 */
	public void pause() throws ServerException;

	/**
	 * @return Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#getLength()}.
	 * @throws ServerException
	 */
	public long getLength() throws ServerException;

	/**
	 * @return Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#getTime()}.
	 * @throws ServerException
	 */
	public long getTime() throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#setTime(long)}.
	 * 
	 * @throws ServerException
	 */
	public void setTime(long time) throws ServerException;

	/**
	 * @return Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#getPosition()}.
	 * @throws ServerException
	 */
	public float getPosition() throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#setPosition(float)}.
	 * 
	 * @throws ServerException
	 */
	public void setPosition(float pos) throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#skip(long)}.
	 * 
	 * @throws ServerException
	 */
	public void skip(long delta) throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#getMediaDetails()}.
	 * 
	 * @return The MediaDetails
	 * @throws ServerException
	 */
	public MediaDetails getMediaDetails() throws ServerException;
}
