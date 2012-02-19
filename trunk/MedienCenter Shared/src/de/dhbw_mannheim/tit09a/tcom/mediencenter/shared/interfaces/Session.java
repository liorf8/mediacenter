package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileSystemException;
import java.rmi.ServerException;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public interface Session
{
	public void changeLogin(String pw, String newLogin) throws ServerException;

	public void changePw(String currentPw, String newPw) throws ServerException;

	public List<FileInfo> listFileInfos(String dirUri) throws ServerException, FileSystemException;

	public void createDir(String parentDirUri, String dirName) throws FileSystemException, ServerException;
	
	public void renameFile(String uri, String newName) throws FileSystemException, ServerException;
	
	public int copyFile(String srcUri, String targetDirUri, boolean replace) throws FileSystemException, ServerException;

	public int moveFile(String srcUri, String targetDirUri, boolean replace) throws FileSystemException, ServerException;
	
	public int deleteFile(String uri, boolean deleteNotEmptyDir) throws FileSystemException, ServerException;

	public int prepareRawChannel(String targetDirUri, String filename, long fileSize) throws FileSystemException, ServerException;

	public void downloadFile(String uri) throws FileSystemException, ServerException;
}
