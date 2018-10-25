package com.merlinweber.gci.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class AccumulationSolution {

  private AccumulationSolution() {}

  public static void main(String[] arguments) {
    AccumulationConfigParser cliParser = AccumulationConfigParser.create(arguments);
    Accumulation accumulation = Accumulation.create(cliParser.getInstance());

    interpretSuccessState(configureLogger(), accumulation.tryRun());
  }

  private static void interpretSuccessState(Logger log, boolean success) {
    if (!success) {
      log.severe("The accumulation solution has failed.");
    }

    log.finest("Successfully finished running the accumulation solution.");
  }

  private static Logger configureLogger() {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format", "%1$tF %1$tT (%4$-7s): %5$s %n");

    Logger logger = Logger.getLogger("GCI");
    logger.setLevel(Level.ALL);
    return logger;
  }
}
