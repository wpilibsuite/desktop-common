package edu.wpi.first.desktop.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Utilities dealing with threading.
 */
public final class ThreadUtils extends UtilityClass {

  /**
   * A ThreadFactory that creates a daemon thread.
   */
  public static final ThreadFactory DAEMON_THREAD_FACTORY = ThreadUtils::makeDaemonThread;

  /**
   * A single-threaded ScheduledExecutorService that uses a daemon thread instead of one the doesn't respect shutdown.
   */
  public static ScheduledExecutorService newDaemonScheduledExecutorService() {
    return Executors.newSingleThreadScheduledExecutor(DAEMON_THREAD_FACTORY);
  }

  /**
   * Creates a daemon thread to run the given runnable.
   */
  public static Thread makeDaemonThread(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.setDaemon(true);
    return thread;
  }

}
