package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.myfiletree;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class FileInfoTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	FileInfo fi = new FileInfo("D:", 1024*1024);

	System.out.println(fi.getAbsolutPath());
	System.out.println(fi.getFileName());
	System.out.println(fi.getFileSize());
    }

}
