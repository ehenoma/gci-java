package com.merlinweber.gci.concurrent.worker;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import java.util.Objects;

/**
 * Configuration that contains variables of the work performed by workers.
 *
 * <p>The class currently has only one data-member but is still preferable over primitive obsession.
 */
public class WorkConfig {
  private int iterationCount;

  private WorkConfig(int iterationCount) {
    this.iterationCount = iterationCount;
  }

  public int iterationCount() {
    return iterationCount;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("iterations", iterationCount).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(iterationCount);
  }

  @Override
  public boolean equals(Object value) {
    if (!(value instanceof WorkConfig)) {
      return false;
    }

    return ((WorkConfig) value).iterationCount == iterationCount;
  }

  public static WorkConfig create(int iterationCount) {
    checkArgument(iterationCount > 0, "Iteration count needs to be greather than zero.");

    return new WorkConfig(iterationCount);
  }
}
