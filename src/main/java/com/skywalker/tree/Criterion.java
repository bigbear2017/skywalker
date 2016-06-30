package com.skywalker.tree;

import com.google.common.collect.Maps;
import com.skywalker.utils.DoubleUtils;
import com.skywalker.utils.MapUtils;
import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Here we implement the criterion class. We calculate the criterion incrementally, which means,
 * for one feature, we update the criterion value for all the possible splits. We do not need
 * to scan the data every time. To implement new criterion, we only need to figure out how to
 * calculate the value incrementally.
 * <p>
 *
 * We have implement regression criterion and classification criterion. For regression criterion,
 * we have used the method : (a - b)^2 = a^2 + b^2 - 2 * a * b. So each time, we update a^2, b^2
 * and 2 * a * b. For classification criterion, it will be easier to calculate the value.
 *
 * @author caonn
 * @version 16-3-7.
 */
public abstract class Criterion {
  private static Map<String, Criterion> criterionMap = new HashMap<String, Criterion>();
  static {
    criterionMap.put("mse", new MseCriterion());
    criterionMap.put("gini", new GiniCriterion());
    criterionMap.put("miss", new MissClassifyCriterion());
    criterionMap.put("cross", new CrossEntropyCriterion());
  }

  protected static DecisionTree.ParamBlock pb = null;
  protected static DecisionTree.DataBlock db = null;

  protected DoubleMatrix y;
  protected DoubleMatrix x;
  protected int [] indices;
  protected int size;

  protected DoubleMatrix ySort;
  protected DoubleMatrix xSort;
  protected int [] sortIndices;

  public void init(int[] indices, int featureIndex) {
    this.indices = indices;
    this.size = indices.length;
    this.y = db.y;
    this.x = db.x;
    DoubleMatrix xf = x.getColumn(featureIndex).get(indices);
    DoubleMatrix yf = y.get(indices);
    this.sortIndices = xf.sortingPermutation();
    this.xSort = xf.get(sortIndices);
    this.ySort = yf.get(sortIndices);
  }

  abstract protected void createInstance();

  abstract protected void updateInstance(double yi);

  abstract protected double calcInstance();

  public Tuple<Double, Double> getBestSplitValue(int [] indices, int featureIndex) {
    init(indices, featureIndex);
    createInstance();
    double preY = ySort.get(0);
    double bestCriterionValue = Double.MAX_VALUE;
    double bestFeatureSplit = Double.MAX_VALUE;
    //int besti = 0;
    //find the minimal split right
    int end = size - 1 - pb.minSamples;
    double preX = xSort.get(end);
    while( end > pb.minSamples ) {
      double xi = xSort.get(end - 1);
      if ( xi != preX ) {
        break;
      }
      preX = xi;
      end --;
    }

    preX = xSort.get(0);
    //find the minimal split right
    for(int i = 1; i < end; i++) {
      double xi = xSort.get(i);
      double yi = ySort.get(i);
      updateInstance(preY);
      //find all the samples that have the same value,
      //then we calculate the criterion.
      if( xi == preX || i < pb.minSamples) {
        continue;
      }
      double criterion = calcInstance();
      if( criterion < bestCriterionValue ) {
        bestCriterionValue = criterion;
        bestFeatureSplit = preX;
        //besti = i;
      }
      preX = xi;
      preY = yi;
    }

    //System.out.println("best criterion value: " + bestCriterionValue + "best I: " + besti);
    return new Tuple(bestCriterionValue, bestFeatureSplit);
  }

  public abstract double getLabel(int [] indices);

  public static Criterion getCriterion(DecisionTree.ParamBlock pb, DecisionTree.DataBlock db) {
    Criterion.pb = pb;
    Criterion.db = db;
    return criterionMap.get(pb.criterion);
  }
}

class MseCriterion extends Criterion {
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

  protected void createInstance() {
    leftY2 = 0;
    leftY1 = 0;
    leftNum = 0;
    leftAvg = 0;

    rightY2 = ySort.mul(ySort).sum();
    rightY1 = ySort.sum();
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
    return criterion;
  }

  @Override
  public double getLabel(int [] indices) {
    return y.get(indices).mean();
  }
}

/**
 */
abstract class ClassifyCriterion extends Criterion {
  Map<Integer, Integer> leftCount;
  Map<Integer, Integer> rightCount;

  int leftSize;
  int rightSize;

  @Override
  public double getLabel(int [] indices) {
    DoubleMatrix ys = y.get(indices);
    int size = indices.length;
    Map<Integer, Integer> countMap = Maps.newHashMap();
    for( int i = 0; i < size; i++ ) {
      double yi = ys.get(i);
      MapUtils.incrementMap(countMap, DoubleUtils.intBase(yi, DoubleUtils.DOUBLE_BASE), 1);
    }
    return DoubleUtils.undoIntBase(getMaxValueEntry(countMap).getKey(), DoubleUtils.DOUBLE_BASE);
  }

  protected Map.Entry<Integer, Integer> getMaxValueEntry(Map<Integer,Integer> countMap) {
    int maxCount = 0;
    Map.Entry<Integer, Integer> maxEntry = null;
    for( Map.Entry<Integer, Integer> entry : countMap.entrySet() ) {
      if( entry.getValue() > maxCount ) {
        maxCount = entry.getValue();
        maxEntry = entry;
      }
    }
    return maxEntry;
  }

  protected void createInstance() {
    leftCount = Maps.newHashMap();
    rightCount = Maps.newHashMap();
    for(double yi : ySort.data) {
      MapUtils.incrementMap(rightCount, DoubleUtils.intBase(yi, DoubleUtils.DOUBLE_BASE), 1);
    }
    leftSize = 0;
    rightSize = size;
  }

  protected void updateInstance(double yi) {
    MapUtils.incrementMap(leftCount, DoubleUtils.intBase(yi, DoubleUtils.DOUBLE_BASE), 1);
    MapUtils.incrementMap(rightCount, DoubleUtils.intBase(yi, DoubleUtils.DOUBLE_BASE), -1);
    leftSize ++;
    rightSize --;
  }

  abstract protected double calcInstance() ;
}

class MissClassifyCriterion extends ClassifyCriterion {
  protected double calcInstance() {
    double sum = 0;
    Map.Entry<Integer, Integer> maxLeftEntry = getMaxValueEntry(leftCount);
    sum += ( leftSize - maxLeftEntry.getValue() ) * 1.0 / leftSize;

    Map.Entry<Integer, Integer> maxRightEntry = getMaxValueEntry(rightCount);
    sum += ( rightSize - maxRightEntry.getValue() ) * 1.0 / rightSize;
    return sum;
  }
}

class GiniCriterion extends ClassifyCriterion {
  @Override
  protected double calcInstance() {
    double sum = 0;
    Map.Entry<Integer, Integer> maxLeftEntry = getMaxValueEntry(leftCount);
    double pl = ( leftSize - maxLeftEntry.getValue() ) * 1.0 / leftSize;
    Integer key = maxLeftEntry.getKey();
    for( Map.Entry<Integer, Integer> entry: leftCount.entrySet()) {
      if(! entry.getKey().equals(key) ) {
        double pm = entry.getValue()  * 1.0 / leftSize;
        sum += pm * pl;
      }
    }

    Map.Entry<Integer, Integer> maxRightEntry = getMaxValueEntry(rightCount);
    double pr = ( rightCount.size() - maxRightEntry.getValue() ) * 1.0 / rightCount.size();
    for( Map.Entry<Integer, Integer> entry: leftCount.entrySet()) {
      if( !entry.getKey().equals(key) ) {
        double pm =  entry.getValue()  * 1.0 / rightSize;
        sum += pm * pr;
      }
    }
    return sum;
  }
}

class CrossEntropyCriterion extends ClassifyCriterion {
  @Override
  protected double calcInstance() {
    double sum = 0;
    for( Map.Entry<Integer, Integer> entry: leftCount.entrySet()) {
      double pm = entry.getValue()  * 1.0 / leftSize;
      sum += pm * Math.log(pm);
    }

    for( Map.Entry<Integer, Integer> entry: leftCount.entrySet()) {
      double pm =  entry.getValue()  * 1.0 / rightSize;
      sum += pm * Math.log(pm);
    }
    return sum;
  }
}

