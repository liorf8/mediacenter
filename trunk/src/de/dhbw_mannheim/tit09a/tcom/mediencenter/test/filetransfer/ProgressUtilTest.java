package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.util.ProgressUtil;

public class ProgressUtilTest
{
    public static void main(String[] args)
    {
	long total = 31;
	long done =21;
	
	System.out.println(ProgressUtil.percentage(done, total));
	System.out.println(ProgressUtil.speed(35, 100));
	System.out.println(ProgressUtil.remainingSecs(3500, 350));
    }
}
