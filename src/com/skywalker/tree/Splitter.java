package com.skywalker.tree;

/**
 * This class is to split each node. Given a node, the splitter will scan all
 * the features. And for each feature, the splitter will scan all the possible
 * split points. The point which have the largest criterion value will be selected.
 *
 * @author caonn
 * @version 16-3-4.
 */
public class Splitter {
  private int featureIndex;
  private double featureValue;

  private double getBestSplitValue() {
    return 0;
  }

  private int getBestSplitIndex() {
    return 1;
  }

  public Node getLeftNode() {
    return new Node();
  }

  public Node getRightNode() {
    return new Node();
  }

  public int getFeatureIndex() {
    return featureIndex;
  }

  public double getFeatureValue() {
    return featureValue;
  }
}
