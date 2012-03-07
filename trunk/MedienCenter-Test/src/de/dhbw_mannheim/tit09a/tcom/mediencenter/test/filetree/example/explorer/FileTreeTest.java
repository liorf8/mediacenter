package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.example.explorer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class FileTreeTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		final JFrame f = new JFrame("Datei auswählen");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final FileTree chooser = new FileTree();
		chooser.setFileFilter(FileTree.ACCEPT_ALL_FILTER);
		JScrollPane scrp = new JScrollPane(chooser);
		Border empty = BorderFactory.createEmptyBorder(5, 5, 2, 5);
		scrp.setBorder(BorderFactory.createCompoundBorder(empty, scrp.getBorder()));

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(new JButton(new AbstractAction("OK")
		{
			private static final long	serialVersionUID	= -5534161705609979615L;

			public void actionPerformed(ActionEvent e)
			{
				File file = chooser.getSelectedFile();
				String s = "'" + file + "'";
				if (file == null)
					s = "nix";
				JOptionPane.showMessageDialog(f, "Es wurde " + s + " selektiert.");
				f.dispose();
			}

		}));

		f.add(scrp);
		f.add(bottom, BorderLayout.SOUTH);

		f.setSize(300, 400);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}