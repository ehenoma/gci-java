package com.merlinweber.gci.concurrent.worker;

import static com.merlinweber.gci.concurrent.worker.WorkerFactories.explicitlyExecuted;

import java.util.concurrent.Executors;
import java.util.function.IntFunction;

/**
 * Common policies that determine how a worker can be started.
 *
 * @see WorkerFactories
 */
public enum WorkerExecutionPolicy {
  /** Exclusively allocates a Thread for every worker. */
  EXCLUSIVE_THREAD(num -> WorkerFactories.exclusiveThreadPerWorker()),

  /* Allocates a pool of threads which executes the workers.*/
  THREAD_POOL(num -> explicitlyExecuted(Executors.newFixedThreadPool(num)));

  private IntFunction<ConfigurableWorkerFactory> workerFactoryCreator;

  WorkerExecutionPolicy(IntFunction<ConfigurableWorkerFactory> workerFactoryCreator) {
    this.workerFactoryCreator = workerFactoryCreator;
  }

  public ConfigurableWorkerFactory createFactory(int workerCount) {
    return workerFactoryCreator.apply(workerCount);
  }
}
