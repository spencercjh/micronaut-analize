package com.example;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author spencercjh
 */
@MicronautTest
class ApplicationTest {
  @Inject
  private ApplicationContext applicationContext;

  @Test
  void loadContext() {
    assertTrue(applicationContext.getEnvironment().getActiveNames().contains("test"));
  }
}