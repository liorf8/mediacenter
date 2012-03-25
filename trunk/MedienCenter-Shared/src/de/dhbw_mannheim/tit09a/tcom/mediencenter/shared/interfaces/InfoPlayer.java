package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileSystemException;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;

import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.TrackInfo;

public interface InfoPlayer
{
	public boolean isValid();
	
	public long getLength(String path) throws ServerException, FileSystemException;

	public List<TrackInfo> getTrackInfo(String path) throws ServerException, FileSystemException;

	public MediaMeta getMediaMeta(String path) throws ServerException, FileSystemException;
	
	public MediaDetails getMediaDetails(String path) throws ServerException, FileSystemException;
}
