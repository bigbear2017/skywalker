package com.skywalker.tree;

import org.jblas.DoubleMatrix;
import org.junit.Test;

/**
 * @author caonn@mediav.com
 * @version 16/6/26.
 */
public class DecisionTreeTest {
  public void test_regression_tree() {
    DecisionTree.ParamBlock pb = new DecisionTree.ParamBlock();
    pb.minSamples = 2;
    pb.maxHeight = 7;
    pb.criterion = "cross";
    DecisionTree tree = new DecisionTree(pb);
    int dimension = 10;
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

  @Test
  public void test_miss_classify_tree() {
    DecisionTree.ParamBlock pb = new DecisionTree.ParamBlock();
    pb.minSamples = 10;
    pb.maxHeight = 100;
    pb.criterion = "gini";
    DecisionTree tree = new DecisionTree(pb);
    int dimension = 10;
    int samples = 1000;
    int testSamples = 10;
    DoubleMatrix x = DoubleMatrix.rand(samples, dimension);
    DoubleMatrix t = DoubleMatrix.rand(testSamples, dimension);
    DoubleMatrix beta = DoubleMatrix.rand(dimension, 1);
    DoubleMatrix y = x.mmul(beta);
    for( int i = 0; i < samples; i++ ) {
      y.put(i, 0, (int)y.get(i) - 1);
    }
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
    tree.printTree();
  }
}
