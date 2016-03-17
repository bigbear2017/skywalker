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
  private int minSamples;  //mininal samles for each node, default 1
  private int maxHeight;   //maximal height for the tree
  private int numFeatures;
  private int numSamples;
  private String criterion;
  private Node headNode;

  /**
   * With all the parameters above, fit the data with the model.
   *
   * @param x The data points to fix the model
   * @param y The labels to fit the model
   */
  public void fit(DoubleMatrix x, DoubleMatrix y) {
    Node headNode = Node.getHeadNode(x, y, numFeatures, x.getColumns(), criterion);
    Queue<Node> treeNodes = new LinkedList<Node>();
    treeNodes.add(headNode);
    int treeHeight = 0;
    while (treeHeight < maxHeight) {
      while (!treeNodes.isEmpty()) {
        Node currentNode = treeNodes.poll();
        if (currentNode.getIndices().length < minSamples) {
          continue;
        }
        Splitter splitter = currentNode.getBestSplitter();
        Node leftNode = splitter.getLeftNode();
        Node rightNode = splitter.getRightNode();
        if (leftNode.getIndices().length < minSamples || rightNode.getIndices().length < minSamples) {
          continue;
        }
        currentNode.setLeftNode(leftNode);
        currentNode.setRightNode(rightNode);
        currentNode.setFeatureIndex(splitter.getFeatureIndex());
        currentNode.setFeatureValue(splitter.getFeatureValue());
        treeNodes.add(leftNode);
        treeNodes.add(rightNode);
      }
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
}
