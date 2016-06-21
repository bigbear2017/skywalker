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
   * @param featureValue the feature value to split current node
   * @return criterion value
   */
  public abstract double getCriterionValue(int featureIndex, double featureValue);

  public abstract void init( int [] indices );

  public static Criterion getCriterion(DecisionTree.ParamBlock pb, DecisionTree.DataBlock db) {
    Criterion.pb = pb;
    Criterion.db = db;
    return criterionMap.get(pb.criterion);
  }
}

class MseCriterion extends Criterion {
  protected DoubleMatrix y;
  protected DoubleMatrix x;
  protected int [] indices;
  protected int size;

  int leftCounter = 0;
  int rightCounter = 0;
  double leftSum = 0;
  double rightSum = 0;

  public MseCriterion() {
  }

  @Override
  public void init( int [] indices ) {
    this.indices = indices;
    this.size = indices.length;
    this.y = db.y;
    this.x = db.x;
  }

  @Override
  public double getCriterionValue(int featureIndex, double featureValue) {
    clearValue();
    updateAvg(featureIndex, featureValue);
    return -1 * calcCriterion(featureIndex, featureValue);
  }

  private void clearValue() {
    leftCounter = 0;
    rightCounter = 0;
    leftSum = 0;
    rightSum = 0;
  }

  private void updateAvg(int featureIndex, double featureValue) {
    DoubleMatrix xs = x.getColumn(featureIndex);
    for(int i = 0; i < size; i++) {
      if( xs.get(i) <= featureValue ) {
        leftCounter += 1;
        leftSum += y.get(i);
      } else {
        rightCounter += 1;
        rightSum += y.get(i);
      }
    }
  }

  private double calcCriterion(int featureIndex, double featureValue) {
    double criterion = 0;
    DoubleMatrix xs = x.getColumn(featureIndex);
    double leftAvg = leftSum / leftCounter;
    double rightAvg = rightSum / rightCounter;
    for(int i = 0; i < size; i++) {
      if( xs.get(i) <= featureValue ) {
        criterion += (y.get(i) - leftAvg) * (y.get(i) - leftAvg);
      } else {
        criterion += (y.get(i) - rightAvg) * (y.get(i) - rightAvg);
      }
    }
    return criterion;
  }

}

class GiniCriterion extends Criterion {

  @Override
  public void init(int[] indices) {
  }

  @Override
  public double getCriterionValue(int featureIndex, double featureValue) {
    return 0;
  }
}

class MissClassCriterion extends Criterion {


  private double p = 0;
  private int numLabel = 0;
  private Integer [] labelCounts;
  private int size;

  @Override
  public void init(int[] indices) {

  }

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
  public double getCriterionValue(int featureIndex, double featureValue) {
    int index = 0;
    updateP(index);
    return p * (1-p);
  }

  protected void updateP( int index ) {
  }

}

