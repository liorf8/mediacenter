package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;

public class ProgressInputStream extends FilterInputStream
{
    private File file;
    private PropertyChangeListener l;
    private long totalBytesRead;
    private int percentage;

    protected ProgressInputStream(PropertyChangeListener l, File file) throws FileNotFoundException
    {
	super(new FileInputStream(file));

	this.l = l;
	this.file = file;

	// reset the value
	totalBytesRead = 0;
	percentage = 0;
    }

    public int read() throws IOException
    {
	int nextByte = super.read();
	if (nextByte != -1) // is returned when no more bytes
	    mayFirePropertyChanges(1);
	return nextByte;
    }

    public int read(byte[] b) throws IOException
    {
	return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
	int bytesRead = super.read(b, off, len);
	if (bytesRead != -1) // is returned when no more bytes
	    mayFirePropertyChanges(bytesRead);
	return bytesRead;
    }

    public long skip(long n) throws IOException
    {
	long bytesSkipped = super.skip(n);
	mayFirePropertyChanges(bytesSkipped);
	return bytesSkipped;
    }

    public File getFile()
    {
	return file;
    }

    private void mayFirePropertyChanges(long bytesRead)
    {
	long newTotalBytesRead = totalBytesRead + bytesRead;
	if (newTotalBytesRead != totalBytesRead)
	{
	    totalBytesRead = newTotalBytesRead;
	    l.propertyChange(new PropertyChangeEvent(this, "totalBytesRead", totalBytesRead, newTotalBytesRead));
	}
	
	int newPercentage = (int) (totalBytesRead * 100 / file.length());
	if (newPercentage != percentage)
	{
	    percentage = newPercentage;
	    l.propertyChange(new PropertyChangeEvent(this, "progress", percentage, newPercentage));
	}
    }

}
