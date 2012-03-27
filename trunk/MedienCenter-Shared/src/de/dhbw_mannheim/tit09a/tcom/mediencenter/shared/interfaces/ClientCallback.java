package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileAlreadyExistsException;

import de.root1.simon.exceptions.SimonException;

/**
 * Das Callback-Interface des Clients. Die Methoden werden vom Server aufgerufen.
 * 
 * @author mhertram
 * 
 */
public interface ClientCallback
{
	public void notifyShutdown(int delayInSeconds);

	/**
	 * Wird vom Server aufgerufen, wenn er eine Nachricht schickt. Zum Beispiel eine informative Nachricht, eine Warnung oder eine Fehlermeldung.
	 * 
	 * @param text
	 *            Der textuelle Inhalt der Nachricht.
	 * @param messageType
	 *            Einer der {@link javax.swing.JOptionPane} messageTypes.
	 */
	public void message(String text, int messageType);

	/**
	 * Wird vom Server aufgerufen, wenn der Client den Download einer Datei anfordert.
	 * 
	 * @param filename
	 *            Der Dateiname der anzulegenden Datei.
	 * @param fileSize
	 *            Die Größe in Bytes.
	 * @return Einen Token, mit dem die Channel-Verbindung identifiziert wird.
	 * @throws SimonException
	 *             Siehe {@link de.root1.simon.Simon#prepareRawChannel}.
	 * @throws FileAlreadyExistsException
	 *             Falls die Datei schon existiert und nicht überschrieben werden soll.
	 */
	public int prepareRawChannel(String filename, long fileSize) throws SimonException, FileAlreadyExistsException;
}
