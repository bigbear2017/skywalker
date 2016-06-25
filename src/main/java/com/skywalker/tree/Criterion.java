package com.skywalker.tree;

import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

import java.util.HashMap;
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

  public abstract Tuple<Double, Double> getBestSplitValue(int featureIndex);

  public abstract int [] getLeftIndices();
  public abstract int [] getRightIndices();

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
  protected DoubleMatrix ySort;
  protected DoubleMatrix xSort;
  protected int [] indices;
  protected int [] sortIndices;
  protected int size;
  int bestIndex;

  double leftY2 = 0;
  double leftY1 = 0;
  int leftNum = 0;
  int rightNum = 0;
  double rightY2 = 0;
  double rightY1 = 0;
  double leftAvg = 0;
  double rightAvg = 0;

  public MseCriterion() {
  }

  protected void createInstance(int featureIndex) {
    DoubleMatrix xf = x.getColumn(featureIndex).get(indices);
    DoubleMatrix yf = y.get(indices);
    sortIndices = xf.sortingPermutation();
    xSort = xf.get(sortIndices);
    ySort = yf.get(sortIndices);

    leftY2 = 0;
    leftY1 = 0;
    leftNum = 0;
    leftAvg = 0;

    rightY2 = yf.mul(yf).sum();
    rightY1 = yf.sum();
    rightNum = indices.length;
    rightAvg = 0;
  }

  protected void updateInstance(double yi) {
    leftY2 += yi * yi;
    leftY1 += yi;
    leftNum += 1;

    rightY2 -= yi * yi;
    rightY1 -= yi;
    rightNum -= 1;
  }

  protected double calcInstance() {
    leftAvg = leftY1 / leftNum;
    rightAvg = rightY1 / rightNum;
    double leftAvg2 = leftAvg * leftAvg;
    double rightAvg2 = rightAvg * rightAvg;
    double criterion = leftY2 + leftNum * leftAvg2 - 2 * leftAvg * leftY1;
    criterion += rightY2 + rightNum * rightAvg2 - 2 * rightAvg * rightY1;
    return -criterion;
  }

  @Override
  public Tuple<Double, Double> getBestSplitValue(int featureIndex) {
    createInstance(featureIndex);
    double pre = xSort.get(0);
    double bestCriterionValue = -Double.MAX_VALUE;
    double bestFeatureSplit = Double.MIN_VALUE;
    for(int i = 1; i < size; i++) {
      double v = xSort.get(i);
      double yi = ySort.get(i-1);
      updateInstance(yi);
      //find all the samples that have the same value,
      //then we calculate the criterion.
      if( v == pre ) {
        continue;
      }
      double criterion = calcInstance();
      if( criterion > bestCriterionValue ) {
        bestCriterionValue = criterion;
        bestFeatureSplit = pre;
        bestIndex = i-1;
      }
      pre = v;
    }

    return new Tuple(bestCriterionValue, bestFeatureSplit);
  }

  @Override
  public double getLabel(int [] indices) {
    return y.get(indices).mean();
  }

  @Override
  public int[] getLeftIndices() {
    int leftSize = bestIndex + 1;
    int [] leftIndices = new int[leftSize];
    System.arraycopy(sortIndices, 0, leftIndices, 0, leftSize);
    return leftIndices;
  }

  @Override
  public int[] getRightIndices() {
    int rightSize = size - bestIndex - 1;
    int [] rightIndices = new int[rightSize];
    System.arraycopy(sortIndices, bestIndex + 1, rightIndices, 0, rightSize);
    return rightIndices;
  }

  @Override
  public void init( int [] indices ) {
    this.indices = indices;
    this.size = indices.length;
    this.y = db.y;
    this.x = db.x;
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
  public Tuple<Double, Double> getBestSplitValue(int featureIndex) {
    return new Tuple(0, 0);
  }

  @Override
  public int[] getLeftIndices() {
    return new int[0];
  }

  @Override
  public int[] getRightIndices() {
    return new int[0];
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
  public int[] getLeftIndices() {
    return new int[0];
  }

  @Override
  public int[] getRightIndices() {
    return new int[0];
  }

  @Override
  public Tuple<Double, Double> getBestSplitValue(int featureIndex) {
    return new Tuple(0, 0);
  }

}

