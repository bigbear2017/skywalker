package com.skywalker.tree;

import com.skywalker.utils.OptionParser;
import org.jblas.DoubleMatrix;

import java.util.LinkedList;
import java.util.Queue;

import org.kohsuke.args4j.Option;

/**
 * This class will implement the simplest decision tree algorithm.
 * I think I will implement CART algorithm.
 *
 * @author caonn
 * @version 16-3-4.
 */
public class DecisionTree {
  private Node headNode = null;
  private static ParamBlock pb;
  private static DataBlock db;

  public DecisionTree(String [] args) {
    pb = OptionParser.parse(new ParamBlock(), args);
  }

  public DecisionTree(ParamBlock pb) {
    this.pb = pb;
  }

  /**
   * With all the parameters above, fit the data with the model.
   *
   * @param x The data points to fix the model
   * @param y The labels to fit the model
   */
  public void fit(DoubleMatrix x, DoubleMatrix y) {
    db = new DataBlock(x, y);
    headNode = Node.getHeadNode(pb, db);
    Queue<Node> curLevelNodes = new LinkedList<Node>();
    curLevelNodes.add(headNode);
    int treeHeight = 0;
    while (treeHeight < pb.maxHeight && !curLevelNodes.isEmpty()) {
      Queue<Node> nextLevelNodes = new LinkedList<Node>();
      while (!curLevelNodes.isEmpty()) {
        Node currentNode = curLevelNodes.poll();
        Splitter splitter = currentNode.getBestSplitter();
        Node leftNode = currentNode.splitLeftNode(splitter);
        Node rightNode = currentNode.splitRightNode(splitter);
        if (leftNode.getNumSamples() < pb.minSamples || rightNode.getNumSamples() < pb.minSamples) {
          continue;
        }
        currentNode.setLeftNode(leftNode);
        currentNode.setRightNode(rightNode);
        currentNode.setSplitter(splitter);
        nextLevelNodes.add(leftNode);
        nextLevelNodes.add(rightNode);
      }
      curLevelNodes = nextLevelNodes;
      treeHeight ++;
    }
  }

  public DoubleMatrix predict(DoubleMatrix xs) {
    int numSamplesX = xs.rows;
    DoubleMatrix res = DoubleMatrix.ones(numSamplesX);
    for( int i = 0; i < numSamplesX; i++ ) {
      Node node = headNode;
      DoubleMatrix xp = xs.getRow(i);
      while(!node.isLeafNode()) {
        if (xp.get(node.getFeatureIndex()) <= node.getFeatureValue()) {
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

  public void printTree() {
    Node node = headNode;
    printNode(node);
  }

  private void printNode(Node node) {
    if( node != null  ) {
      if( !node.isLeafNode()) {
        System.out.println("Split Index:" + node.getFeatureIndex() + " Split Value:" + node.getFeatureValue()
                + " node size :" + node.getNumSamples());
        printNode(node.getLeftNode());
        printNode(node.getRightNode());
      } else {
        System.out.println("Leaf value:" + node.getLabel());
      }
    }
  }

  public static void main(String [] args) {
    ParamBlock pb = new ParamBlock();
    pb.minSamples = 2;
    pb.maxHeight = 7;
    pb.criterion = "mse";
    DecisionTree tree = new DecisionTree(pb);
    int dimension = 100;
    int samples = 100000;
    int testSamples = 10;
    DoubleMatrix x = DoubleMatrix.rand(samples, dimension);
    DoubleMatrix t = DoubleMatrix.rand(testSamples, dimension);
    DoubleMatrix beta = DoubleMatrix.rand(dimension, 1);
    DoubleMatrix y = x.mmul(beta);
    DoubleMatrix ty = t.mmul(beta);
    long now = System.currentTimeMillis();
    tree.fit(x, y);
    now = System.currentTimeMillis() - now;
    System.out.println("Time is : " + now/ 1000);
    DoubleMatrix py = tree.predict(t);
    DoubleMatrix pt = py.sub(ty);
    double mse = pt.mul(pt).sum()/testSamples;
    System.out.println(ty);
    System.out.println(py);
    System.out.println("Mse : " + mse);
  }
  public static class ParamBlock {
    @Option(name = "-minSamples", usage = "the minimum samples for each node")
    public int minSamples;  //mininal samles for each node, default 1

    @Option(name = "-maxHeight", usage = "The max height of the tree")
    public int maxHeight;   //maximal height for the tree

    @Option(name = "-criterion", usage = "Criterion to use in the evaluation")
    public String criterion ;

  }
  public static class DataBlock {
    public final DoubleMatrix x;
    public final DoubleMatrix y;
    public final int featureSize;
    public final int totalSamples;

    public DataBlock(DoubleMatrix x, DoubleMatrix y) {
      this.x = x;
      this.y = y;
      this.featureSize = x.columns;
      this.totalSamples = x.rows;
    }
  }
}
