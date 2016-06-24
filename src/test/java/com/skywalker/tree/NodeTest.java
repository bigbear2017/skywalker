package com.skywalker.tree;

import org.jblas.DoubleMatrix;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author caonn@mediav.com
 * @version 16/6/24.
 */
public class NodeTest {

  @Test
  public void test_split_node() {
    DecisionTree.ParamBlock pb = new DecisionTree.ParamBlock();
    pb.minSamples = 10;
    pb.maxHeight = 10;
    pb.criterion = "mse";
    int size = 10;
    DoubleMatrix x = DoubleMatrix.randn(size, 3);
    DoubleMatrix beta = DoubleMatrix.rand(3, 1);
    DoubleMatrix y = x.mmul(beta);
    DecisionTree.DataBlock db = new DecisionTree.DataBlock(x,y);
    System.out.println(x.getColumn(0).toString());
    Node headNode = Node.getHeadNode(pb,db);
    Splitter splitter = new Splitter();
    splitter.setFeatureIndex(0);
    splitter.setFeatureValue(0.1);
    Node left = headNode.splitLeftNode(splitter);
    System.out.println(Arrays.toString(left.getIndices()));
  }

  @Test
  public void test_best_splitter() {
    DecisionTree.ParamBlock pb = new DecisionTree.ParamBlock();
    pb.minSamples = 10;
    pb.maxHeight = 10;
    pb.criterion = "mse";
    int size = 10;
    DoubleMatrix x = DoubleMatrix.randn(size, 3);
    DoubleMatrix beta = DoubleMatrix.rand(3, 1);
    DoubleMatrix y = x.mmul(beta);
    DecisionTree.DataBlock db = new DecisionTree.DataBlock(x,y);
    System.out.println(x.getColumn(0).toString());
    Node headNode = Node.getHeadNode(pb,db);
    Splitter splitter = headNode.getBestSplitter();
    System.out.println(splitter.getFeatureIndex() + " " + splitter.getFeatureValue());
  }


}
