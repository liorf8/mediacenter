package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileSystemException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;

import uk.co.caprica.vlcj.player.MediaMeta;

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
public interface StreamMediaPlayer
{
	public static final String	DEFAULT_PROTOCOL	= "rtp";
	public static final int		DEFAULT_PORT		= 5555;
	public static final String	DEFAULT_RTSP_PATH	= "/mediencenter";	// "/mediencenter"

	/**
	 * @return Die URL, die der Client aufrufen muss, um den Stream zu empfangen.
	 * @throws ServerException
	 */
	public String getStreamTarget() throws ServerException;

	/**
	 * Zum Setzen des Stream-Targets. Der Client kann dann diese URL abspielen. Wird diese Methode nicht aufgerufen, werden die Default-Werte benutzt.
	 * {@link #DEFAULT_PROTOCOL}, {@link #DEFAULT_PORT}.
	 * 
	 * @param protocol
	 *            Das zu benutzende Protokoll. HTTP, RTP oder RTSP.
	 * @param port
	 *            Der zu benutzende Port auf Clientseite.
	 * @return Die URL, die der Client abspielen kann.
	 * @throws ServerException
	 */
	public String setAndGetStreamTarget(String protocol, int port) throws ServerException;

	/**
	 * Transkodierende Variante.
	 * Wenn vCodec oder aCodec null sind, wird Video bzw Audio nicht verändert. Sind beide null, wird nicht transkodiert.
	 * Wenn vKBitRate oder aKBitRate < 1 sind, werden diese nicht verändert.
	 * 
	 * @param protocol
	 *            Das zu benutzende Protokoll.
	 * @param port
	 *            Der zu benutzende Port auf Clientseite.
	 * @param vCodec
	 *            Ein Video-Codec aus http://wiki.videolan.org/Codec.
	 * @param aCodec
	 *            Ein Audio-Codec aus http://wiki.videolan.org/Codec.
	 * @param vKBitRate
	 *            Die Kilobitrate für die Videoübertragung.
	 * @param aKBitRate
	 *            Die Kilobitrate für die Audioübertragung.
	 * @param audioSync
	 *            Ob Audio mit Video gesynct werden soll, indem, falls nötig, Video-Frames gedropped werden.
	 * @param deinterlace
	 *            Ob deinterlacing benutzt werden soll.
	 * @return Die URL, die der Client abspielen kann.
	 * @throws ServerException
	 */
	public String setAndGetStreamTarget(String protocol, int port, String vCodec, String aCodec, int vKBitRate, int aKBitRate, boolean audioSync,
			boolean deinterlace) throws ServerException;

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
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#stop()}.
	 * Die abgelaufene Zeit wird gespeichert, sodass wieder an der gestoppten Stelle fortgefahren werden kann.
	 * 
	 * @throws ServerException
	 */
	public void stop() throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#pause()}.
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
	 * @throws ServerException
	 */
	public void setPosition(float pos) throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#skip(long)}.
	 * @throws ServerException
	 */
	public void skip(long delta) throws ServerException;

	/**
	 * Siehe {@link uk.co.caprica.vlcj.player.MediaPlayer#getMediaMeta()}.
	 * @throws ServerException
	 */
	public MediaMeta getRemoteMediaMeta() throws ServerException;

}
