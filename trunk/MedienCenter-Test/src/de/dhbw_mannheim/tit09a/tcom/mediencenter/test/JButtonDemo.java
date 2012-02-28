package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JButtonDemo
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b = new JButton("Ende");
		frame.add(b);
		ActionListener al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		};
		b.addActionListener(al);
		frame.pack();
		frame.setVisible(true);
	}
}