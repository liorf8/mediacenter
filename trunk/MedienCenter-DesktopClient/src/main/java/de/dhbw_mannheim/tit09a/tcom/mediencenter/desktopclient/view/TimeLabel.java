package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import javax.swing.JLabel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;

public class TimeLabel extends JLabel
{
	// --------------------------------------------------------------------------------
	// -- Class Variable(s) -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long	serialVersionUID	= 7347461386326835469L;
	private static final String	DEFAULT_TEXT		= "--:--/--:--";

	// --------------------------------------------------------------------------------
	// -- Instance Variable(s) --------------------------------------------------------
	// --------------------------------------------------------------------------------
	private long				currentTime;
	private long				fullTime;

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
	public void setCurrentTime(long millis)
	{
		this.currentTime = millis;
		updateText();
	}

	public long getCurrentTime()
	{
		return currentTime;
	}

	// --------------------------------------------------------------------------------
	public void setFullTime(long millis)
	{
		this.fullTime = millis;
		updateText();
	}

	public long getFullTime()
	{
		return fullTime;
	}

	private void updateText()
	{
		this.setText(TimeValue.formatMillis(currentTime, true, true) + "/" + TimeValue.formatMillis(fullTime, true, true));
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
