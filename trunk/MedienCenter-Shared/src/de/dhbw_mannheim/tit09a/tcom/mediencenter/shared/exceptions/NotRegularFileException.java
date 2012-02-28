package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions;

import java.nio.file.FileSystemException;

public class NotRegularFileException extends FileSystemException
{
	private static final long	serialVersionUID	= -3496905612288700889L;

	public NotRegularFileException(String file)
	{
		super(file);
		// TODO Auto-generated constructor stub
	}

	public NotRegularFileException(String file, String other, String reason)
	{
		super(file, other, reason);
		// TODO Auto-generated constructor stub
	}
}
