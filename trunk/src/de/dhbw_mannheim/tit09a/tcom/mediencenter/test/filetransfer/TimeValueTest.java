package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

public class TimeValueTest
{
    public static void main (String[] args)
    {
	long millis = 1000*60*60 + 1000*53 + 1000*60*60*24*4; // 1h
	TimeValue dv = new TimeValue(millis);
	System.out.println(dv);
    }
}
