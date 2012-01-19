package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;

// Similar to FilterInputStream, but it was not necessary to extend the FilterOutputclass.
public class ProgressInputStream extends InputStream
{
    private PropertyChangeListener l;
    private long totalBytesRead;
    private InputStream in;

    public ProgressInputStream(InputStream in, PropertyChangeListener l)
    {
	this.in = in;
	this.l = l;
    }

    public int read() throws IOException
    {
	// -1 is returned when no more bytes when no byte was left to read
	int nextByte = in.read();
	firePropertyChange(nextByte > -1 ? 1 : -1);
	return nextByte;
    }

    public int read(byte[] b) throws IOException
    {
	return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
	// -1 is returned when no more bytes were read
	int bytesRead = in.read(b, off, len);
	firePropertyChange(bytesRead);
	return bytesRead;
    }

    private void firePropertyChange(long bytesRead)
    {
	// Fire a propertyChange with the old value and the new cumulated totalBytesRead.
	// If bytesRead == -1, this indicates the end of reading.
	// To signal this totalyBytesRead is set to -1 as well.
	l.propertyChange(new PropertyChangeEvent(this, "totalBytesRead", totalBytesRead,
		totalBytesRead = (bytesRead > -1 ? totalBytesRead + bytesRead : -1)));
    }

}
