package com.skywalker.tree;

import com.google.common.collect.Maps;
import com.skywalker.utils.MapUtils;
import org.jblas.DoubleMatrix;

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
    updateCounter(featureIndex, featureValue);
    return -1 * calcCriterion(featureIndex, featureValue);
  }

  private void clearValue() {
    leftCounter = 0;
    rightCounter = 0;
    leftSum = 0;
    rightSum = 0;
  }

  private void updateCounter(int featureIndex, double featureValue) {
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


/**
 * A class to calculate the miss classification error criterion.
 * All the values will be cast to int label.
 */
class MissClassCriterion extends Criterion {
  protected DoubleMatrix y;
  protected DoubleMatrix x;
  protected int [] indices;
  protected int size;

  double leftLabel;
  double rightLabel;
  int leftCounter;
  int rightCounter;

  @Override
  public void init(int[] indices) {
    this.indices = indices;
    this.size = indices.length;
    this.y = db.y;
    this.x = db.x;
  }
  @Override
  public double getCriterionValue(int featureIndex, double featureValue) {
    clearValue();
    updateLabel(featureIndex, featureValue);
    return 0;
  }

  private void clearValue() {
    leftCounter = 0;
    rightCounter = 0;
    leftLabel = 0;
    rightLabel = 0;
  }

  private void updateLabel(int featureIndex, double featureValue) {
    DoubleMatrix xs = x.getColumn(featureIndex);
    Map<Integer, Integer> leftDict = Maps.newHashMap();
    Map<Integer, Integer> rightDict = Maps.newHashMap();
    for(int i = 0; i < size; i++) {
      Integer key = wrapperValue(y.get(i));
      if( xs.get(i) <= featureValue ) {
        leftCounter += 1;
        MapUtils.incrementMap(leftDict, key, 1);
        List<Map.Entry<Integer, Integer>> values = MapUtils.sortMapByValue(leftDict);
        leftLabel = unwrapperValue(values.get(0).getKey());
      } else {
        rightCounter += 1;
        MapUtils.incrementMap(rightDict, key, 1);
        List<Map.Entry<Integer, Integer>> values = MapUtils.sortMapByValue(rightDict);
        rightLabel = unwrapperValue(values.get(0).getKey());
      }
    }
  }

  private int wrapperValue(double yi) {
    return (int) (yi * 10);
  }

  private double unwrapperValue(int yi) {
    return yi * 1.0 / 10;
  }

  private double calcCriterion(int featureIndex, double featureValue) {
    double criterion = 0;
    return criterion;
  }

}

