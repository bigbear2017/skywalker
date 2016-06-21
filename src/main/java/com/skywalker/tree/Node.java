package com.skywalker.tree;

import org.jblas.DoubleMatrix;

import java.util.Arrays;

/**
 * This class define the node of decision tree.
 *
 * @author caonn
 * @version 16-3-4.
 */
public class Node {
  private static Node headNode = null;
  private static DecisionTree.ParamBlock pb;
  private static DecisionTree.DataBlock db;
  private Criterion criterion = null;

  private Node leftNode = null; //Left Tree to traversal
  private Node rightNode = null; //Right Tree to traversal
  private Splitter splitter = null;
  private int [] indices = null; //the indices in this node

  public Node() {
  }

  /**
   * Get the head node, use singleton.
   * @return head node of the tree.
   */
  public static Node getHeadNode(DecisionTree.ParamBlock pb, DecisionTree.DataBlock db) {
    if( headNode == null ) {
      Node.pb = pb;
      Node.db = db;
      headNode = new Node();
      headNode.setIndices(getHeadIndices());
      headNode.initCriterion();
    }
    return headNode;
  }

  private static int [] getHeadIndices() {
    int [] indices = new int[db.totalSamples];
    for( int i = 0; i < db.totalSamples; i++ ) {
      indices[i] = i;
    }
    return indices;
  }

  private void initCriterion() {
    criterion = Criterion.getCriterion(pb, db);
    criterion.init(indices);
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
    criterion.init(indices);
    double bestCriterion = Double.MIN_VALUE;
    for (int f = 0; f < db.featureSize; f++ ) {
      DoubleMatrix feature = db.x.getColumn(f).getColumns(indices);//for the first feature, get the split point.
      double[] data = Arrays.copyOf(feature.data, feature.data.length);
      Arrays.sort(data);
      double preValue = Double.MIN_VALUE;
      //TODO optimize the search, skip some useless features.
      for(double fv : data) {
        if( fv == preValue ) {
          continue;
        }
        double criterionValue = criterion.getCriterionValue(f, fv);
        if (criterionValue > bestCriterion) {
          bestSplitter.setFeatureIndex(f);
          bestSplitter.setFeatureValue(fv);
          bestCriterion = criterionValue;
          preValue = fv;
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

  public Node splitLeftNode(Splitter splitter ) { return new Node();}
  public Node splitRightNode(Splitter splitter) { return new Node();}

  public Node getRightNode() {
    return rightNode;
  }

  public void setRightNode(Node rightNode) {
    this.rightNode = rightNode;
  }

  public int[] getIndices() {
    return indices;
  }

  public Splitter getSplitter() {
    return splitter;
  }

  public void setSplitter(Splitter splitter) {
    this.splitter = splitter;
  }

  public int getFeatureIndex() {
    return splitter.getFeatureIndex();
  }

  public double getFeatureValue() {
    return splitter.getFeatureValue();
  }

  public int getNumSamples() {
    return indices.length;
  }

  public void setIndices(int[] indices) {
    this.indices = indices;
  }
}

