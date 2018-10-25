package com.merlinweber.gci.concurrent.worker;

/**
 * Common policies that determine how a worker can be started.
 *
 * @see WorkerFactories
 */
public enum WorkerExecutionPolicy {
  EXCLUSIVE_THREAD,

  SYNCHRONOUS,

  SINGLE_THREAD,

  THREAD_POOL
}
