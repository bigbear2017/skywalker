package com.skywalker.tree;

import org.jblas.DoubleMatrix;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class will implement the simplest decision tree algorithm.
 * I think I will implement CART algorithm.
 *
 * @author caonn
 * @version 16-3-4.
 */
public class DecisionTree {
  private Node headNode = null;
  private static DataBlock db;
  public DecisionTree(String [] args) {
  }

  /**
   * With all the parameters above, fit the data with the model.
   *
   * @param x The data points to fix the model
   * @param y The labels to fit the model
   */
  public void fit(DoubleMatrix x, DoubleMatrix y) {
    Node headNode = Node.getHeadNode(x, y, db.numFeatures, x.getColumns(), db.criterion);
    Queue<Node> curLevelNodes = new LinkedList<Node>();
    curLevelNodes.add(headNode);
    int treeHeight = 0;
    while (treeHeight < db.maxHeight && !curLevelNodes.isEmpty()) {
      Queue<Node> nextLevelNodes = new LinkedList<Node>();
      while (!curLevelNodes.isEmpty()) {
        Node currentNode = curLevelNodes.poll();
        if (currentNode.getIndices().length < db.minSamples) {
          continue;
        }
        Splitter splitter = currentNode.getBestSplitter();
        Node leftNode = currentNode.splitLeftNode(splitter);
        Node rightNode = currentNode.splitRightNode(splitter);
        if (leftNode.getIndices().length < db.minSamples || rightNode.getIndices().length < db.minSamples) {
          continue;
        }
        currentNode.setLeftNode(leftNode);
        currentNode.setRightNode(rightNode);
        currentNode.setFeatureIndex(splitter.getFeatureIndex());
        currentNode.setFeatureValue(splitter.getFeatureValue());
        nextLevelNodes.add(leftNode);
        nextLevelNodes.add(rightNode);
      }
      curLevelNodes = nextLevelNodes;
      treeHeight ++;
    }
  }

  public DoubleMatrix predict(DoubleMatrix xs) {
    int numSamplesX = xs.rows;
    Node node = headNode;
    DoubleMatrix res = DoubleMatrix.ones(numSamplesX);
    for( int i = 0; i < numSamplesX; i++ ) {
      while(!node.isLeafNode()) {
        DoubleMatrix xp = xs.getRow(i);
        if (xp.get(node.getFeatureIndex()) < node.getFeatureValue()) {
          node = node.getLeftNode();
        } else {
          node = node.getRightNode();
        }
      }
      double labelValue = node.getLabel();
      res.put(i, labelValue);
    }
    return res;
  }
  public static class DataBlock {
    public final int minSamples;  //mininal samles for each node, default 1
    public final int maxHeight;   //maximal height for the tree
    public final int numFeatures ;
    public final int numSamples ;
    public final String criterion ;
    public final DoubleMatrix x;
    public final DoubleMatrix y ;

    public DataBlock(int minSamples, int maxHeight, int numFeatures, int numSamples,
                     String criterion, DoubleMatrix x, DoubleMatrix y) {
      this.minSamples = minSamples;
      this.maxHeight = maxHeight;
      this.numFeatures = numFeatures;
      this.numSamples = numSamples;
      this.criterion = criterion;
      this.x = x;
      this.y = y;
    }
  }
}
