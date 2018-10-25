package com.merlinweber.gci.concurrent.worker;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import java.util.Objects;

/**
 * Immutable class that describes a worker without carrying any of its state.
 *
 * @see Worker
 */
public final class WorkerDescriptor {

  private String name;
  private long referenceNumber;

  private WorkerDescriptor(String name, long referenceNumber) {
    this.name = name;
    this.referenceNumber = referenceNumber;
  }

  public String name() {
    return name;
  }

  public long referenceNumber() {
    return this.referenceNumber;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("ref", referenceNumber)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, referenceNumber);
  }

  @Override
  public boolean equals(Object value) {
    if (!(value instanceof WorkerDescriptor)) {
      return false;
    }

    WorkerDescriptor descriptor = (WorkerDescriptor) value;
    return descriptor.name.equals(this.name) && descriptor.referenceNumber == this.referenceNumber;
  }

  /**
   * Static factory method of the WorkerDescriptor.
   *
   * @param name Name of the worker.
   * @param referenceNumber Integer that is greater than zero.
   */
  public static WorkerDescriptor create(String name, long referenceNumber) {
    checkNotNull(name);
    checkArgument(referenceNumber >= 0);

    return new WorkerDescriptor(name, referenceNumber);
  }
}
