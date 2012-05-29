package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScheduledEventDispatcher implements ScheduledExecutorService
{
	private ScheduledExecutorService	scheduledExecutor;

	public ScheduledEventDispatcher(int corePoolSize, ThreadFactory threadFactory)
	{
		scheduledExecutor = Executors.newScheduledThreadPool(corePoolSize, threadFactory);
	}

	@Override
	public void shutdown()
	{
		scheduledExecutor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow()
	{
		return scheduledExecutor.shutdownNow();
	}

	@Override
	public boolean isShutdown()
	{

		return scheduledExecutor.isShutdown();
	}

	@Override
	public boolean isTerminated()
	{

		return scheduledExecutor.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
	{

		return scheduledExecutor.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task)
	{

		return scheduledExecutor.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result)
	{

		return scheduledExecutor.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task)
	{

		return scheduledExecutor.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
	{

		return scheduledExecutor.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
	{

		return scheduledExecutor.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
	{

		return scheduledExecutor.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
			TimeoutException
	{

		return scheduledExecutor.invokeAny(tasks, timeout, unit);
	}

	@Override
	public void execute(Runnable command)
	{
		scheduledExecutor.execute(command);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
	{
		return scheduledExecutor.schedule(command, delay, unit);
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
	{
		return scheduledExecutor.schedule(callable, delay, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
	{
		return scheduledExecutor.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
	{
		return scheduledExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	// --------------------------------------------------------------------------------
	public ScheduledFuture<?> scheduleOnEventDispatchThread(final Runnable command, long delay, TimeUnit unit)
	{
		ScheduledFuture<?> f = scheduledExecutor.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				EventQueue.invokeLater(command);
			}
		}, delay, unit);
		return f;
	}

	// --------------------------------------------------------------------------------
	public ScheduledFuture<?> scheduleAtFixedRateOnEventDispatchThread(final Runnable command, long initialDelay, long period, TimeUnit unit)
	{
		ScheduledFuture<?> f = scheduledExecutor.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				EventQueue.invokeLater(command);
			}
		}, initialDelay, period, unit);
		return f;
	}

	// --------------------------------------------------------------------------------
	public ScheduledFuture<?> scheduleWithFixedDelayOnEventDispatchThread(final Runnable command, long initialDelay, long delay, TimeUnit unit)
	{
		ScheduledFuture<?> f = scheduledExecutor.scheduleWithFixedDelay(new Runnable()
		{
			@Override
			public void run()
			{
				EventQueue.invokeLater(command);
			}
		}, initialDelay, delay, unit);
		return f;
	}

}
