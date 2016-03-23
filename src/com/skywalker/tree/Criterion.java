package com.skywalker.tree;

import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

import java.util.ArrayList;
import java.util.List;

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
    return updateCriterion();
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

  protected double updateCriterion() {
    double criterionValue = 0;
    for(int i = 0; i < lastPointer; i++) {
      double value = y.get(indices[i]) - avgPrefix;
      criterionValue += value * value;
    }
    for(int i = lastPointer + 1; i < size; i++) {
      double value = y.get(indices[i]) - avgSuffix;
      criterionValue += value * value;
    }
    return criterionValue;
  }
}

class GiniCriterion extends Criterion {
  @Override
  public double getCriterionValue(int i) {
    return 0;
  }
}

class MissClassCriterion extends Criterion {
  private double p = 0;
  private int numLabel = 0;
  private int [] labelArray;

  @Override
  public void init(Tuple<Double, Integer>[] arrIndices ) {
    super.init(arrIndices);
    List<Integer> labelList = new ArrayList<Integer>();
    double preValue = arrIndices[0].first();
    int labelCounter = 1;
    for( int i = 1; i < size; i++ ) {
      double nextValue = arrIndices[i].first();
      if( Math.abs(preValue - nextValue) > 0.001 ) {
        labelList.add(labelCounter);
        labelCounter = 0;
        preValue = nextValue;
      }
      labelCounter += 1;
    }
  }
  @Override
  public double getCriterionValue(int index) {
    updateP(index);
    return p * (1-p);
  }

  protected void updateP( int index ) {
  }

}

