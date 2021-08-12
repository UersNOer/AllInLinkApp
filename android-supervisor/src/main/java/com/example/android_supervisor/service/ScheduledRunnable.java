package com.example.android_supervisor.service;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author wujie
 */
public interface ScheduledRunnable extends Runnable {

    void start(ScheduledExecutorService service);

    void stop(ScheduledExecutorService service);
}
