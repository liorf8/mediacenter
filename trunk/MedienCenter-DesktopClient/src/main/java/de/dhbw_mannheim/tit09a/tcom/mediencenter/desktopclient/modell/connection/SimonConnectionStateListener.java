package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection;


public interface SimonConnectionStateListener
{
	public void stateChanged(SimonConnectionState oldState, SimonConnectionState newState);
}
