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
    private long newTotalBytesRead;
    private int progress;
    private int newProgress;
    

    protected ProgressInputStream(PropertyChangeListener l, File file) throws FileNotFoundException
    {
	super(new FileInputStream(file));

	this.l = l;
	this.setFile(file);

	// reset the value
	totalBytesRead = 0;
	progress = 0;
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

    public void setFile(File file)
    {
	if (this.file != file)
	{
	    l.propertyChange(new PropertyChangeEvent(this, "file", this.file, file));
	    this.file = file;
	}
    }

    private void mayFirePropertyChanges(long bytesRead)
    {
	newTotalBytesRead = totalBytesRead + bytesRead;
	if (newTotalBytesRead != totalBytesRead)
	{
	    l.propertyChange(new PropertyChangeEvent(this, "totalBytesRead", totalBytesRead,
		    newTotalBytesRead));
	    totalBytesRead = newTotalBytesRead;
	}

	newProgress = (int) (totalBytesRead * 100 / file.length());
	if (newProgress != progress)
	{
	    l.propertyChange(new PropertyChangeEvent(this, "progress", progress, newProgress));
	    progress = newProgress;
	}
    }

}
