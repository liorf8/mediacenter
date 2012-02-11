package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

public interface ClientCallback
{
	public void message(String text, int messageType);

	public void releaseConnection();
}
