package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

public interface ClientCallback
{
	public int getStreamingPort();
	
	public void message(String text, int messageType);

	public int prepareRawChannel(String filename, long fileSize);
}
