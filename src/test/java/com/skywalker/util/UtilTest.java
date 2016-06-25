package com.skywalker.util;

import org.jblas.DoubleMatrix;
import org.junit.Test;

import java.util.Arrays;

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

  @Test
  public void test_sort_indices() {
    DoubleMatrix x = DoubleMatrix.rand(10, 3);
    System.out.println(x);
    int [] [] indices = x.columnSortingPermutations();
    System.out.println(x.getColumn(0));
    System.out.println(x.getColumn(0).get(indices[0]));
  }

}
