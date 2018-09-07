package edu.wpi.first.desktop.util;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Utility class for various JavaFX functions.
 */
public final class FxUtils extends UtilityClass {

  /**
   * Runs a task on the JavaFX application thread as soon as possible. If this is called from the
   * application thread, the task will be run <i>immediately</i>. Otherwise, it will be run at
   * some later point.
   *
   * @param task the task to run
   */
  public static void runOnFxThread(Runnable task) {
    Objects.requireNonNull(task, "Null task");
    runOnFxThread(() -> {
      task.run();
      return (Void) null;
    });
  }

  /**
   * Runs a task on the JavaFX application thread as soon as possible. If this is called from the
   * application thread, the task will be run <i>immediately</i>. Otherwise, it will be run at
   * some later point.
   *
   * @param <T>  the type of objects returned by the task
   * @param task the task to run
   *
   * @return a completable future that will have a result of the output of the task once it has completed
   */
  public static <T> CompletableFuture<T> runOnFxThread(Supplier<T> task) {
    Objects.requireNonNull(task, "Null task");
    if (Platform.isFxApplicationThread()) {
      return CompletableFuture.completedFuture(task.get());
    } else {
      CompletableFuture<T> future = new CompletableFuture<>();
      Platform.runLater(() -> future.complete(task.get()));
      return future;
    }
  }

  /**
   * Runs a task on the FX thread and blocks until the task completes or throws an error.
   *
   * @param task the task to run
   *
   * @throws InterruptedException if the calling thread was interrupted while waiting for the task to complete
   */
  public static void runAndWait(Runnable task) throws InterruptedException {
    // Code taken verbatim from the now-inaccessible method
    // com.sun.javafx.application.PlatformImpl.runAndWait
    Objects.requireNonNull(task, "Null task");
    if (Platform.isFxApplicationThread()) {
      task.run();
    } else {
      final CountDownLatch doneLatch = new CountDownLatch(1);
      Platform.runLater(() -> {
        try {
          task.run();
        } finally {
          doneLatch.countDown();
        }
      });

      doneLatch.await();
    }
  }

  /**
   * Schedules a batch of tasks to run later on the application thread.
   *
   * @param tasks the tasks to run on the application thread
   */
  public static void runLater(Iterable<Runnable> tasks) {
    Platform.runLater(batchRunnables(tasks));
  }

  /**
   * Schedules a batch of tasks to run on the application thread.
   *
   * @param tasks the tasks to run on the application thread
   */
  public static void runOnFxThread(Iterable<Runnable> tasks) {
    runOnFxThread(batchRunnables(tasks));
  }

  /**
   * Schedules a batch of tasks to run later on the application thread and blocks until all tasks have completed.
   *
   * @param tasks the tasks to run on the application thread
   *
   * @throws InterruptedException if the calling thread was interrupted while waiting for the tasks to complete
   */
  public static void runAndWait(Iterable<Runnable> tasks) throws InterruptedException {
    runAndWait(batchRunnables(tasks));
  }

  private static Runnable batchRunnables(Iterable<Runnable> tasks) {
    return () -> {
      for (Runnable task : tasks) {
        // Skip nulls instead of throwing a NPE later
        if (task == null) {
          continue;
        }
        try {
          task.run();
        } catch (RuntimeException e) {
          Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
        }
      }
    };
  }

  /**
   * Fires a close request on a window. This is useful to call on the main application window to allow shutdown
   * listeners to run, since they are not run when {@code System.exit()} or {@code Platform.exit()} are called.
   *
   * @param window the window to request to close
   */
  public static void requestClose(Window window) {
    window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
  }
}
