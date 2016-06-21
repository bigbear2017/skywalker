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

  /**
   * With all the parameters above, fit the data with the model.
   *
   * @param x The data points to fix the model
   * @param y The labels to fit the model
   */
  public void fit(DoubleMatrix x, DoubleMatrix y) {
    db = new DataBlock(x, y);
    Node headNode = Node.getHeadNode(pb, db);
    Queue<Node> curLevelNodes = new LinkedList<Node>();
    curLevelNodes.add(headNode);
    int treeHeight = 0;
    while (treeHeight < pb.maxHeight && !curLevelNodes.isEmpty()) {
      Queue<Node> nextLevelNodes = new LinkedList<Node>();
      while (!curLevelNodes.isEmpty()) {
        Node currentNode = curLevelNodes.poll();
        if (currentNode.getIndices().length < pb.minSamples) {
          continue;
        }
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
