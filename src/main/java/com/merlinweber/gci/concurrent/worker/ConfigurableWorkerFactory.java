package com.merlinweber.gci.concurrent.worker;

import com.merlinweber.gci.concurrent.util.InterruptableRunnable;
import java.util.concurrent.TransferQueue;

/**
 * More concrete abstraction for creating Workers.
 *
 * @see WorkerFactories
 * @see WorkerFactory
 */
public interface ConfigurableWorkerFactory extends WorkerFactory {

  @Override
  Worker getInstance(String name);

  /** Sets the configuration of the work that the Worker should do. */
  void setWork(WorkConfig work);

  /** Sets the transfer-queue that the Worker uses to submit his results concurrently. */
  void setSubmissionQueue(TransferQueue<WorkResult> submissionQueue);

  /** Sets the action that the worker performs to signal that he has finished his task. */
  void setWorkFinishSignaller(InterruptableRunnable workFinishSignaller);

  /** Sets the action that will block the Worker until it is permitted to work. */
  void setWorkPermitWaiter(InterruptableRunnable workPermitWaiter);
}
