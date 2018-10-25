package com.merlinweber.gci.concurrent.worker;

/**
 * Creates instances of the Worker class with an explicit name.
 * <p>
 * This SAM interface grants the highest possible abstraction of creating
 * Worker classes. The more concrete {@code ConfigurableWorkerFactory} should
 * be used to created configurable workers in production. It's still good to
 * fallback onto this interface because of testability and the DIP (SOLID).
 *
 * @see Worker
 * @see WorkerFactories
 */
@FunctionalInterface
interface WorkerFactory {

  Worker getInstance(String name);

}
