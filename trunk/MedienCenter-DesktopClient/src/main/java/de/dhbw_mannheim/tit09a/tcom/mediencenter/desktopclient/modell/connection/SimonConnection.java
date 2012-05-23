package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public interface SimonConnection
{
	public SimonConnectionState getState();
	
	public void setServerHost(String serverHost);

	public String getServerHost();
	
	public void setServerRegistryPort(int serverRegistryPort);

	public int getServerRegistryPort();
	
	public void setServerBindname(String serverBindname);

	public String getServerBindname();

	public Session getSession();

	public Server getServer();
	
	public void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed;

	public void login(String login, String pw, ClientCallback callback) throws ServerException;

	public void disconnect();
}
