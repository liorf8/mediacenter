package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.util.Map;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public interface Session
{
	public void putAttr(String key, String newValue) throws IllegalArgumentException, ServerException;

	public Map<String, String> getAllAttrs() throws ServerException;

	public String getAttr(String key) throws IllegalArgumentException, ServerException;

	public boolean copyFile(String srcUri, String destDirUri, boolean replace) throws IllegalArgumentException, ServerException;

	public boolean deleteFile(String uri, boolean onlyIfDirIsEmpty) throws IllegalArgumentException, ServerException;

	public FileInfo[] listFileInfos(String dirUri) throws IllegalArgumentException, ServerException;

	public boolean mkDir(String parentDirUri, String newDirName) throws IllegalArgumentException, ServerException;

	public int openFileChannel(String destDirUri, String filename, long fileSize) throws IllegalArgumentException, ServerException;

	public boolean renameFile(String uri, String newName) throws IllegalArgumentException, ServerException;

}
