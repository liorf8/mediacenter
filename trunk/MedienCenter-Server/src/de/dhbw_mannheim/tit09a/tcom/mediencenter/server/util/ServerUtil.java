package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.rmi.ServerException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.root1.simon.SimonRegistryStatistics;
import de.root1.simon.SimonRemoteStatistics;

public class ServerUtil
{
	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static String remoteStatsToString(SimonRemoteStatistics stats)
	{
		StringBuffer sb = new StringBuffer("RemoteStatistics:\n");
		sb.append("getLastIoTime: " + stats.getLastIoTime() + "\n");
		sb.append("getLastReadTime: " + stats.getLastReadTime() + "\n");
		sb.append("getLastWriteTime: " + stats.getLastWriteTime() + "\n");
		sb.append("getReadBytes: " + stats.getReadBytes() + "\n");
		sb.append("getReadBytesThroughput: " + stats.getReadBytesThroughput() + "\n");
		sb.append("getReadMessages: " + stats.getReadMessages() + "\n");
		sb.append("getReadMessagesThroughput: " + stats.getReadMessagesThroughput() + "\n");
		sb.append("getScheduledWriteBytes: " + stats.getScheduledWriteBytes() + "\n");
		sb.append("getScheduledWriteMessages: " + stats.getScheduledWriteMessages() + "\n");
		sb.append("getWrittenBytes: " + stats.getWrittenBytes() + "\n");
		sb.append("getWrittenBytesThroughput: " + stats.getWrittenBytesThroughput() + "\n");
		sb.append("getWrittenMessages: " + stats.getWrittenMessages() + "\n");
		sb.append("getWrittenMessagesThroughput: " + stats.getWrittenMessagesThroughput());
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	public static String registryStatsToString(SimonRegistryStatistics stats)
	{
		StringBuffer sb = new StringBuffer("RegistryStatistics:\n");
		sb.append("getCumulativeManagedSessionCount: " + stats.getCumulativeManagedSessionCount() + "\n");
		sb.append("getLargestManagedSessionCount: " + stats.getLargestManagedSessionCount() + "\n");
		sb.append("getLargestReadBytesThroughput: " + stats.getLargestReadBytesThroughput() + "\n");
		sb.append("getLargestReadMessagesThroughput: " + stats.getLargestReadMessagesThroughput() + "\n");
		sb.append("getLargestWrittenBytesThroughput: " + stats.getLargestWrittenBytesThroughput() + "\n");
		sb.append("getLargestWrittenMessagesThroughput: " + stats.getLargestWrittenMessagesThroughput());
		return sb.toString();
	}

	// --------------------------------------------------------------------------------
	public static ServerException logUserThrowable(String user, Throwable t)
	{
		ServerMain.INVOKE_LOGGER.error("Invocation by user " + user + " caught exception", t);
		return new ServerException(t.toString());
	}

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private ServerUtil()
	{

	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
