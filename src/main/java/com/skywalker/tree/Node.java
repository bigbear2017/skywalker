package com.skywalker.tree;

import com.google.common.collect.Lists;
import com.skywalker.utils.ListUtils;
import com.skywalker.utils.Tuple;
import org.jblas.DoubleMatrix;

import java.util.List;

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

  public Node(int [] indices) {
    this.indices = indices;
    this.criterion = Criterion.getCriterion(pb, db);
  }

  /**
   * Get the head node, use singleton.
   * @return head node of the tree.
   */
  public static Node getHeadNode(DecisionTree.ParamBlock pb, DecisionTree.DataBlock db) {
    if( headNode == null ) {
      Node.pb = pb;
      Node.db = db;
      headNode = new Node(getHeadIndices());
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

  public boolean isLeafNode() {
    if (leftNode == null && rightNode == null) {
      return true;
    }
    return false;
  }

  public double getLabel() {
    return criterion.getLabel(indices);
  }

  public Splitter getBestSplitter() {
    Splitter bestSplitter = new Splitter();
    double bestCriterion = Double.MAX_VALUE;
    for (int f = 0; f < db.featureSize; f++ ) {
      Tuple<Double, Double> split = criterion.getBestSplitValue(indices, f);
      //System.out.println("Split feature : " + f);
      if( split.first() < bestCriterion ) {
        bestCriterion = split.first();
        bestSplitter.setFeatureIndex(f);
        bestSplitter.setFeatureValue(split.second());
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


  public Node splitLeftNode(Splitter splitter ) {
    DoubleMatrix xs = db.x.getColumn(splitter.getFeatureIndex());
    List<Integer> leftIndices = Lists.newArrayList();
    for(int index : indices) {
      if(xs.get(index) <= splitter.getFeatureValue()) {
        leftIndices.add(index);
      }
    }
    int [] leftIndicesArray = ListUtils.toArray(leftIndices);
    return new Node(leftIndicesArray);
  }


  public Node splitRightNode(Splitter splitter) {
    DoubleMatrix xs = db.x.getColumn(splitter.getFeatureIndex());
    List<Integer> rightIndices = Lists.newArrayList();
    for(int index : indices) {
      if(xs.get(index) > splitter.getFeatureValue()) {
        rightIndices.add(index);
      }
    }
    int [] rightIndicesArray = ListUtils.toArray(rightIndices);
    return new Node(rightIndicesArray);
  }

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

