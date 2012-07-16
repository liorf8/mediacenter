package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

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
		btnCancel = new JButton(MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Cancel.png"));
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
	
	public void setCancelEnabled(boolean enabled)
	{
		btnCancel.setEnabled(enabled);
	}

	public void setTaskFinished(boolean successfull, String message)
	{
		btnCancel.setEnabled(false);
		btnCancel.setIcon(null);
		progressBar.setString(message);
		progressBar.setValue(progressBar.getMaximum());
		if (successfull)
			progressBar.setForeground(MediaUtil.COLOR_DARK_GREEN);
		else
			progressBar.setForeground(Color.RED);
	}
}
