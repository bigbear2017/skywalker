package com.skywalker.linear;

import com.google.common.collect.Lists;
import com.skywalker.utils.Sorter;
import com.skywalker.utils.Tuple;

import java.util.List;

/**
 * This class implements the PAVA algorithm.
 * @author caonn@mediav.com
 * @version 16/6/12.
 */
public class IsonicRegression {
  private List<Record> records = Lists.newArrayList();

  /**
   * This function will fit the data points according to pava algorithm.
   * @param tuples the input points, tuple.first is index, tuple.second is value
   * @param <T1> the type of index, could be int or double
   * @param <T2> the type of value, could be int or double
   */
  public <T1 extends Comparable< ? super T1>, T2 > void fit(List<Tuple<T1 ,T2>> tuples) {
    Sorter.sortTupleList(tuples);
    for( Tuple t : tuples) {
      Record record = new Record( (Comparable)t.first(), (Comparable)t.first(), t.second());
    }
  }

  public <T1, T2> List<T2> predict(List<Tuple<T1, T2>> tuples) {

    return Lists.newArrayList();
  }
}

class Record<T1 extends Comparable<? super T1>, T2> {
  public final T1 lowIndex;
  public final T1 highIndex;
  public final T2 value;
  public Record(T1 lowIndex, T1 highIndex, T2 value) {
    this.lowIndex = lowIndex;
    this.highIndex = highIndex;
    this.value = value;
  }
}
