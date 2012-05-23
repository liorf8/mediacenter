package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.documents;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.documents.ChangeDeniedListener.UpdateMethod;


public abstract class RestrainedDocument<T> extends PlainDocument
{
	private static final long	serialVersionUID	= -2925207309958872115L;

	private T					checkedValue;

	private List<ChangeDeniedListener<T>>	changeDeniedListeners			= new CopyOnWriteArrayList<>();


	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void addChangeDeniedListener(ChangeDeniedListener<T> l)
	{
		changeDeniedListeners.add(l);
	}

	// --------------------------------------------------------------------------------
	public void removeChangeDeniedListener(ChangeDeniedListener<T> l)
	{
		changeDeniedListeners.remove(l);
	}

	public abstract String getValidContentDescription();

	
	@Override
	public void insertString(int offset, String string, AttributeSet attributes) throws BadLocationException
	{
		if (string == null)
		{
			return;
		}
		else
		{
			String newValue;
			int length = getLength();
			if (length == 0)
			{
				newValue = string;
			}
			else
			{
				String currentContent = getText(0, length);
				StringBuffer currentBuffer = new StringBuffer(currentContent);
				currentBuffer.insert(offset, string);
				newValue = currentBuffer.toString();
			}
			try
			{
				checkedValue = checkInput(newValue);
				super.insertString(offset, string, attributes);
			}
			catch (Exception e)
			{
				for(ChangeDeniedListener<T> l : changeDeniedListeners)
				{
					l.changeDenied(this, UpdateMethod.INSERT, newValue, e);
				}
			}
		}
	}

	@Override
	public void remove(int offset, int length) throws BadLocationException
	{
		int currentLength = getLength();
		String currentContent = getText(0, currentLength);
		String before = currentContent.substring(0, offset);
		String after = currentContent.substring(length + offset, currentLength);
		String newValue = before + after;
		try
		{
			checkedValue = checkInput(newValue);
			super.remove(offset, length);
		}
		catch (Exception e)
		{
			for(ChangeDeniedListener<T> l : changeDeniedListeners)
			{
				l.changeDenied(this, UpdateMethod.DELETE, newValue, e);
			}
		}
	}

	
	// checks the value if it is valid and returns the casted value
	public abstract T checkInput(String proposedValue) throws Exception;

	public T getValue()
	{
		return checkedValue;
	}
}
