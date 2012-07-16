package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.documents;

public interface ChangeDeniedListener<T>
{
	public static enum UpdateMethod { INSERT, DELETE };
	
	public void changeDenied(RestrainedDocument<T> src, UpdateMethod method, String deniedContent, Exception reason);
}
