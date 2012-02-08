package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.Lookup;
import de.root1.simon.annotation.SimonRemote;

// mark this class as a remote class and export all methods known in ClientCallbackInterface
@SimonRemote(value = { ClientCallback.class })
public class ClientCallbackImpl implements ClientCallback
{
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    private final Lookup lookup;
    private final Server server;

    public ClientCallbackImpl(Lookup lookup, Server server)
    {
	this.lookup = lookup;
	this.server = server;
    }

    @Override
    public void message(final String text)
    {
	SwingUtilities.invokeLater(new Runnable()
	{
	    @Override
	    public void run()
	    {
		JOptionPane.showMessageDialog(null, text, "Message from Server",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	});
    }

    @Override
    public void releaseConnection()
    {
	if (lookup != null && server != null)
	    lookup.release(server);
	else
	{
	    System.err.println("Lookup or server == null: " + lookup + ", " + server);
	}
    }
}