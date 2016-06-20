package com.skywalker.tree;

import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The criterion can only be used for two classes.
 * For multiple class case, it will be implemented later.
 *
 * @author caonn
 * @version 16-3-7.
 */
public abstract class Criterion {
  private static Map<String, Criterion> criterionMap = new HashMap<String, Criterion>();
  static {
    criterionMap.put("mse", new MseCriterion());
    criterionMap.put("gini", new GiniCriterion());
    criterionMap.put("miss", new MissClassCriterion());
  }

  protected static DecisionTree.ParamBlock pb = null;
  protected static DecisionTree.DataBlock db = null;

  /**
   * Given an index, return current criterion value
   * @param i the index
   * @return criterion value
   */
  public abstract double getCriterionValue(int i);

  public abstract void init( int [] indices );

  public static Criterion getCriterion(DecisionTree.ParamBlock pb, DecisionTree.DataBlock db) {
    Criterion.pb = pb;
    Criterion.db = db;
    return criterionMap.get(pb.criterion);
  }
}

class MseCriterion extends Criterion {
  protected DoubleMatrix y;
  protected DoubleMatrix feature;
  protected int [] indices;
  protected int size;

  public MseCriterion() {
  }

  @Override
  public void init( int [] indices ) {
    this.feature = feature;
    this.y = y;
    this.indices = indices;
    this.size = indices.length;

    int size = arrIndices.length;
    indices = new int[size];
    for (int i = 0; i < size; i++) {
      indices[i] = arrIndices[i].second();
    }
  }

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

  /**
   * TODO : rewrite this part by using vectors
   * @return new criterion value
   */
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
  private Integer [] labelCounts;
  private int size;

  public void init(Tuple<Double, Integer>[] arrIndices ) {
    //super.init(arrIndices);
    List<Integer> countList = new ArrayList<Integer>();
    double preValue = Double.MIN_VALUE;
    int index = -1;
    int labelCounter = 0;
    for( int i = 0; i < size; i++ ) {
      double nextValue = arrIndices[i].first();
      labelCounter +=1;
      if( Math.abs(preValue - nextValue) > 0.001 ) {
        index += 1;
        countList.set(index, labelCounter);
        labelCounter = 1;
      } else {
        labelCounter +=1;
        countList.set(index, labelCounter);
      }
    }
    numLabel = countList.size();
    labelCounts = new Integer[numLabel];
    labelCounts = countList.toArray(labelCounts);
  }
  @Override
  public double getCriterionValue(int index) {
    updateP(index);
    return p * (1-p);
  }

  protected void updateP( int index ) {
  }

}

