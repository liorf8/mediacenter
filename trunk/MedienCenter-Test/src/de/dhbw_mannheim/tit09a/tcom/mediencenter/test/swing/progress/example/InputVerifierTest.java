package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.swing.progress.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// This program demonstrates the use of the Swing InputVerifier class.
// It creates two text fields; the first of the text fields expects the
// string "pass" as input, and will allow focus to advance out of it
// only after that string is typed in by the user.

public class InputVerifierTest extends JFrame
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public InputVerifierTest()
	{
		JTextField tf1 = new JTextField("Type \"pass\" here");
		getContentPane().add(tf1, BorderLayout.NORTH);
		tf1.setInputVerifier(new PassVerifier());

		JTextField tf2 = new JTextField("TextField2");
		getContentPane().add(tf2, BorderLayout.SOUTH);

		WindowListener l = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		};
		addWindowListener(l);
	}

	class PassVerifier extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			JTextField tf = (JTextField) input;
			return "pass".equals(tf.getText());
		}
	}

	public static void main(String[] args)
	{
		Frame f = new InputVerifierTest();
		f.pack();
		f.setVisible(true);
	}
}
