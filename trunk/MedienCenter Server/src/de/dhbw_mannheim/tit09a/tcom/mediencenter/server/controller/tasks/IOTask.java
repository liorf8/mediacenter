package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface IOTask<T> extends Callable<T>
{
    @Override
    public abstract T call() throws IOException;
}
