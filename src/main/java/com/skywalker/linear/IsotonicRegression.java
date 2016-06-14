package com.skywalker.linear;

import com.google.common.collect.Lists;
import com.skywalker.utils.Sorter;
import com.skywalker.utils.Tuple;

import java.util.List;
import java.util.Stack;

/**
 * This class implements the PAVA algorithm.
 * @author caonn@mediav.com
 * @version 16/6/12.
 */
public class IsotonicRegression {
  private Stack<Record> records = new Stack<Record>();

  /**
   * This function will fit the data points according to pava algorithm.
   * @param tuples the input points, tuple.first is index, tuple.second is value
   */
  public  void fit (List<Tuple<Double, Double>> tuples) {
    Sorter.sortTupleList(tuples);
    for( Tuple<Double, Double> t : tuples) {
      Record record = new Record( t.first(), t.first(), t.second(), 1);
      merge(record);
    }
  }

  /**
   * Merge record to record list.
   * @param record record to be merged into records.
   */
  private void merge(Record record) {
    if( records.size() == 0 ) {
      records.add(record);
      return;
    }
    Record lastRecord = records.peek();
    if( lastRecord.value < record.value ) {
      records.add(record);
    } else {
      records.pop();
      int newNumber = record.number + lastRecord.number;
      Double newValue = ( record.number * record.value + lastRecord.number * lastRecord.value ) / newNumber;
      Record newRecord = new Record(lastRecord.lowIndex, record.highIndex, newValue, newNumber);
      merge(newRecord);
    }
  }

  public void printRecords() {
    for (Record record : records) {
      System.out.println(record.toString());
    }
  }

  public List<Double> predict(List<Double> indices) {
    List<Double> result = Lists.newArrayList();
    for( Double index : indices) {
      result.add(predict(index));
    }
    return result;
  }

  public double predict( double index ) {
    Record first = records.firstElement();
    if( index <= first.lowIndex ) return first.value;

    for(Record r : records) {
      if( index > r.lowIndex && index <= r.highIndex ) {
        return r.value;
      }
    }

    Record last = records.lastElement();
    return last.value;
  }
}

class Record {
  public final double lowIndex;
  public final double highIndex;
  public final double value;
  public final int number;
  public Record(double lowIndex, double highIndex, double value, int number) {
    this.lowIndex = lowIndex;
    this.highIndex = highIndex;
    this.value = value;
    this.number = number;
  }
  public String toString() {
    return String.valueOf(lowIndex) + " " + String.valueOf(highIndex) + " " + String.valueOf(value) + " " + String.valueOf(number);
  }
}
