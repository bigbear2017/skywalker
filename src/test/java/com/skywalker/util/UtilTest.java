package com.skywalker.util;

import com.google.common.collect.Lists;
import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

  @Test
  public void test_for() {
    int windowSize = 3;
    Pattern SPACES = Pattern.compile("\\s+");
    String s = "I am a chinese, I love china";
    String [] parts = SPACES.split(s);
    int tempSize = parts.length < windowSize ? parts.length : windowSize;
    for(int i = 0; i <= parts.length - tempSize; i++) {
      for( int j = 0; j < tempSize - 1; j ++ ) {
        for( int k = j + 1; k < tempSize; k++) {
          String w1 = parts[i+j];
          String w2 = parts[i+k];
          if( !w1.equals(w2) ) {
            System.out.println( w1 + " " + w2 );
            System.out.println( w2 + " " + w1);
          }
        }
      }
    }
  }

}
