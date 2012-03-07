package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui;

import javax.swing.JLabel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;


public class TimeLabel extends JLabel
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private static final long serialVersionUID = 7347461386326835469L;
    private static final String DEFAULT_TEXT = "--:--/--:--";

    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private String currentTime = "--:--";
    private String fullTime = "--:--";

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public TimeLabel()
    {
	this(DEFAULT_TEXT);
    }

    // --------------------------------------------------------------------------------
    public TimeLabel(String text)
    {
	super(text);
    }

    // --------------------------------------------------------------------------------
    // -- Instance Method(s) ----------------------------------------------------------
    // --------------------------------------------------------------------------------
    public void setCurrentTime(String currentTime)
    {
	this.currentTime = currentTime;
	this.setText(this.currentTime + "/" + this.fullTime);
    }

    // --------------------------------------------------------------------------------
    public void setCurrentTime(long millis)
    {
	this.setCurrentTime(TimeValue.formatMillis(millis,  true, true));
    }

    // --------------------------------------------------------------------------------
    public void setFullTime(String fullTime)
    {
	this.fullTime = fullTime;
	this.setText(this.currentTime + "/" + this.fullTime);
    }

    // --------------------------------------------------------------------------------
    public void setFullTime(long millis)
    {
	this.setFullTime(TimeValue.formatMillis(millis,  true, true));
    }

    // --------------------------------------------------------------------------------
    public void reset()
    {
	this.setText(DEFAULT_TEXT);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
