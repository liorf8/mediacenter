package de.dhbw_mannheim.tit09a.tcom.test.proxy.dynamicproxy.myproxy;

import java.io.File;

public interface IFileController
{
    public boolean mkDir(File parentDir, File dir);
    
    public boolean deleteDir(File dir);
}
