package com.merlinweber.gci.concurrent;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.merlinweber.gci.concurrent.worker.ConfigurableWorkerFactory;
import com.merlinweber.gci.concurrent.worker.WorkConfig;
import com.merlinweber.gci.concurrent.worker.WorkerExecutionPolicy;

public final class AccumulationConfig {

  private int workerCount;

  private WorkerExecutionPolicy executionPolicy;

  private WorkConfig workConfig;

  private AccumulationConfig(
      int workerCount, WorkConfig workConfig, WorkerExecutionPolicy executionPolicy) {

    this.workerCount = workerCount;
    this.executionPolicy = executionPolicy;
    this.workConfig = workConfig;
  }

  public ConfigurableWorkerFactory createWorkerFactory() {
    return executionPolicy.createFactory();
  }

  public WorkConfig workConfig() {
    return workConfig;
  }

  public int workerCount() {
    return workerCount;
  }

  public static AccumulationConfig create(int workerCount, WorkConfig workConfig) {
    return create(workerCount, workConfig, WorkerExecutionPolicy.EXCLUSIVE_THREAD);
  }

  public static AccumulationConfig create(
      int workerCount, WorkConfig workConfig, WorkerExecutionPolicy executionPolicy) {
    checkNotNull(workConfig);
    checkNotNull(executionPolicy);
    checkArgument(workerCount > 0, "Count must be greater than zero.");

    return new AccumulationConfig(workerCount, workConfig, executionPolicy);
  }
}
