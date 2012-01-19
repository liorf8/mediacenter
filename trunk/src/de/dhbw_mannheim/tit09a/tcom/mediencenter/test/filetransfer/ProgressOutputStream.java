package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;

//Similar to FilterOutputStream, but it was not necessary to extend the FilterOutputclass.
public class ProgressOutputStream extends OutputStream
{
    private PropertyChangeListener l;
    private long totalBytesWritten;
    private OutputStream out;

    public ProgressOutputStream(OutputStream out, PropertyChangeListener l)
    {
	this.out = out;
	this.l = l;
    }

    @Override
    public void write(int b) throws IOException
    {
	out.write(b);
	firePropertyChange(1);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
	write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
	out.write(b, off, len);
	firePropertyChange(len);
    }

    private void firePropertyChange(long bytesWritten)
    {
	l.propertyChange(new PropertyChangeEvent(this, "totalBytesWritten", bytesWritten,
		totalBytesWritten = totalBytesWritten + bytesWritten));
    }

}
