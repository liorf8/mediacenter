package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class TaskPanel extends JPanel
{
	private static final long	serialVersionUID	= 1L;

	private JButton				btnCancel;
	private JProgressBar		progressBar;

	public TaskPanel(ActionListener cancelActionListener)
	{
		GroupLayout groupLayout = new GroupLayout(this);
		this.setLayout(groupLayout);

		// ProgressBar
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);

		// Cancel button
		btnCancel = new JButton("x");
		btnCancel.addActionListener(cancelActionListener);

		// Layout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addComponent(progressBar).addComponent(btnCancel));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(progressBar)
				.addComponent(btnCancel)));
	}

	public void setProgress(int progress)
	{
		progressBar.setValue(progress);
	}

	public void setStatus(String status)
	{
		progressBar.setString(status);
	}

	public void setCancelText(String text)
	{
		btnCancel.setText(text);
	}

	public void setTaskFailed(String cause)
	{
		btnCancel.setEnabled(false);
		progressBar.setString(cause);
		progressBar.setValue(progressBar.getMaximum());
		progressBar.setForeground(Color.RED);
	}
}
