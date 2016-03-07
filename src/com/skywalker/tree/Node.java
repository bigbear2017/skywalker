package com.skywalker.tree;

import org.jblas.DoubleMatrix;

import java.util.ArrayList;

/**
 * @author caonn
 * @version 16-3-4.
 */
public class Node {
  private Node leftNode = null; //Left Tree to traversal
  private Node rightNode = null; //Right Tree to traversal
  private int featureIndex;
  private double featureValue;
  private int[] indices; //all the data points that belongs to this node

  private static DoubleMatrix x;
  private static DoubleMatrix y;

  public Node(int[] indices) {
    this.leftNode = null;
    this.rightNode = null;
    this.featureIndex = -1;
    this.featureValue = Double.MAX_VALUE;
    this.indices = indices;
  }

  public double getLabel(DoubleMatrix xp) {
    double res = 0;

    return res;
  }

  public Node() {
  }

  public boolean isLabel() {
    if(leftNode == null && rightNode == null) {
      return true;
    }
    return false;
  }

  public double getLabel() {
    return 0;
  }

  public static Node getHeadNode(int numSamples) {
    return new Node(new int[numSamples]);
  }

  public Splitter getBestSplitter() {
    return new Splitter();
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

  public void setIndices(int[] indices) {
    this.indices = indices;
  }
}
