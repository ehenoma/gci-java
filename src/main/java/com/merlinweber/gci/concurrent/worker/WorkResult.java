package com.merlinweber.gci.concurrent.worker;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;

import com.google.common.base.MoreObjects;
import java.util.logging.Logger;

/**
 * Immutable result of a Worker, which is loggable.
 *
 * @see Worker
 * @see WorkerDescriptor
 */
public class WorkResult {
  private static final Logger LOG = Logger.getLogger("GCI");

  private WorkerDescriptor worker;
  private int computedSum;

  private WorkResult(WorkerDescriptor worker, int sum) {
    this.worker = worker;
    this.computedSum = sum;
  }

  /** Logs the workers name and its computed result to the console (logging handlers). */
  public void log() {
    LOG.info(format("Result of {0}: {1}", worker, computedSum));
  }

  public WorkerDescriptor worker() {
    return this.worker;
  }

  public int computedSum() {
    return this.computedSum;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("worker", worker)
        .add("sum", computedSum)
        .toString();
  }

  /**
   * Static factory method that creates a WorkResult.
   *
   * @param descriptor Descriptor of the worker.
   * @param sum Computed sum / The actual results value.
   */
  public static WorkResult create(WorkerDescriptor descriptor, int sum) {
    checkNotNull(descriptor);

    return new WorkResult(descriptor, sum);
  }
}
