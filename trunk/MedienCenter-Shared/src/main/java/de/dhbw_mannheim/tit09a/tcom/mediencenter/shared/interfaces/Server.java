package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.KeyAlreadyExistsException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;

/**
 * Das Interface des Servers. Dieser kann auf Client-Seite wie folgt aufgerufen werden:
 * 
 * <pre>
 * Lookup nameLookup = Simon.createNameLookup(Server.IP, Server.REGISTRY_PORT);
 * Server server = (Server) nameLookup.lookup(Server.BIND_NAME);
 * System.out.println(&quot;Current server time: &quot; + server.serverTime());
 * </pre>
 * 
 * Geht bei der Ausführung einer Methode auf Serverseite etwas unerwartet schief, wird immer eine {@link ServerException} geworfen. Dies ist eine
 * {@link RuntimeException}, sie muss also nicht abgefangen werden.
 * 
 * @author mhertram
 * 
 */
public interface Server
{
	/**
	 * Der Name, unter dem der Server über die SIMON-Registry erreichbar ist.
	 */
	public static final String	BIND_NAME		= "server";

	/**
	 * Der SIMON-Port.
	 */
	public static final int		REGISTRY_PORT	= 22222;

	/**
	 * Die IP des Servers.
	 */
	public static final String	IP				= "127.0.0.1";	// "192.168.2.111" - "127.0.0.1"

	/**
	 * @return Die aktuelle Zeit des Servers in MS seit 01.01.1970.
	 * @throws ServerException
	 */
	public long serverTime() throws ServerException;

	/**
	 * Um einen User beim Server zu registrieren. Ist der gew�nschte Login-Name bereits vergeben, wird eine KeyAlreadyExistsException geworfen.
	 * 
	 * @param login
	 *            Der Login-Name.
	 * @param pw
	 *            Das Passwort.
	 * @return Die ID des registrierten Users.
	 * @throws KeyAlreadyExistsException
	 *             Falls der gewünschte Login-Name schon vorhanden ist.
	 * @throws ServerException
	 */
	public long register(String login, String pw) throws KeyAlreadyExistsException, ServerException;

	/**
	 * Hiermit kann eine User-Session erstellt werden.
	 * 
	 * @param login
	 *            Der bei der Registrierung ({@link Server#register(String, String)}) angegebene Login-Name.
	 * @param pw
	 *            Das dazugehörige Passwort.
	 * @param callback
	 *            Ein {@link ClientCallback}, dessen Methoden vom Server aufgerufen werden.
	 * @return Eine {@link Session} f�r den User.
	 * @throws ServerException
	 */
	public Session login(String login, String pw, ClientCallback callback) throws ServerException;

	/**
	 * Um das Passwort des Users zurückzusetzen. Das neu generierte Passwort muss im Moment dann im Server-Log nachgelesen werden.
	 * 
	 * @param login
	 *            Der Login-Name f�r den das Passwort zur�ckgesetzt werden soll.
	 * @throws ServerException
	 */
	public void resetPw(String login) throws ServerException;
}
