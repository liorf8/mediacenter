package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;

public class MediaCenterApp
{

	public static void main(String[] args)
	{
		// Properties systemproperties = System.getProperties();
		// systemproperties.list(System.out);

		MainController.getInstance();
	}

}