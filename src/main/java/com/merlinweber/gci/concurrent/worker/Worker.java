package com.merlinweber.gci.concurrent.worker;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;
import static java.util.stream.IntStream.range;

import com.merlinweber.gci.concurrent.util.InterruptableRunnable;
import java.util.concurrent.Executor;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Performs a verbose accumulation of numbers in a configurable range.
 *
 * <p>Workers are allocated by the Accumulation and all perform the same task. The amount and
 * work-style of workers is defined in configurations. A Worker may run in its own thread, depending
 * on the factory used, and will never start with its task until it is permitted to.
 *
 * <p>Workers are not reusable, they are created and then run. Their syncs should also be explicitly
 * created or at least restored every time an accumulation is run.
 *
 * @see WorkConfig
 * @see WorkResult
 * @see WorkerDescriptor
 * @see WorkerFactory
 */
public class Worker {

  private static final Logger LOG = Logger.getLogger("GCI");

  /* Do not write in caps, this is not a constant. */
  private static final AtomicInteger referenceCount = new AtomicInteger();

  /* Delegated to run the worker. This way thread-pooling can be configurable. */
  private Executor executor;

  private WorkConfig work;

  private WorkerDescriptor descriptor;

  /* Sync that gives permission to start the work.
   * Running the runnable will acquire the sync. */
  private InterruptableRunnable workPermitWaiter;

  /* Sync that marks the actual computation as finished.
   * Running the runnable will signal the accumulator. */
  private InterruptableRunnable workFinishSignaller;

  /* Synchronizer that is used to transfer the computations. */
  private TransferQueue<WorkResult> submissionQueue;

  private Worker(
      Executor executor,
      WorkConfig work,
      WorkerDescriptor descriptor,
      InterruptableRunnable workPermitWaiter,
      InterruptableRunnable workFinishSignaller,
      TransferQueue<WorkResult> submissionQueue) {

    this.executor = executor;
    this.work = work;
    this.descriptor = descriptor;
    this.workPermitWaiter = workPermitWaiter;
    this.workFinishSignaller = workFinishSignaller;
    this.submissionQueue = submissionQueue;
  }

  /** Runs the worker in a different thread. */
  public void start() {
    executor.execute(this::tryWork);
  }

  /* Tries to execute the work() method and returns false on interruption. */
  private boolean tryWork() {
    try {
      work();
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      return false;
    }
    return true;
  }

  /* Does the actual accumulation and transfers through the submission queue. */
  private void work() throws InterruptedException {
    workPermitWaiter.run();

    int sum = range(0, work.iterationCount()).reduce(0, this::sumVerbose);
    WorkResult result = WorkResult.create(descriptor, sum);
    workFinishSignaller.run();
    
    submissionQueue.transfer(result);
  }

  private int sumVerbose(int identity, int value) {
    if (LOG.isLoggable(Level.INFO)) {
      LOG.info(format("Worker {0}: Increment {1} by {2}.", descriptor.name(), identity, value));
    }
    return identity + value;
  }

  /* Returns a description of the Worker instance. */
  public WorkerDescriptor descriptor() {
    return this.descriptor;
  }

  /* Method used by the AbstractWorkerFactory to create a worker.
   * Will increment a reference count on every call.*/
  static Worker create(
      Executor executor,
      WorkConfig work,
      String name,
      InterruptableRunnable workPermitWaiter,
      InterruptableRunnable workFinishSignaller,
      TransferQueue<WorkResult> submissionQueue) {

    checkNotNull(executor);
    checkNotNull(work);
    checkNotNull(name);
    checkNotNull(workPermitWaiter);
    checkNotNull(workFinishSignaller);
    checkNotNull(submissionQueue);

    int ref = referenceCount.getAndIncrement();
    WorkerDescriptor descriptor = WorkerDescriptor.create(name, ref);
    return new Worker(
        executor, work, descriptor, workPermitWaiter, workFinishSignaller, submissionQueue);
  }
}
