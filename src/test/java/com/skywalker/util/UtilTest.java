package com.skywalker.util;

import org.jblas.DoubleMatrix;
import org.junit.Test;

/**
 * @author caonn@mediav.com
 * @version 16/6/24.
 */
public class UtilTest {
  @Test
  public void test_sort_column() {
    DoubleMatrix x = DoubleMatrix.rand(10, 3);
    System.out.println(x);
    DoubleMatrix xsort = x.sortColumns();
    System.out.println(xsort);
    System.out.println(x);
  }

}
