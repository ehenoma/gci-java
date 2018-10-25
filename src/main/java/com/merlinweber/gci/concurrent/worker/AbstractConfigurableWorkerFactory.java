package com.merlinweber.gci.concurrent.worker;

import com.google.common.base.MoreObjects;
import com.merlinweber.gci.concurrent.util.InterruptableRunnable;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TransferQueue;

/*
 * Internal, Abstract implementation of the ConfigurableWorkerFactory,
 * that implements the configurables by using data-members.
 */
abstract class AbstractConfigurableWorkerFactory implements ConfigurableWorkerFactory {

  private InterruptableRunnable workPermitWaiter;

  private InterruptableRunnable workFinishSignaller;

  private WorkConfig work;

  private TransferQueue<WorkResult> submissionQueue;

  // Do nothing but implement for derived classes.
  AbstractConfigurableWorkerFactory() {}

  @Override
  public abstract Worker getInstance(String name);

  protected final Worker getInstance(String name, Executor executor) {
    return Worker.create(
        executor, work, name, workPermitWaiter, workFinishSignaller, submissionQueue);
  }

  public void setWork(WorkConfig work) {
    this.work = work;
  }

  public void setSubmissionQueue(TransferQueue<WorkResult> submissionQueue) {
    this.submissionQueue = submissionQueue;
  }

  public void setWorkFinishSignaller(InterruptableRunnable workFinishSignaller) {
    this.workFinishSignaller = workFinishSignaller;
  }

  public void setWorkPermitWaiter(InterruptableRunnable workPermitWaiter) {
    this.workPermitWaiter = workPermitWaiter;
  }

  @Override
  public int hashCode() {
    return Objects.hash(work);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("work", work).toString();
  }

  @Override
  public boolean equals(Object value) {
    if (!(value instanceof AbstractConfigurableWorkerFactory)) {
      return false;
    }

    return ((AbstractConfigurableWorkerFactory) value).work.equals(work);
  }
}
