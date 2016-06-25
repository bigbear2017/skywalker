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
    this.criterion.init(indices);
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
    criterion.init(indices);
    double bestCriterion = -Double.MAX_VALUE;
    for (int f = 0; f < db.featureSize; f++ ) {
      Tuple<Double, Double> split = criterion.getBestSplitValue(f);
      if( split.first() > bestCriterion ) {
        bestCriterion = split.first();
        bestSplitter.setFeatureIndex(f);
        bestSplitter.setFeatureValue(split.second());
        bestSplitter.setLeftIndices(criterion.getLeftIndices());
        bestSplitter.setRightIndices(criterion.getRightIndices());
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
    return new Node(splitter.getLeftIndices());
  }

  /**
   *  Before this, I use another naive method to split the node.
   *  Scan the feature and split the indices. Later, I use this method.
   *  I think this could be a little faster. So, I keep the code here.
   *  Maybe I can find a better way to split the node.
   * @param splitter
   * @return
   */
  public Node splitRightNode(Splitter splitter) {
/*    DoubleMatrix xs = db.x.getColumn(splitter.getFeatureIndex());
    List<Integer> rightIndices = Lists.newArrayList();
    for(int index : indices) {
      if(xs.get(index) > splitter.getFeatureValue()) {
        rightIndices.add(index);
      }
    }
    int [] rightIndicesArray = ListUtils.toArray(rightIndices);*/
    //return new Node(rightIndicesArray);
    return new Node(splitter.getRightIndices());
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

