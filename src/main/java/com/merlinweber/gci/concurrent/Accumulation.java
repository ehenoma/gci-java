package com.merlinweber.gci.concurrent;

import com.merlinweber.gci.concurrent.worker.AbstractWorkerFactory;
import com.merlinweber.gci.concurrent.worker.WorkResult;
import com.merlinweber.gci.concurrent.worker.Worker;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/** @see AccumulationConfig */
public final class Accumulation {

  private static final Logger LOG = Logger.getLogger("GCI");

  /* Character used to name workers based on their ref-number. */
  private static final char WORKER_NAME_BASE_CHAR = 'A';

  private AccumulationConfig config;

  private AbstractWorkerFactory workerFactory;

  /* Synchronizer which is used to transfer worker results.*/
  private TransferQueue<WorkResult> submissions;

  private Accumulation(AccumulationConfig config) {
    this.config = config;
    this.workerFactory = config.createWorkerFactory();
    this.submissions = new LinkedTransferQueue<>();
  }

  /**
   * Tries to run the run() method and returns false if it fails.
   *
   * <p>In the event of an interrupted exception, a warning is logged, the current thread is
   * interrupted and the method returns false. If everything works out smoothly, no additional
   * logging is done and true is returned.
   *
   * @return Whether the accumulation has been run without interruptions.
   */
  public boolean tryRun() {
    try {
      run();
    } catch (InterruptedException interruption) {
      LOG.warning("Could not finish accumulation because of an interruption.");
      Thread.currentThread().interrupt();
      return false;
    }

    return true;
  }

  /**
   * Creates workers and logs their result of the accumulation.
   *
   * <p>The method blocks until every worker has finished his work and transferred its result
   * through the submissions queue.
   *
   * @throws InterruptedException Thrown when being interrupted while using on of the synchronizers.
   */
  public void run() throws InterruptedException {
    CountDownLatch workPermitSync = new CountDownLatch(1);
    CountDownLatch workFinishSync = new CountDownLatch(config.workerCount());
    // Set the synchronizers in the factory, so that all newly created
    // workers will get thm injected into their constructor.
    workerFactory.setWorkPermitWaiter(workPermitSync::await);
    workerFactory.setWorkFinishSignaller(workFinishSync::countDown);
    // Creates all te workers. They won't start their work
    // until the 'workPermitSync' is counted down.
    createWorkers();
    // Allowing the worker threads to start their work by counting
    // down the latch which every of them are awaiting at this point.
    workPermitSync.countDown();
    // Waits for all worker threads to finish their work
    // in order for the results to be logged closely together.
    workFinishSync.await();
    // Collects all the results from the transfer queue.
    gatherWorkerResults().forEach(WorkResult::log);
  }

  /* Uses the submissions queue to transfer all the results to the current thread. */
  private Stream<WorkResult> gatherWorkerResults() throws InterruptedException {
    logVerbose("Gathering Results of the workers...");

    Collection<WorkResult> results = new ArrayList<>(config.workerCount());
    for (int workerIndex = 0; workerIndex < config.workerCount(); workerIndex++) {
      WorkResult submission = submissions.take();
      results.add(submission);
    }

    return results.stream();
  }

  /*
   * Create the configured amount of workers and starts them.
   * That they're started, doesn't mean, that they will start their work.
   * Internally the workers will wait until an injected synchronizer allows them to.
   */
  private void createWorkers() {
    logVerbose("The Workers are now created...");

    for (int workerIndex = 0; workerIndex < config.workerCount(); workerIndex++) {
      String name = String.valueOf((char) (WORKER_NAME_BASE_CHAR + workerIndex));
      Worker slave = workerFactory.getInstance(name);
      slave.start();

      logVerbose("Worker {0} has been created.");
    }
  }

  /* Logs the message if the logger allows verbose logging. */
  private void logVerbose(String message, Object... args) {
    if (!LOG.isLoggable(Level.INFO)) {
      return;
    }

    LOG.info(MessageFormat.format(message, args));
  }

  public static Accumulation create(AccumulationConfig config) {
    return null;
  }
}
