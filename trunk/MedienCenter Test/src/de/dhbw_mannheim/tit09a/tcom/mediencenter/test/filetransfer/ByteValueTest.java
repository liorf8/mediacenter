package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.ByteValue.ByteUnit;

public class ByteValueTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	ByteValue bv = new ByteValue(Integer.MAX_VALUE);
	System.out.println(bv);
	System.out.println(bv.getBytes(ByteUnit.MEGABYTE));
    }

}
