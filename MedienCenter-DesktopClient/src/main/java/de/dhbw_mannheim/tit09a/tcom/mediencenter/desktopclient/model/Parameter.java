package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model;

public class Parameter<T>
{
	private final String	key;
	private T				value;
	private String			label;
	private String			description;

	public Parameter(String key, String label, String description)
	{
		this.key = key;
		this.label = label;
		this.description = description;
	}

	public String getKey()
	{
		return key;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof Parameter<?>)
		{
			if (((Parameter<?>) obj).getKey().equals(key))
				return true;
		}
		return false;
	}

	public int hashCode()
	{
		return key.hashCode();
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

}
