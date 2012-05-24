package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class Settings
{
	public final String						PROPS_FILE									= "mediascrub_properties.txt";

	public static final String				KEY_FRAME_WIDTH								= "frame.width";
	public static final String				KEY_FRAME_HEIGHT							= "frame.height";
	public static final String				KEY_SETTINGS_SELECTED_CATEGORY				= "settings.selectedCategory";
	public static final String				KEY_SETTINGS_DIVIDER_LOCATION				= "settings.dividerLocation";
	public static final String				KEY_SERVER_HOST								= "server.host";
	public static final String				KEY_SERVER_REGISTRY_PORT					= "server.registryPort";
	public static final String				KEY_SERVER_BINDNAME							= "server.bindname";
	public static final String				KEY_PLAYER_VOLUME							= "player.volume";
	public static final String				KEY_PLAYER_TOOLBAR_LOCATION					= "player.toolbarLocation";
	public static final String				KEY_STREAMING_PROTOCOL						= "streaming.protocol";
	public static final String				KEY_STREAMING_DESTINATION_PORT				= "streaming.destinationPort";
	public static final String				KEY_STREAMING_TRANSCODING_ACTIVE			= "streaming.transcoding.active";
	public static final String				KEY_STREAMING_TRANSCODING_VIDEO_CODEC		= "streaming.transcoding.videoCodec";
	public static final String				KEY_STREAMING_TRANSCODING_AUDIO_CODEC		= "streaming.transcoding.audioCodec";
	public static final String				KEY_STREAMING_TRANSCODING_VIDEO_KBIT_RATE	= "streaming.transcoding.videoKBitRate";
	public static final String				KEY_STREAMING_TRANSCODING_AUDIO_KBIT_RATE	= "streaming.transcoding.audioKBitRate";
	public static final String				KEY_STREAMING_TRANSCODING_AUDIO_SYNC		= "streaming.transcoding.audioSync";
	public static final String				KEY_STREAMING_TRANSCODING_DEINTERLACE		= "streaming.transcoding.deinterlace";

	private Map<String, String>				defaultProps								= new HashMap<>(15);
	private Properties						runtimeProps								= new Properties();
	private List<Parameter<?>>				parameters									= new ArrayList<>(15);

	private List<PropertyChangeListener>	propChangeListeners							= new CopyOnWriteArrayList<>();

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public Settings()
	{
		this(null);
	}

	// --------------------------------------------------------------------------------
	// very important to add the listener this early, so that the default properties can have effect
	public Settings(PropertyChangeListener l)
	{
		if (l != null)
			addPropertyChangeListener(l);

		// add the parameter and store the default settings
		parameters.add(new Parameter<Integer>(KEY_FRAME_WIDTH, KEY_FRAME_WIDTH, "INTERNAL"));
		defaultProps.put(KEY_FRAME_WIDTH, "760");

		parameters.add(new Parameter<Integer>(KEY_FRAME_HEIGHT, KEY_FRAME_HEIGHT, "INTERNAL"));
		defaultProps.put(KEY_FRAME_HEIGHT, "480");

		parameters.add(new Parameter<String>(KEY_SETTINGS_SELECTED_CATEGORY, KEY_SETTINGS_SELECTED_CATEGORY, "INTERNAL"));
		defaultProps.put(KEY_SETTINGS_SELECTED_CATEGORY, "Server");

		parameters.add(new Parameter<Integer>(KEY_SETTINGS_DIVIDER_LOCATION, KEY_SETTINGS_DIVIDER_LOCATION, "INTERNAL"));
		defaultProps.put(KEY_SETTINGS_DIVIDER_LOCATION, "150");

		parameters.add(new Parameter<String>(KEY_SERVER_HOST,

		"Server host", "The host where the server is located. E.g. 127.0.0.1 or localhost"));
		defaultProps.put(KEY_SERVER_HOST, "localhost");
		parameters.add(new Parameter<Integer>(KEY_SERVER_REGISTRY_PORT,

		"Server registry port", "The port where the server's SIMON registry is available"));
		defaultProps.put(KEY_SERVER_REGISTRY_PORT, "22222");
		parameters.add(new Parameter<String>(KEY_SERVER_BINDNAME,

		"Server bindname", "The bindname of the server interface in the server's SIMON registry"));
		defaultProps.put(KEY_SERVER_BINDNAME, "server");
		parameters.add(new Parameter<String>(KEY_PLAYER_TOOLBAR_LOCATION,

		"Toolbar location", "The location of the play utility toolbar"));
		defaultProps.put(KEY_PLAYER_TOOLBAR_LOCATION, BorderLayout.SOUTH); // South or North

		parameters.add(new Parameter<Integer>(KEY_PLAYER_VOLUME, KEY_PLAYER_VOLUME, "INTERNAL"));
		defaultProps.put(KEY_PLAYER_VOLUME, "50");

		parameters.add(new Parameter<String>(KEY_STREAMING_PROTOCOL, "Streaming protocol", "http, rtp or rtsp"));
		defaultProps.put(KEY_STREAMING_PROTOCOL, "http");

		parameters.add(new Parameter<Integer>(KEY_STREAMING_DESTINATION_PORT,
				"Streaming destination port",
				"The port on your computer, where the stream should be sent to"));
		defaultProps.put(KEY_STREAMING_DESTINATION_PORT, "5000");

		parameters.add(new Parameter<Integer>(KEY_STREAMING_TRANSCODING_ACTIVE, "Activate transcoding", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_ACTIVE, "false");

		parameters.add(new Parameter<String>(KEY_STREAMING_TRANSCODING_VIDEO_CODEC, "Video codec", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_VIDEO_CODEC, "h264");

		parameters.add(new Parameter<String>(KEY_STREAMING_TRANSCODING_AUDIO_CODEC, "Audio codec", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_AUDIO_CODEC, "mp3");

		parameters.add(new Parameter<Integer>(KEY_STREAMING_TRANSCODING_VIDEO_KBIT_RATE, "Video kBit rate", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_VIDEO_KBIT_RATE, "1024");

		parameters.add(new Parameter<Integer>(KEY_STREAMING_TRANSCODING_AUDIO_KBIT_RATE, "Audio kBit rate", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_AUDIO_KBIT_RATE, "192");

		parameters.add(new Parameter<Boolean>(KEY_STREAMING_TRANSCODING_AUDIO_SYNC, "Audio-sync", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_AUDIO_SYNC, "true");

		parameters.add(new Parameter<Boolean>(KEY_STREAMING_TRANSCODING_DEINTERLACE, "Deinterlace", "bla"));
		defaultProps.put(KEY_STREAMING_TRANSCODING_DEINTERLACE, "true");

		// store them in the runtime properties
		for (Entry<String, String> entry : defaultProps.entrySet())
		{
			setProperty(entry.getKey(), entry.getValue());
		}

		// overwrite the defaults with the read ins
		try
		{
			readProperties();
		}
		catch (IOException e)
		{
			System.out.println("Exception while trying to read properties (" + e.toString()
					+ "). Using defaults. The property file gets created/updated when you close this application.");
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		propChangeListeners.add(l);
	}

	// --------------------------------------------------------------------------------
	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		propChangeListeners.remove(l);
	}

	// --------------------------------------------------------------------------------
	public Parameter<?> getParameter(String key)
	{
		for (Parameter<?> p : parameters)
		{
			if (p.getKey().equals(key))
				return p;
		}
		return null;
	}

	// --------------------------------------------------------------------------------
	public String getDefault(String key)
	{
		return defaultProps.get(key);
	}

	// --------------------------------------------------------------------------------
	public int getDefaultAsInt(String key)
	{
		return Integer.valueOf(defaultProps.get(key));
	}

	// --------------------------------------------------------------------------------
	public boolean getDefaultAsBoolean(String key)
	{
		return Boolean.parseBoolean(defaultProps.get(key));
	}

	// --------------------------------------------------------------------------------
	public String getProperty(String key)
	{
		return runtimeProps.getProperty(key);
	}

	// --------------------------------------------------------------------------------
	public int getPropertyAsInt(String key)
	{
		return Integer.valueOf(runtimeProps.getProperty(key));
	}

	// --------------------------------------------------------------------------------
	public boolean getPropertyAsBoolean(String key)
	{
		return Boolean.parseBoolean(runtimeProps.getProperty(key));
	}

	// --------------------------------------------------------------------------------
	public void setProperty(String key, String newValue)
	{
		String oldValue = runtimeProps.getProperty(key);
		runtimeProps.setProperty(key, newValue);
		if ((newValue == null && oldValue != null) || (!newValue.equals(oldValue)))
		{
			for (PropertyChangeListener l : propChangeListeners)
			{
				l.propertyChange(new PropertyChangeEvent(this, key, oldValue, newValue));
			}
		}
	}

	// --------------------------------------------------------------------------------
	public void setProperty(String key, int newValue)
	{
		setProperty(key, newValue + "");
	}

	// --------------------------------------------------------------------------------
	public void setProperty(String key, boolean newValue)
	{
		setProperty(key, newValue + "");
	}

	// --------------------------------------------------------------------------------
	public void setToDefault(String key)
	{
		setProperty(key, getDefault(key));
	}

	// --------------------------------------------------------------------------------
	public void storeProperties() throws IOException
	{
		storeProperties(runtimeProps);
	}

	// --------------------------------------------------------------------------------
	public void readProperties() throws IOException
	{
		FileInputStream propInFile = new FileInputStream(PROPS_FILE);
		Properties loadedProps = new Properties();
		loadedProps.load(propInFile);
		System.out.println("Read properties:");
		loadedProps.list(System.out);

		// overwrite the default properties with the loaded ones
		Enumeration<?> propNames = loadedProps.propertyNames();
		while (propNames.hasMoreElements())
		{
			String key = (String) propNames.nextElement();
			setProperty(key, loadedProps.getProperty(key));
		}

	}

	// --------------------------------------------------------------------------------
	public boolean[] valuesEqualCurrentAndDefaultSettings(Map<String, Object> valueComponents)
	{
		boolean valuesEqualCurrentSettings = true;
		boolean valuesEqualDefaultSettings = true;

		for (Map.Entry<String, Object> entry : valueComponents.entrySet())
		{
			boolean[] valueEqualsCurrentAndDefaultSetting = valueEqualsCurrentAndDefaultSetting(entry.getKey(), entry.getValue());

			// compare
			if (!valueEqualsCurrentAndDefaultSetting[0])
			{
				valuesEqualCurrentSettings = false;
			}
			if (!valueEqualsCurrentAndDefaultSetting[1])
			{
				valuesEqualDefaultSettings = false;
			}
			// no more searching needs to be done, if both values are already false
			if (!valuesEqualCurrentSettings && !valuesEqualDefaultSettings)
			{
				break;
			}
		}

		return new boolean[] { valuesEqualCurrentSettings, valuesEqualDefaultSettings };
	}

	// --------------------------------------------------------------------------------
	public boolean[] valueEqualsCurrentAndDefaultSetting(String key, Object component)
	{
		String compValue = getComponentsValue(component);
		boolean valuesEqualCurrentSettings = getProperty(key).equals(compValue);
		boolean valuesEqualDefaultSettings = getDefault(key).equals(compValue);
		return new boolean[] { valuesEqualCurrentSettings, valuesEqualDefaultSettings };
	}

	// --------------------------------------------------------------------------------
	public void saveValuesToSettings(Map<String, Object> valueComponents)
	{
		for (Map.Entry<String, Object> entry : valueComponents.entrySet())
		{
			saveValueToSettings(entry.getKey(), entry.getValue());
		}
	}

	// --------------------------------------------------------------------------------
	public void saveValueToSettings(String key, Object component)
	{
		String compValue = getComponentsValue(component);
		setProperty(key, compValue);
	}

	// --------------------------------------------------------------------------------
	public void resetValuesAccordingToSettings(Map<String, Object> valueComponents, boolean resetToDefault)
	{
		for (Map.Entry<String, Object> entry : valueComponents.entrySet())
		{
			resetValueAccordingToSettings(entry.getKey(), entry.getValue(), resetToDefault);
		}
	}

	// --------------------------------------------------------------------------------
	public void resetValueAccordingToSettings(String key, Object component, boolean resetToDefault)
	{
		String property;
		if (resetToDefault)
			property = getDefault(key);
		else
			property = getProperty(key);

		setComponentsValue(component, property);
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static String getComponentsValue(Object component)
	{
		if (component instanceof JTextField)
		{
			return getComponentsValue((JTextField) component);
		}
		else if (component instanceof JSpinner)
		{
			return getComponentsValue((JSpinner) component);
		}
		else if (component instanceof AbstractButton)
		{
			return getComponentsValue((AbstractButton) component);
		}
		else if (component instanceof ButtonGroup)
		{
			return getComponentsValue((ButtonGroup) component);
		}
		else if (component instanceof JComboBox<?>)
		{
			return getComponentsValue((JComboBox<?>) component);
		}
		else
		{
			System.err.print("WARNING: JComponent " + component + " not supported for getComponentsValue()!");
			return null;
		}
	}

	// --------------------------------------------------------------------------------
	public static String getComponentsValue(JTextField txtFld)
	{
		return txtFld.getText();
	}

	// --------------------------------------------------------------------------------
	public static String getComponentsValue(JSpinner spnr)
	{
		SpinnerModel model = spnr.getModel();
		if (model instanceof SpinnerNumberModel)
		{
			SpinnerNumberModel numModel = (SpinnerNumberModel) model;
			if (numModel.getNumber() instanceof Integer)
				return "" + (int) spnr.getValue();
			else
				throw new UnsupportedOperationException("SpinnerModel not supported: " + model);
		}

		else
		{
			throw new UnsupportedOperationException("SpinnerModel not supported: " + model);
		}
	}

	// --------------------------------------------------------------------------------
	// for JCheckBox
	public static String getComponentsValue(AbstractButton btn)
	{
		return btn.isSelected() + "";
	}

	// --------------------------------------------------------------------------------
	// for Radio Buttons
	public static String getComponentsValue(ButtonGroup btnGroup)
	{
		Enumeration<AbstractButton> btns = btnGroup.getElements();
		while (btns.hasMoreElements())
		{
			AbstractButton btn = btns.nextElement();
			if (btn.isSelected())
			{
				return btn.getText();
			}
		}
		System.err.println("WARNING: ButtonGroup " + btnGroup + " has no selected Button!");
		return null;
	}

	// --------------------------------------------------------------------------------
	public static String getComponentsValue(JComboBox<?> box)
	{
		return (String) box.getSelectedItem();
	}

	// --------------------------------------------------------------------------------
	public static void setComponentsValue(Object component, String value)
	{
		if (component instanceof JTextField)
		{
			setComponentsValue((JTextField) component, value);
		}
		else if (component instanceof JSpinner)
		{
			setComponentsValue((JSpinner) component, value);
		}
		else if (component instanceof AbstractButton)
		{
			setComponentsValue((AbstractButton) component, value);
		}
		else if (component instanceof ButtonGroup)
		{
			setComponentsValue((ButtonGroup) component, value);
		}
		else if (component instanceof JComboBox<?>)
		{
			setComponentsValue((JComboBox<?>) component, value);
		}
		else
		{
			System.err.print("WARNING: JComponent " + component + " not supported for setComponentsValue()!");
		}
	}

	// --------------------------------------------------------------------------------
	public static void setComponentsValue(JTextField txtFld, String value)
	{
		txtFld.setText(value);
	}

	// --------------------------------------------------------------------------------
	public static void setComponentsValue(JSpinner spnr, String value)
	{
		SpinnerModel model = spnr.getModel();
		if (model instanceof SpinnerNumberModel)
		{
			SpinnerNumberModel numModel = (SpinnerNumberModel) model;
			if (numModel.getNumber() instanceof Integer)
				spnr.setValue(Integer.parseInt(value));
			else
				throw new UnsupportedOperationException("SpinnerModel not supported: " + model);
		}
		else
		{
			throw new UnsupportedOperationException("SpinnerModel not supported: " + model);
		}
	}

	// --------------------------------------------------------------------------------
	// For JCheckBox
	public static void setComponentsValue(AbstractButton btn, String value)
	{
		btn.setSelected(Boolean.parseBoolean(value));
	}

	// --------------------------------------------------------------------------------
	// For JCheckBox
	public static void setComponentsValue(ButtonGroup btnGroup, String value)
	{
		Enumeration<AbstractButton> btns = btnGroup.getElements();
		while (btns.hasMoreElements())
		{
			AbstractButton btn = btns.nextElement();
			if (btn.getText().equals(value))
			{
				btn.setSelected(true);
				return;
			}
		}

		System.err.println("WARNING: ButtonGroup " + btnGroup + " had no Button named " + value + ". No Button was selected!");
	}

	// --------------------------------------------------------------------------------
	public static void setComponentsValue(JComboBox<?> box, String value)
	{
		box.setSelectedItem(value);
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void storeProperties(Properties props) throws IOException
	{
		FileOutputStream propOutFile = new FileOutputStream(PROPS_FILE);
		props.store(propOutFile, "");

		System.out.println("Stored properties:");
		props.list(System.out);
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}