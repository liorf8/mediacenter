package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection;


public interface ServerConnectionStateListener
{
	public void stateChanged(ServerConnectionState oldState, ServerConnectionState newState);
}
