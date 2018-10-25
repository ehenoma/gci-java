package com.merlinweber.gci.concurrent.worker;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Util class that provides some factory methods for creating WorkerFactories.
 *
 * @see WorkerFactory
 * @see ConfigurableWorkerFactory
 */
public final class WorkerFactories {

  // Prevent manual creation.
  private WorkerFactories() {}

  /**
   * Returns a factory that creates workers which all
   * run in one explicitly created thread.
   */
  public static ConfigurableWorkerFactory singleThreadExecuted() {
    return explicitlyExecuted(Executors.newSingleThreadExecutor());
  }

  /**
   * Returns a factory that creates workers which all
   * work in their own explicitly created thread.
   */
  public static ConfigurableWorkerFactory exclusiveThreadPerWorker() {
    return explicitlyExecuted(task -> {
      Thread delegate = new Thread(task);
      delegate.start();
    });
  }

  /** Returns an factory that creates workers which work in the current thread. */
  public static ConfigurableWorkerFactory synchronous() {
    return explicitlyExecuted(MoreExecutors.directExecutor());
  }

  /**
   * Returns a factory that uses the given executor to start its workers.
   * @param executor Explicit executor that starts workers.
   */
  public static ConfigurableWorkerFactory explicitlyExecuted(Executor executor) {
    return new ExplicitlyExecutedFactory(executor);
  }

  /* Delegates an executor to create a factory. */
  private static class ExplicitlyExecutedFactory extends AbstractConfigurableWorkerFactory {
    private Executor executor;

    private ExplicitlyExecutedFactory(Executor executor) {
      super();
      this.executor = executor;
    }

    @Override
    public Worker getInstance(String name) {
      return getInstance(name, executor);
    }
  }
}
