package com.skywalker.tree;

import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

/**
 * The criterion can only be used for two classes.
 * For multiple class case, it will be implemented later.
 *
 * @author caonn
 * @version 16-3-7.
 */
public abstract class Criterion {
  protected int size;
  protected int[] indices;
  protected double[] data;
  protected DoubleMatrix y;

  public void init(Tuple<Double, Integer>[] arrIndices) {
    int size = arrIndices.length;
    data = new double[size];
    indices = new int[size];
    for (int i = 0; i < size; i++) {
      data[i] = arrIndices[i].first();
      indices[i] = arrIndices[i].second();
    }
  }

  public abstract double getCriterionValue(int i);
}

class MseCriterion extends Criterion {
  private double sumPrefix = 0;
  private double sumSuffix = 0;
  private int lastPointer = 0;
  private double avgPrefix = 0;
  private double avgSuffix = 0;

  @Override
  public double getCriterionValue(int index) {
    updateSum(index);
    updateAvg(index);

    return 0;
  }

  protected  void updateSum( int index ) {
    for(int i = lastPointer; i <= index; i++ ) {
      sumPrefix += y.get(indices[i]);
      sumSuffix -= y.get(indices[i]);
    }
  }

  protected void updateAvg( int index ) {
    avgPrefix = sumPrefix / index;
    avgSuffix = sumSuffix / ( size - index - 1);
  }
}

class GiniCriterion extends Criterion {
  @Override
  public double getCriterionValue(int i) {
    return 0;
  }
}

class MissClassCriterion extends Criterion {
  @Override
  public double getCriterionValue(int i) {
    return 0;
  }
}

