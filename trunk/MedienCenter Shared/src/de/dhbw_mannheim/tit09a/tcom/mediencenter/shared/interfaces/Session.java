package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ReturnObj;

public interface Session
{
	public ReturnObj<Void> changePw(String currentPw, String newPw) throws IllegalArgumentException, ServerException;
	
	public ReturnObj<Void> changeLogin(String pw, String newLogin) throws IllegalArgumentException, ServerException;
	
	public ReturnObj<Void> copyFile(String srcUri, String destDirUri, boolean replace) throws IllegalArgumentException, ServerException;

	public ReturnObj<Void> deleteFile(String uri) throws IllegalArgumentException, ServerException;

	public ReturnObj<FileInfo[]> listFileInfos(String dirUri) throws IllegalArgumentException, ServerException;

	public ReturnObj<Void> mkDir(String parentDirUri, String newDirName) throws IllegalArgumentException, ServerException;

	public ReturnObj<Integer> openFileChannel(String destDirUri, String filename, long fileSize) throws IllegalArgumentException, ServerException;

	public ReturnObj<Void> renameFile(String uri, String newName) throws IllegalArgumentException, ServerException;

}
