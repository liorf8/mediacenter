package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.session;

import de.root1.simon.annotation.SimonRemote;

// mark this class as a remote class and export all methods known in ClientCallbackInterface
@SimonRemote(value = { ClientCallback.class })
public class ClientCallbackImpl implements ClientCallback
{
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Override
    public void callback(String text)
    {
	System.out.println(Thread.currentThread() +": This message was received from the server: " + text);

    }
}