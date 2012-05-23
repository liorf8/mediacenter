package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileSystemException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.AuthenticationException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.KeyAlreadyExistsException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Alle Pfadangaben sind relative Pfadangaben ausgehend vom Root-Verzeichnis des jeweiligen Users. Um dieses anzugeben, einfach einen leeren String
 * �bergeben (<code>""</code>). F�r den Standard-Ordner "Music" <code>"/Music"</code>.
 * 
 * @author mhertram
 * 
 */
public interface Session
{
	public boolean isValid();
	
	/**
	 * @return Die ID des Users.
	 */
	public long getId();

	/**
	 * @return Der Login-Name des Users.
	 */
	public String getLogin();

	/**
	 * @return Die Session-ID dieser Session.
	 */
	public String getSessionId();

	/**
	 * Zum �ndern des Login-Namens.
	 * 
	 * @param pw
	 *            Das aktuelle Passwort des Users.
	 * @param newLogin
	 *            Der neue, gew�nschte Login-Name des Users.
	 * @throws AuthenticationException
	 *             Falls das Passwort nicht stimmt.
	 * @throws KeyAlreadyExistsException
	 *             Falls der gew�nschte Login-Name schon vergeben ist.
	 * @throws ServerException
	 */
	public void changeLogin(String pw, String newLogin) throws AuthenticationException, KeyAlreadyExistsException, ServerException;

	/**
	 * Zum �ndern des Passwortes.
	 * 
	 * @param currentPw
	 *            Das aktuelle Passwort.
	 * @param newPw
	 *            Das neue, gew�nschte Passwort.
	 * @throws AuthenticationException
	 *             Falls das aktuelle Passwort nicht stimmt.
	 * @throws ServerException
	 */
	public void changePw(String currentPw, String newPw) throws AuthenticationException, ServerException;

	/**
	 * @param dirPath
	 *            Der Pfad des Ordners, dessen Dateien aufgelistet werden sollen. Es wird nur die 1. Ebene aufgelistet.
	 * @return Eine Liste mit den {@link FileInfo}s der Unterdateien.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public List<FileInfo> listFileInfos(String dirPath) throws FileSystemException, ServerException;

	/**
	 * Existiert das Verzeichnis schon, wird automatisch ein neues unter einem anderen Namen erstellt. Z.B. "neues verzeichnis (1)".
	 * 
	 * @param parentDirPath
	 *            Der Pfad zum Ordner, in dem der neue Ordner erstellt werden soll.
	 * @param dirName
	 *            Der Name f�r das neue Verzeichnis.
	 * @return
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public String createDir(String parentDirPath, String dirName) throws FileSystemException, ServerException;

	/**
	 * Zum Umbennenen einer Datei. Existiert schon eine Datei unter dem gew�nschten Namen, wird die zu erstellende automatisch umbenannt.
	 * 
	 * @param path
	 *            Der Pfad zur Datei.
	 * @param newName
	 *            Der gew�nschte, neue Name.
	 * @return Den Pfad zur Datei unter dem neuen Namen.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public String renameFile(String path, String newName) throws FileSystemException, ServerException;

	/**
	 * Kopiert eine Datei oder einen Dateibaum (einen Ordner samt seiner Unterordner und aller sich darin befindenden Dateien).
	 * 
	 * @param srcPath
	 *            Der Pfad zur Datei oder zum Ordner, die/der kopiert werden soll.
	 * @param targetDirPath
	 *            Der Pfad zum Ordner, in den kopiert werden soll.
	 * @param replace
	 *            Ob existierende Dateien ersetzt werden sollen.
	 * @return Wie viele Dateien tats�chlich kopiert wurden.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public int copyFile(String srcPath, String targetDirPath, boolean replace) throws FileSystemException, ServerException;

	/**
	 * Verschiebt eine Datei oder einen Dateibaum.
	 * 
	 * @param srcPath
	 *            Der Pfad der zu verschiebenden Datei / des Ordners.
	 * @param targetDirPath
	 *            Der Pfad zum Zielordner.
	 * @param replace
	 *            Ob existierende Dateien ersetzt werden sollen.
	 * @return Wie viele Dateien tats�chlich verschoben wurden.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public int moveFile(String srcPath, String targetDirPath, boolean replace) throws FileSystemException, ServerException;

	/**
	 * L�scht eine Datei oder einen Dateibaum.
	 * 
	 * @param path
	 *            Der Pfad der zu l�schenden Datei bzw des Ordners.
	 * @param deleteNotEmptyDir
	 *            Ob nicht-leere Ordner gel�scht werden sollen.
	 * @return Die Anzahl der tats�chlich gel�schten Dateien.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public int deleteFile(String path, boolean deleteNotEmptyDir) throws FileSystemException, ServerException;

	/**
	 * Wird vom Client aufgerufen, wenn er eine Datei hochladen will. Hiermit wird ein RawChannel beim SIMON-Dispatcher des Servers registriert. Die
	 * R�ckgabe ist der Token, der diesen Channel identifiziert.
	 * 
	 * @param targetDirPath
	 *            Der Ordner, in dem die hochgeladene Datei erstellt werden soll.
	 * @param filename
	 *            Der Name der hochzuladenden Datei.
	 * @param fileSize
	 *            Die Gr��e der hochzuladenden Datei.
	 * @return Der Token, der den Channel identifiziert, �ber den der tats�chliche Upload-Vorgang stattfindet.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public int prepareRawChannel(String targetDirPath, String filename, long fileSize) throws FileSystemException, ServerException;

	/**
	 * L�dt eine Datei herunter unter Verwendung von {@link ClientCallback#prepareRawChannel(String, long)}.
	 * 
	 * @param path
	 *            Der Pfad zur herunterzuladenden Datei.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public void downloadFile(String path) throws FileSystemException, ServerException;

	/**
	 * Gibt die Bytes einer Datei zur�ck. Limit sind 32MB.
	 * 
	 * @param path
	 *            Der Pfad zu der Datei.
	 * @return Ein Byte-Array mit den Bytes der datei.
	 * @throws FileSystemException
	 *             Bei IO-Fehlern.
	 * @throws ServerException
	 */
	public byte[] getFileBytes(String path) throws FileSystemException, ServerException;

	/**
	 * Gibt den der Session zugeordneten StreamPlayer zur�ck.
	 * 
	 * @return Die der Session zugeordnete Instanz des StreamPlayers.
	 * @throws NoSuchElementException
	 * @throws ServerException
	 */
	public StreamPlayer getStreamPlayer() throws NoSuchElementException, ServerException;

	/**
	 * Gibt den der Session zugeordnete InfoPlayer zur�ck. Der InfoPlayer dient zum Abrufen von ID3-Tags und anderen Metainformationen wie L�nge,
	 * Audio/Video-Spuren, und Kodierung. Er ist eine eigene MediaPlayer-Instanz und unabh�ngig vom StreamPlayer, damit letzterer weiter wiedergeben
	 * kann, w�hrend Infos gelesen werden.
	 * 
	 * @returnDie der Session zugeordnete Instanz des InfoPlayers.
	 * @throws NoSuchElementException
	 * @throws ServerException
	 */
	public InfoPlayer getInfoPlayer() throws NoSuchElementException, ServerException;

	public void logout();
}
