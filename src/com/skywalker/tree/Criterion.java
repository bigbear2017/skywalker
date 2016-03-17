package com.skywalker.tree;

import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

/**
 * The criterion can only be used for two classes.
 * For multiple class case, it will be implemented later.
 * @author caonn
 * @version 16-3-7.
 */
public abstract class Criterion {
  protected double [] data;
  protected int [] indices;
  protected DoubleMatrix y ;
  public void init(Tuple<Double, Integer>[] arrIndices){
    int size = arrIndices.length;
    data = new double[size];
    indices = new int [size];
    for( int i = 0; i < size; i++ ) {
      data[i] = arrIndices[i].first();
      indices[i] = arrIndices[i].second();
    }
  }
  public abstract  double getCriterionValue(int i);
}

class MseCriterion extends Criterion {
  @Override
  public double getCriterionValue(int i) {

    return 0;
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

