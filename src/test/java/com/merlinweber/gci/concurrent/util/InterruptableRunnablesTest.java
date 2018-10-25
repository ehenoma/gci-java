package com.merlinweber.gci.concurrent.util;

import static com.google.common.util.concurrent.Runnables.doNothing;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class InterruptableRunnablesTest {

  @Test
  void checkNoThrow_nothrow() {
    InterruptableRunnable action = InterruptableRunnables.nothrow(doNothing());

    try {
      action.run();
    } catch (InterruptedException interruption) {
      fail("InterruptableRunnable of type nothrow throws an InterruptedException.");
    }
  }

  @Test
  void checkThrow_immediate() {
    InterruptableRunnable action = InterruptableRunnables.immediate();

    assertNotNull(action);
    assertThrows(InterruptedException.class, action::run);
  }
}
