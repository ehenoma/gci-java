package com.merlinweber.gci.concurrent.worker;

import static com.merlinweber.gci.concurrent.worker.WorkerFactories.explicitlyExecuted;

import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Common policies that determine how a worker can be started.
 *
 * @see WorkerFactories
 */
public enum WorkerExecutionPolicy {
  /** Exclusively allocates a Thread for every worker. */
  EXCLUSIVE_THREAD(WorkerFactories::exclusiveThreadPerWorker),

  /* Allocates one thread that is executing all workers sequentially. */
  SINGLE_THREAD(WorkerFactories::singleThreadExecuted),

  /* Allocates a pool of threads which executes the workers.*/
  THREAD_POOL(() -> explicitlyExecuted(Executors.newCachedThreadPool()));

  private Supplier<ConfigurableWorkerFactory> workerFactoryCreator;

  WorkerExecutionPolicy(Supplier<ConfigurableWorkerFactory> workerFactoryCreator) {
    this.workerFactoryCreator = workerFactoryCreator;
  }

  public ConfigurableWorkerFactory createFactory() {
    return workerFactoryCreator.get();
  }
}
