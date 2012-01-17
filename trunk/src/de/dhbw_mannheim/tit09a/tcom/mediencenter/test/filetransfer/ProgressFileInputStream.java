package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;

public class ProgressFileInputStream extends FilterInputStream
{
    private File file;
    private PropertyChangeListener l;
    private long totalBytesRead;

    protected ProgressFileInputStream(PropertyChangeListener l, File file)
	    throws FileNotFoundException
    {
	super(new FileInputStream(file));

	this.l = l;
	this.setFile(file);
    }

    public int read() throws IOException
    {
	int nextByte = super.read();
	if (nextByte != -1) // is returned when no more bytes
	    firePropertyChange(1);
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
	    firePropertyChange(bytesRead);
	return bytesRead;
    }

    public long skip(long n) throws IOException
    {
	long bytesSkipped = super.skip(n);
	firePropertyChange(bytesSkipped);
	return bytesSkipped;
    }

    public File getFile()
    {
	return file;
    }

    public void setFile(File file)
    {
	// reset the value
	totalBytesRead = 0;

	if (this.file != file)
	{
	    l.propertyChange(new PropertyChangeEvent(this, "file", this.file, file));
	    this.file = file;
	}
    }

    private void firePropertyChange(long bytesRead)
    {
	l.propertyChange(new PropertyChangeEvent(this, "totalBytesRead", totalBytesRead,
		totalBytesRead = totalBytesRead + bytesRead));
    }

}
