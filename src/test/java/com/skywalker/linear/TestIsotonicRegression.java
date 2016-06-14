package com.skywalker.linear;

import com.skywalker.utils.Tuple;
import org.junit.Test;

import java.util.Random;

/** Test cases for class {@link IsonicRegression}.
 * @author caonn@mediav.com
 * @version 16/6/14.
 */
public class TestIsotonicRegression {
  @Test
  public void test_fit() {
    Random random = new Random();
    int size = 100;
    double [] x = new double[size];
    double [] y = new double[size];
    for( int i = 0; i < size; i++ ) {
      x[i] = random.nextDouble();
      y[i] = random.nextDouble();
    }
    List<Tuple<Double,Double>>
  }

}
