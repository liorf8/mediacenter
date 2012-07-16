package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import uk.co.caprica.vlcj.logger.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.CancelSupport;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.DefaultClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;

public class UploadFileAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 8818453510509043691L;

	public UploadFileAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setLargeIcon(MediaUtil.PATH_IMGS_16x16 + "Upload.png");
		setActionCommand("Upload");
		setShortDescription("Upload a file to the server");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new UploadWorker(mainFrame, action, e);
	}

	private class UploadWorker extends AbstractTaskPanelSwingWorker implements PropertyChangeListener, CancelSupport
	{
		private long	fileSize;
		private long	start;
		private long	totalReadBytes;
		private Path	src;
		private String	targetUri;
		private int		readBytesUpdateCount	= 0;

		public UploadWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			src = Paths.get(mainFrame.getManageTab().getChosenFileToUpload());
			targetUri = mainFrame.getManageTab().getChosenDestinationFolder();
			if (src == null || targetUri == null)
				throw new IllegalArgumentException("Please choose a file to upload and a destination folder!");

			publish("Waiting for user input");
			int answer = JOptionPane.showConfirmDialog(mainFrame,
					"Upload " + src.getFileName() + " to " + targetUri + "?",
					"Upload?",
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION)
			{
				setProgress(10);
				Session session = MainController.getInstance().getServerConnection().getSession();
				setProgress(30);
				MainController.getInstance().getClientCallback().sendFile(session, src, targetUri, this, this);
			}
			else
			{
				this.cancel(false);
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			if (evt.getPropertyName().equals(DefaultClientCallback.PROPERTY_FILESIZE))
			{
				start = System.currentTimeMillis();
				fileSize = (Long) evt.getNewValue();
			}
			else if (evt.getPropertyName().equals(DefaultClientCallback.PROPERTY_READ_BYTES))
			{
				totalReadBytes += (int) evt.getNewValue();

				readBytesUpdateCount++;
				if (readBytesUpdateCount % 10 == 0)
				{
					int procentage = (int) ProgressUtil.percentage(totalReadBytes, fileSize);
					long speedPerSeconds = (long) ProgressUtil.speed(totalReadBytes, System.currentTimeMillis() - start);
					long remainingSecs = ProgressUtil.remainingSecs(fileSize - totalReadBytes, speedPerSeconds);
					setProgress(procentage);
					publish(src.getFileName() + ": " + ByteValue.bytesToString(totalReadBytes) + "/" + ByteValue.bytesToString(fileSize) + " @ "
							+ ByteValue.bytesToString((long) speedPerSeconds) + "/s ETA:" + TimeValue.formatMillis(remainingSecs * 1000));
				}
			}
		}

		@Override
		public void onFailed(Exception e)
		{
			try
			{
				MainController.LOGGER.warn("Task {} failed (Exception:{}). Deleting target", actionEvent.getActionCommand(), e);
				mainController.getServerConnection().getSession().deleteFile(targetUri + "/" + src.getFileName(), false);
			}
			catch (FileSystemException | ServerException e1)
			{
				MainController.LOGGER.warn("Deleting failed. Exception: {}" + e1);
				e1.printStackTrace();
			}
		}
	}
}
