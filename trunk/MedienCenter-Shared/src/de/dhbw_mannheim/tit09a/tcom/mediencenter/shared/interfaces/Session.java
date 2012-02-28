package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.nio.file.FileSystemException;
import java.rmi.ServerException;
import java.util.List;
import java.util.NoSuchElementException;

public interface Session
{
	public long getId();

	public String getLogin();

	public String getSessionId();

	public void changeLogin(String pw, String newLogin) throws ServerException;

	public void changePw(String currentPw, String newPw) throws ServerException;

	public List<FileInfo> listFileInfos(String dirPath) throws ServerException, FileSystemException;

	public String createDir(String parentDirPath, String dirName) throws FileSystemException, ServerException;

	public String renameFile(String path, String newName) throws FileSystemException, ServerException;

	public int copyFile(String srcPath, String targetDirPath, boolean replace) throws FileSystemException, ServerException;

	public int moveFile(String srcPath, String targetDirPath, boolean replace) throws FileSystemException, ServerException;

	public int deleteFile(String path, boolean deleteNotEmptyDir) throws FileSystemException, ServerException;

	public int prepareRawChannel(String targetDirPath, String filename, long fileSize) throws FileSystemException, ServerException;

	public void downloadFile(String path) throws FileSystemException, ServerException;

	public StreamMediaPlayer getRemoteMediaPlayer(String protocol, int port) throws NoSuchElementException, ServerException;
}
