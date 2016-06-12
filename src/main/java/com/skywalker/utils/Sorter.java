package com.skywalker.utils;


import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author caonn
 * @version 16-3-10.
 */

public class Sorter {

  public static <X extends Comparable< ? super X>, Y> Tuple<X, Y> [] sortPairFirst(X [] xArr, Y [] yArr) {
    int length = xArr.length;
    Tuple<X, Y> [] tuples = new Tuple[length];
    for( int i = 0; i < length; i++ ) {
      tuples[i] = new Tuple<X, Y>( xArr[i], yArr[i]);
    }

    Comparator<Tuple<X, Y>> comparator = new Comparator<Tuple<X, Y>>() {
      public int compare(Tuple<X, Y> t1, Tuple<X, Y> t2) {
        return t1.first().compareTo(t2.first());
      }
    };
    Arrays.sort(tuples, comparator);
    return tuples;
  }

  public static <X extends Comparable< ? super X>, Y extends Comparable< ? super Y>>
  Tuple<X, Y> [] sortPairArray(X [] xArr, Y [] yArr ) {
    assert( xArr.length == yArr.length );

    int length = xArr.length;
    Tuple<X, Y>[] tuples = new Tuple[length];
    for( int i = 0; i < length; i++ ) {
      tuples[i] = new Tuple<X,Y> (xArr[i], yArr[i]);
    }
    Comparator<Tuple<X,Y>> comparator = new Comparator<Tuple<X,Y>>() {
      public int compare(Tuple<X,Y> t1, Tuple<X, Y> t2) {
        return t1.first().compareTo(t2.first());
      }
    };
    Arrays.sort(tuples, comparator);
    return tuples;
  }

  public static Tuple<Double, Integer> [] sortDoubleArrayWithIndex(double [] xArr) {
    int length = xArr.length;
    Tuple<Double, Integer> [] tuples = new Tuple[length];
    for( int i = 0; i < length; i++ ) {
      tuples[i] = new Tuple<Double, Integer>( xArr[i], i);
    }

    Comparator<Tuple<Double, Integer>> comparator = new Comparator<Tuple<Double, Integer>>() {
      public int compare(Tuple<Double, Integer> t1, Tuple<Double, Integer> t2) {
        return t1.first().compareTo(t2.first());
      }
    };
    Arrays.sort(tuples, comparator);
    return tuples;
  }

  public static <X extends Comparable<? super X>, Y>
  void sortTupleList(List<Tuple<X,Y>> tupleList) {
    Comparator<Tuple<X,Y>> comparator = new Comparator<Tuple<X,Y>>() {
      public int compare(Tuple<X,Y> t1, Tuple<X, Y> t2) {
        return t1.first().compareTo(t2.first());
      }
    };

    Collections.sort(tupleList, comparator);
  }


}
