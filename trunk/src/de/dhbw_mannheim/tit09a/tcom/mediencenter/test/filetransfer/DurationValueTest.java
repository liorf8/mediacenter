package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

public class DurationValueTest
{
    public static void main (String[] args)
    {
	DurationValue dv = new DurationValue(352*1000);
	System.out.println(dv.getTotalMillis());
	System.out.println(dv);
    }
}
