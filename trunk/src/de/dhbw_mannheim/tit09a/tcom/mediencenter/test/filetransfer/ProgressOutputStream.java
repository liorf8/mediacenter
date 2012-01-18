package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeListener;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class ProgressOutputStream extends FilterOutputStream
{
    private PropertyChangeListener l;
    private long totalBytesWritten;

    public ProgressOutputStream(OutputStream out, PropertyChangeListener l)
    {
	super(out);
	this.l = l;
    }

}
