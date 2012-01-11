package de.dhbw_mannheim.tit09a.tcom.app.modell.gui;

import javax.swing.JLabel;

import de.dhbw_mannheim.tit09a.tcom.app.modell.Duration;

public class TimeLabel extends JLabel
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private static final long serialVersionUID = 7347461386326835469L;
    private static final String defaultString = "--:--/--:--";

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
	this(defaultString);
    }

    // --------------------------------------------------------------------------------
    public TimeLabel(String text)
    {
	super(text);
    }

    // --------------------------------------------------------------------------------
    // -- Static Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    private static String millisToTimeString(long millis)
    {
	return millisToTimeString(millis, true, true);
    }
    
    // --------------------------------------------------------------------------------
    private static String millisToTimeString(long millis, boolean abandonMillis,
	    boolean abandonZeroHours)
    {
	Duration duration = new Duration(millis);

	StringBuffer timeSB = new StringBuffer();

	if (!abandonZeroHours || duration.getHours() != 0)
	{
	    timeSB.append(String.format("%02d:", duration.getHours()));
	}

	timeSB.append(String.format("%02d:", duration.getMins()));
	timeSB.append(String.format("%02d", duration.getSecs()));

	if (!abandonMillis)
	{
	    timeSB.append("," + duration.getMillis());
	}

	return timeSB.toString();
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
	this.setCurrentTime(millisToTimeString(millis));
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
	this.setFullTime(millisToTimeString(millis, true, true));
    }

    // --------------------------------------------------------------------------------
    public void reset()
    {
	this.setText(defaultString);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
