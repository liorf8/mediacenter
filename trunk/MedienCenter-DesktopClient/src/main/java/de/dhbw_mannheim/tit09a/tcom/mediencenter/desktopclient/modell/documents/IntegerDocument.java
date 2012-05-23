package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.documents;

public class IntegerDocument extends RestrainedDocument<Integer>
{
	private static final long	serialVersionUID	= -3778137454684387864L;

	@Override
	public Integer checkInput(String proposedValue) throws Exception
	{
		return Integer.parseInt(proposedValue);
	}

	@Override
	public String getValidContentDescription()
	{
		return "an integer";
	}

}
