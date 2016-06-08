package com.skywalker.tree;

import com.skywalker.utils.Sorter;
import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * This class define the node of decision tree.
 *
 * @author caonn
 * @version 16-3-4.
 */
public class Node {
  private static DoubleMatrix x;
  private static DoubleMatrix y;
  private static int featureSize;
  private static Criterion criterion = null;
  private static Map<String, Criterion> criterionMap = new HashMap<String, Criterion>();

  static {
    //criterionMap.put("mse", new MseCriterion());
  }

  private Node leftNode = null; //Left Tree to traversal
  private Node rightNode = null; //Right Tree to traversal
  private int featureIndex;
  private double featureValue;
  private int[] indices; //all the data points that belongs to this node

  public Node(int[] indices) {
    this.leftNode = null;
    this.rightNode = null;
    this.featureIndex = -1;
    this.featureValue = Double.MAX_VALUE;
    this.indices = indices;
  }

  public Node() {
  }

  public static Node getHeadNode(DoubleMatrix x, DoubleMatrix y, int numSamples, int featureSize, String criterion) {
    Node.x = x;
    Node.y = y;
    Node.featureSize = featureSize;
    Node.criterion = criterionMap.get(criterion);
    return new Node(new int[numSamples]);
  }

  public double getLabel(DoubleMatrix xp) {
    double res = 0;

    return res;
  }

  public boolean isLeafNode() {
    if (leftNode == null && rightNode == null) {
      return true;
    }
    return false;
  }

  public double getLabel() {
    return 0;
  }

  public Splitter getBestSplitter() {
    Splitter bestSplitter = new Splitter();
    double bestCriterion = Double.MIN_VALUE;
    for (int f = 0; f < featureSize; f++) {
      DoubleMatrix feature = x.getColumns(indices).getColumn(f);//for the first feature, get the split point.
      double[] data = feature.data;
      Tuple<Double, Integer>[] arrIndices = Sorter.sortDoubleArrayWithIndex(data);
      //criterion.init(arrIndices);
      int arrSize = arrIndices.length;
      for (int i = 0; i < arrSize; i++) {
        while ((i < arrSize - 1) && (arrIndices[i].first() != arrIndices[i + 1].first())) {
          i++;
        }
        double criterionValue = criterion.getCriterionValue(i);
        if (criterionValue > bestCriterion) {
          bestSplitter.setFeatureIndex(f);
          bestSplitter.setFeatureValue(arrIndices[i].first());
        }
      }

    }
    return bestSplitter;
  }

  public Node getLeftNode() {
    return leftNode;
  }

  public void setLeftNode(Node leftNode) {
    this.leftNode = leftNode;
  }

  public Node getRightNode() {
    return rightNode;
  }

  public void setRightNode(Node rightNode) {
    this.rightNode = rightNode;
  }

  public int getFeatureIndex() {
    return featureIndex;
  }

  public void setFeatureIndex(int featureIndex) {
    this.featureIndex = featureIndex;
  }

  public double getFeatureValue() {
    return featureValue;
  }

  public void setFeatureValue(double featureValue) {
    this.featureValue = featureValue;
  }

  public int[] getIndices() {
    return indices;
  }

}
