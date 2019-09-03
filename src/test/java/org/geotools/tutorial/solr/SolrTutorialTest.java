package org.geotools.tutorial.solr;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SolrTutorialTest {

  @Test
  public void testDivide() {
    assertThrows(
        ArithmeticException.class,
        () -> {
          Integer.divideUnsigned(42, 0);
        });
  }
}
