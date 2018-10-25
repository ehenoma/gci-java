package com.merlinweber.gci.concurrent.util;

/**
 * Runnable which can throw an InterruptedException during its execution.
 * <p>
 * The plain Runnable SAM does not include Exceptions, when abstracting
 * methods of synchronizers and other blocking mechanisms, special
 * interfaces need to be used.
 */
@FunctionalInterface
public interface InterruptableRunnable {

  /**
   * Runs the interruptable code.
   *
   * @throws InterruptedException Thrown whenever the thread is being
   * interrupted during the execution of the method.
   */
  void run() throws InterruptedException;
}
