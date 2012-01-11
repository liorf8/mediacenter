package de.dhbw_mannheim.tit09a.tcom.mediencenter.app.modell.action;

import javax.swing.JFileChooser;


import de.dhbw_mannheim.tit09a.tcom.mediencenter.app.controller.VLCController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.app.view.MainFrame;

public class OpenFileAction extends AbstractMediaCenterAction
{
    private static final long serialVersionUID = 5257631339826343072L;

    public OpenFileAction()
    {
	super("Datei öffnen...");
    }

    @Override
    public void startAction()
    {
	this.setEnabled(false);
	
	JFileChooser fileChooser = new JFileChooser();
	if (fileChooser.showOpenDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION)
	{
	    String path;
	    path = fileChooser.getSelectedFile().getAbsolutePath();
	    System.out.println("Path:" + path);
	    
	    // TODO:  äöüß and other non ANSI chars must be escaped like a.mkv results in this:
	   // path = "http://localhost:8080/web/upload/%C3%A4.mkv";

	    VLCController.getInstance(true).playMedia(path);
	}
	else
	{
	    System.out.println("abgebrochen");
	}
	
	this.setEnabled(true);
    }
}
