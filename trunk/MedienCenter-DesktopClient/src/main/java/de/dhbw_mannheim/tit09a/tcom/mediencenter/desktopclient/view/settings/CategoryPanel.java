package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.Settings;

public abstract class CategoryPanel extends JPanel
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final long		serialVersionUID	= 2530319073866338148L;

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected JPanel				settingsPanel;
	protected CategoryButtonsPanel	btnsPanel;
	protected Settings				settings;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public CategoryPanel(String categoryName)
	{
		setName(categoryName);
		setMinimumSize(new Dimension(100, 200));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><u>" + getName() + "</u></html>"));
		setLayout(new BorderLayout());

		settings = MainController.getInstance().getSettings();

		settingsPanel = createCategoryPanel();
		add(settingsPanel, BorderLayout.CENTER);

		btnsPanel = new CategoryButtonsPanel();
		add(btnsPanel, BorderLayout.SOUTH);

		addValueChangeListeners();

		// insert the current settings
		settings.resetValuesAccordingToSettings(getValueComponents(), false);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	public String toString()
	{
		return getName();
	}

	// --------------------------------------------------------------------------------
	public abstract Map<String, Object> getValueComponents();

	// --------------------------------------------------------------------------------
	// -- Protected/Package Methods ---------------------------------------------------
	// --------------------------------------------------------------------------------
	protected abstract JPanel createCategoryPanel();

	// --------------------------------------------------------------------------------
	private void addValueChangeListeners()
	{
		for (Object comp : getValueComponents().values())
		{
			addValueChangeListener(comp);
		}
	}

	// --------------------------------------------------------------------------------
	private void addValueChangeListener(Object comp)
	{
		if (comp instanceof JTextField)
		{
			addValueChangeListener((JTextField) comp);
		}
		else if (comp instanceof JSpinner)
		{
			addValueChangeListener((JSpinner) comp);
		}
		else if (comp instanceof AbstractButton)
		{
			addValueChangeListener((AbstractButton) comp);
		}
		else if (comp instanceof ButtonGroup)
		{
			addValueChangeListener((ButtonGroup) comp);
		}
		else if (comp instanceof JComboBox<?>)
		{
			addValueChangeListener((JComboBox<?>) comp);
		}
		else
		{
			System.err.println("WARNING: Component " + comp + " is not supported for adding ValueChangeListener. Changes will never have effect!");
		}
	}

	// --------------------------------------------------------------------------------
	private void addValueChangeListener(JTextField txtFld)
	{
		txtFld.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				someValueChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				someValueChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				someValueChanged();
			}
		});
	}

	// --------------------------------------------------------------------------------
	private void addValueChangeListener(JSpinner spnr)
	{
		spnr.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				someValueChanged();
			}
		});
	}

	// For JCheckBox
	// --------------------------------------------------------------------------------
	private void addValueChangeListener(AbstractButton btn)
	{
		btn.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				someValueChanged();
			}
		});
	}

	// for RadioButtons
	// --------------------------------------------------------------------------------
	private void addValueChangeListener(ButtonGroup btnGroup)
	{
		Enumeration<AbstractButton> btns = btnGroup.getElements();
		while (btns.hasMoreElements())
		{
			AbstractButton btn = btns.nextElement();
			btn.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent e)
				{
					// TODO: check if this if-clause is right
					// do not react if sth gets deselected
					if (e.getStateChange() == ItemEvent.SELECTED)
					{
						someValueChanged();
					}
				}
			});
		}
	}

	// For JComboBox
	// --------------------------------------------------------------------------------
	private void addValueChangeListener(JComboBox<?> box)
	{
		box.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// do not react if sth gets deselected
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					someValueChanged();
				}
			}
		});
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// TODO: this into controller
	private void someValueChanged()
	{
		boolean[] results = settings.valuesEqualCurrentAndDefaultSettings(getValueComponents());
		if (results[0])
		{
			btnsPanel.setBtnSaveEnabled(false);
			btnsPanel.setBtnResetEnabled(false);
		}
		else
		{
			btnsPanel.setBtnSaveEnabled(true);
			btnsPanel.setBtnResetEnabled(true);
		}

		if (results[1])
		{
			btnsPanel.setBtnDefaultsEnabled(false);
		}
		else
		{
			btnsPanel.setBtnDefaultsEnabled(true);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Classes ---------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private class CategoryButtonsPanel extends JPanel implements ActionListener
	{
		private static final long	serialVersionUID	= -9204747272055698936L;

		private JButton				btnSave;
		private JButton				btnCurrents;
		private JButton				btnDefaults;

		public CategoryButtonsPanel()
		{
			btnSave = new JButton("Save settings");
			btnSave.setToolTipText("Saves the settings for this category (if this button is disabled, the current values are equal to the current settings)");
			btnSave.addActionListener(this);
			add(btnSave);

			btnCurrents = new JButton("Insert currents");
			btnCurrents.setToolTipText("Inserts the current settings for this category (if this button is disabled, the current values are equal to the current settings)");
			btnCurrents.addActionListener(this);
			add(btnCurrents);

			btnDefaults = new JButton("Insert defaults");
			btnDefaults.setToolTipText("Inserts the default values for this category (if this button is disabled, the current values are equal to the default settings)");
			btnDefaults.addActionListener(this);
			add(btnDefaults);
		}

		public void setBtnSaveEnabled(boolean enabled)
		{
			btnSave.setEnabled(enabled);
		}

		public void setBtnResetEnabled(boolean enabled)
		{
			btnCurrents.setEnabled(enabled);
		}

		public void setBtnDefaultsEnabled(boolean enabled)
		{
			btnDefaults.setEnabled(enabled);
		}

		// TODO: in Controller
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource().equals(btnSave))
			{
				setBtnResetEnabled(false);
				setBtnSaveEnabled(false);
				settings.saveValuesToSettings(getValueComponents());
			}
			else if (e.getSource().equals(btnCurrents))
			{
				settings.resetValuesAccordingToSettings(getValueComponents(), false);
			}
			else if (e.getSource().equals(btnDefaults))
			{
				int returnValue = JOptionPane.showConfirmDialog(settingsPanel,
						"Do you really want to insert the default settings?\n(No saving)",
						"Insert Defaults?",
						JOptionPane.YES_NO_OPTION);
				if (returnValue == JOptionPane.OK_OPTION)
				{
					settings.resetValuesAccordingToSettings(getValueComponents(), true);
				}

			}
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
