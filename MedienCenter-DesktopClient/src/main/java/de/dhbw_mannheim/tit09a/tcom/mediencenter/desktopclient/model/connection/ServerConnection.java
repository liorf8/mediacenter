package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection;

import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.InfoPlayer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.StreamPlayer;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;

public interface ServerConnection
{
	public ServerConnectionState getState();
	
	public void setServerHost(String serverHost);

	public String getServerHost();
	
	public void setServerRegistryPort(int serverRegistryPort);

	public int getServerRegistryPort();
	
	public void setServerBindname(String serverBindname);

	public String getServerBindname();

	public Session getSession();
	
	public InfoPlayer getInfoPlayer();
	
	public StreamPlayer getStreamPlayer();

	public Server getServer();
	
	public void connect() throws UnknownHostException, LookupFailedException, EstablishConnectionFailed;

	public void login(String login, String pw, ClientCallback callback) throws ServerException;

	public void disconnect();
}
