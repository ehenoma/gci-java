package com.merlinweber.gci.concurrent;

import static java.text.MessageFormat.format;

import com.merlinweber.gci.concurrent.worker.WorkConfig;
import com.merlinweber.gci.concurrent.worker.WorkerExecutionPolicy;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AccumulationSolution {

  /* The fallback configuration which has the values of the original task. */
  private static final AccumulationConfig DEFAULT_CONFIG =
      AccumulationConfig.create(3, WorkConfig.create(10));

  private AccumulationSolution() {}

  public static void main(String[] arguments) {
    Logger logger = configureLogger();

    Accumulator accumulation = Accumulator.create(parseConfig(arguments, logger));
    interpretSuccessState(logger, accumulation.tryRun());
  }

  private static void interpretSuccessState(Logger log, boolean success) {
    if (!success) {
      log.severe("The accumulation solution has failed.");
    }

    log.finest("Successfully finished running the accumulation solution.");
  }

  private static AccumulationConfig parseConfig(String[] arguments, Logger logger) {
    try {
      return tryParseConfig(arguments, logger);
    } catch (NumberFormatException parsingFailure) {
      logger.warning("Unexpected argument: Expected integer.");
      logger.warning("Using default configuration...");
      return DEFAULT_CONFIG;
    }
  }

  private static AccumulationConfig tryParseConfig(String[] arguments, Logger logger) {

    if (arguments.length < 2) {
      return DEFAULT_CONFIG;
    }

    int workers = Integer.parseInt(arguments[0]);
    int iteration = Integer.parseInt(arguments[1]);
    WorkerExecutionPolicy policy = WorkerExecutionPolicy.EXCLUSIVE_THREAD;

    if (arguments.length >= 3) {
      try {
        policy = WorkerExecutionPolicy.valueOf(arguments[2]);
      } catch (IllegalArgumentException invalidName) {
        String validPolicies = Arrays.toString(WorkerExecutionPolicy.values());
        logger.warning(format("Could not find policy: {0}", arguments[2]));
        logger.warning(format("Possible policies: {0}", validPolicies));
        // Simply don't change the policy
      }
    }

    return AccumulationConfig.create(workers, WorkConfig.create(iteration), policy);
  }

  private static Logger configureLogger() {
    System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT (%4$-7s): %5$s %n");

    Logger logger = Logger.getLogger("GCI");
    logger.setLevel(Level.ALL);
    return logger;
  }
}
