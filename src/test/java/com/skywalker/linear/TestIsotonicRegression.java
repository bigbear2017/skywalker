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

  @Test
  public void test_fit_data_sort1() {
    List<Tuple<Double, Double>> tuples = Lists.newArrayList();
    for(int i = 0; i < 100; i++ ) {
      tuples.add(new Tuple<Double, Double>(new Double(i), new Double(i)));
    }
    IsotonicRegression isotonic = new IsotonicRegression();
    isotonic.fit(tuples);
    isotonic.printRecords();
  }

  @Test
  public void test_fit_data_sort2() {
    List<Tuple<Double, Double>> tuples = Lists.newArrayList();
    for(int i = 100; i > 0; i-- ) {
      tuples.add(new Tuple<Double, Double>(new Double(i), new Double(i)));
    }
    IsotonicRegression isotonic = new IsotonicRegression();
    isotonic.fit(tuples);
    isotonic.printRecords();
  }

  @Test
  public void test_fit_data_create() {
    List<Tuple<Double, Double>> tuples = Lists.newArrayList();
    tuples.add(new Tuple<Double, Double>(1d, 1d));
    tuples.add(new Tuple<Double, Double>(2d, 1.5d));
    tuples.add(new Tuple<Double, Double>(3d, 2.5d));
    tuples.add(new Tuple<Double, Double>(4d, 2.2d));
    tuples.add(new Tuple<Double, Double>(5d, 2.4d));
    tuples.add(new Tuple<Double, Double>(6d, 2.3d));
    tuples.add(new Tuple<Double, Double>(7d, 2.5d));
    tuples.add(new Tuple<Double, Double>(8d, 2.6d));
    tuples.add(new Tuple<Double, Double>(9d, 2.55d));
    tuples.add(new Tuple<Double, Double>(10d, 2.7d));
    tuples.add(new Tuple<Double, Double>(11d, 2.75d));

    IsotonicRegression isotonic = new IsotonicRegression();
    isotonic.fit(tuples);
    isotonic.printRecords();
  }

}
