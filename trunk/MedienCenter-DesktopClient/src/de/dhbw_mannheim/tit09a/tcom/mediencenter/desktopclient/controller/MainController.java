package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class MainController
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private static MainController instance;
    public static enum  OS { WIN, NUX, NIX, MAC, OTHER };
    
    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private boolean is64BitVM;
    public OS operatingSystem;

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    private MainController()
    {
	try
	{
	    checkStartConditions();
	    applyLookNFeel();
	    MainFrame.getInstance();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static MainController getInstance()
    {
	if (instance == null)
	{
	    return instance = new MainController();
	}
	return instance;
    }

    // --------------------------------------------------------------------------------
    public boolean is64BitVM()
    {
	return is64BitVM;
    }

    // --------------------------------------------------------------------------------
    public OS getOs()
    {
	return operatingSystem;
    }
    
    // --------------------------------------------------------------------------------
    // -- Private Method(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private void checkStartConditions()
    {
	//Operating System
	operatingSystem = checkOs();
	System.out.println("Betriebssystem: "+operatingSystem);
	    
	// Java VM
	is64BitVM = check64BitVM();
	if (is64BitVM)
	{
	    JOptionPane.showMessageDialog(null,
		    "Bitte starten Sie dieses Programm mit einer 32-bit Java VM.",
		    "Keine 64-bit Java VM unterstützt", JOptionPane.ERROR_MESSAGE);
	    System.exit(1);
	}
	
	// VLC
	if(VLCController.getVLCInstallDir() == null)
	{
	    JOptionPane.showMessageDialog(null,
		    "Bitte installieren Sie die 32-bit-Version von VLC.",
		    "Keine VLC-Installation gefunden", JOptionPane.ERROR_MESSAGE);
	    System.exit(1); 
	}
    }

    // --------------------------------------------------------------------------------
    private boolean check64BitVM()
    {
	String vmName = System.getProperty("java.vm.name");
	System.out.println("VM@" + vmName);
	if (vmName.indexOf("64") > 0)
	{
	    return true;
	}
	return false;
    }
    
    // --------------------------------------------------------------------------------
    private OS checkOs()
    {
	String os = System.getProperty("os.name").toLowerCase();
	if(os.indexOf("win") >= 0)
	{
	    return OS.WIN;
	}
	else if(os.indexOf("nix") >= 0)
	{
	    return OS.NIX;
	}
	else if(os.indexOf("nux") >= 0)
	{
	    return OS.NUX;
	}
	else if(os.indexOf("mac") >= 0)
	{
	    return OS.MAC;
	}
	else
	{
	    return OS.OTHER;
	}
    }

    // --------------------------------------------------------------------------------
    private static void applyLookNFeel()
    {
	try
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    System.out.println(" + System Look'n'Feel applied!");
	}
	catch (Exception e)
	{
	    System.err.println("Error while trying to apply system Look'n'Feel: " + e);
	}
    }
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
