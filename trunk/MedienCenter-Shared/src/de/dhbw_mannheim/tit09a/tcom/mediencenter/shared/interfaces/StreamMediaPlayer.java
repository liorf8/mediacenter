package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.awt.Dimension;
import java.nio.file.FileSystemException;
import java.rmi.ServerException;

public interface StreamMediaPlayer
{
	public static final String	DEFAULT_PROTOCOL	= "http";
	public static final int		DEFAULT_PORT		= 5555;
	public static final String	DEFAULT_RTSP_PATH	= "/mediencenter";	// "/mediencenter"

	public String setStreamTarget(String protocol, int port) throws ServerException;

	public String getMrlForClient() throws ServerException;
	
	public void play(String path) throws FileSystemException, ServerException;

	public void play() throws ServerException;

	public void stop() throws ServerException;

	public void pause() throws ServerException;

	public long getLength();

	public long getTime();

	public float getPosition();

	public void setPosition(float pos);

	public void skip(long delta);
	
	public RemoteMediaMeta getMediaMeta();

}
