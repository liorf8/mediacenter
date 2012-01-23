package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer.rmiio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.util.TimeValue;

public class FileServiceImpl implements FileService
{

    @Override
    public boolean uploadFile(File filePath, RemoteInputStream remoteFileData) throws IOException
    {
	InputStream fileData = RemoteInputStreamClient.wrap(remoteFileData);
	byte[] buffer = new byte[0xFFFF];
	int bytesRead = 0;
	long totalBytesRead = 0L;
	long start = System.currentTimeMillis();
	while ((bytesRead = fileData.read(buffer)) > 0)
	{
	    totalBytesRead += bytesRead;
	    //System.out.printf("Read %s (total %s)%n", new ByteValue(bytesRead), new ByteValue(
		//    totalBytesRead));
	}
	long duration = System.currentTimeMillis() - start;
	System.out.println("fertig");
	System.out.printf("Read %s in %s (%s/s)%n", new ByteValue(totalBytesRead), new TimeValue(
		duration), new ByteValue((int) ProgressUtil.speed(totalBytesRead, duration)));
	if (totalBytesRead > 0) return true;

	return false;
    }

}
