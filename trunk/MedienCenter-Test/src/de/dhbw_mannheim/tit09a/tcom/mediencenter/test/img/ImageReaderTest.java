package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

public class ImageReaderTest
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println(Arrays.toString(ImageIO.getReaderMIMETypes()));
		
		//BufferedImage img = ImageIO.read(new File("D:\\mhertram\\MedienCenter\\USER_FILES\\1\\Pictures\\bild.bmp"));
		//System.out.println(img);
	}

}
