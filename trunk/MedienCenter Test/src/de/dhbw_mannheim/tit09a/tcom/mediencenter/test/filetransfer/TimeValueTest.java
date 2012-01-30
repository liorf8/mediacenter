package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;

public class TimeValueTest
{
    public static void main (String[] args)
    {
	//            4days		1hour		53s	300ms
	long millis = 1000*60*60*24*4 + 1000*60*60 + 1000*53 +  300;
	TimeValue dv = new TimeValue(millis);
	System.out.println(dv);
	System.out.println(dv.toString(false, false));
    }
}
