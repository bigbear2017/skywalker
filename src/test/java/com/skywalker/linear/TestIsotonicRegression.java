package com.skywalker.linear;

import com.google.common.collect.Lists;
import com.skywalker.utils.Tuple;
import org.junit.Test;

import java.util.List;
import java.util.Random;

/** Test cases for class {@link IsotonicRegression}.
 * @author caonn@mediav.com
 * @version 16/6/14.
 */
public class TestIsotonicRegression {
  @Test
  public void test_fit() {
    List<Tuple<Double,Double>> tuples = Lists.newArrayList();
    Random random = new Random();
    int size = 10000;
    for( int i = 0; i < size; i++ ) {
      tuples.add(new Tuple<Double, Double>(random.nextDouble(), random.nextDouble()));
    }
    IsotonicRegression isotonic = new IsotonicRegression();
    isotonic.fit(tuples);
    isotonic.printRecords();
  }

}
