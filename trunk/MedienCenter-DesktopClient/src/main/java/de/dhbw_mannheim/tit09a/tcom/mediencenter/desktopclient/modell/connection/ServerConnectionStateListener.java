package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;


public interface ServerConnectionStateListener
{
	public void stateChanged(ServerConnectionState oldState, ServerConnectionState newState);
}
