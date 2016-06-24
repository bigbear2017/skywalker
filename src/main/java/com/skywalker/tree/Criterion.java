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

  public abstract double getLabel(int [] indices);

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
  public double getLabel(int [] indices) {
    return y.get(indices).mean();
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
      int index = indices[i];
      if( xs.get(index) <= featureValue ) {
        leftCounter += 1;
        leftSum += y.get(index);
      } else {
        rightCounter += 1;
        rightSum += y.get(index);
      }
    }
  }

  private double calcCriterion(int featureIndex, double featureValue) {
    double criterion = 0;
    DoubleMatrix xs = x.getColumn(featureIndex);
    double leftAvg = leftSum / leftCounter;
    double rightAvg = rightSum / rightCounter;
    for(int i = 0; i < size; i++) {
      int index = indices[i];
      if( xs.get(index) <= featureValue ) {
        criterion += (y.get(index) - leftAvg) * (y.get(index) - leftAvg);
      } else {
        criterion += (y.get(index) - rightAvg) * (y.get(index) - rightAvg);
      }
    }
    return criterion;
  }

}

class GiniCriterion extends Criterion {
  @Override
  public double getLabel(int [] indices) {
    return 0;
  }

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
  int leftCorrect;
  int rightCorrect;
  List<Map.Entry<Integer,Integer>> leftList;
  List<Map.Entry<Integer,Integer>> rightList;

  @Override
  public void init(int[] indices) {
    this.indices = indices;
    this.size = indices.length;
    this.y = db.y;
    this.x = db.x;
  }

  @Override
  public double getLabel(int [] indices) {
    return 0;
  }

  @Override
  public double getCriterionValue(int featureIndex, double featureValue) {
    clearValue();
    updateLabel(featureIndex, featureValue);
    return -1 * calcCriterion(featureIndex, featureValue);
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
      int index = indices[i];
      Integer key = wrapperValue(y.get(index));
      if( xs.get(index) <= featureValue ) {
        leftCounter += 1;
        MapUtils.incrementMap(leftDict, key, 1);
      } else {
        rightCounter += 1;
        MapUtils.incrementMap(rightDict, key, 1);
      }
    }
    leftList = MapUtils.sortMapByValue(leftDict);
    leftLabel = unwrapperValue(leftList.get(0).getKey());
    leftCorrect = leftList.get(0).getValue();
    rightList = MapUtils.sortMapByValue(rightDict);
    rightLabel = unwrapperValue(rightList.get(0).getKey());
    rightCorrect = rightList.get(0).getValue();
  }

  private int wrapperValue(double yi) {
    return (int) (yi * 10);
  }

  private double unwrapperValue(int yi) {
    return yi * 1.0 / 10;
  }

  protected double calcCriterion(int featureIndex, double featureValue) {
    double criterion = 0;
    criterion += ( 1 - leftCorrect * 1.0  / leftCounter );
    criterion += ( 1- rightCorrect * 1.0 / rightCounter );
    return criterion;
  }

}

