package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import javax.swing.JLabel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class NoMediaInfoPanel extends MediaInfoPanel
{
	private static final long	serialVersionUID	= 4741032966940381448L;

	private JLabel lblMessage;
	
	public NoMediaInfoPanel(MainFrame mainFrame)
	{
		super(mainFrame);
		
		lblMessage = new JLabel();
		add(lblMessage);
	}
	
	public void setMessage(String msg)
	{
		lblMessage.setText(msg);
	}

	@Override
	public void reset()
	{
		lblMessage.setText("No media selected!");
	}
}
