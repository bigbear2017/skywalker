package com.skywalker.tree;

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
    private int [] indices; //all the data points that belongs to this node
}
