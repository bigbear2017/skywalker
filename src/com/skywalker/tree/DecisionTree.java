package com.skywalker.tree;

import org.jblas.DoubleMatrix;

/** This class will implement the simplest decision tree algorithm.
 * I think I will implement CART algorithm.
 * @author caonn
 * @version 16-3-4.
 */
public class DecisionTree {

    /**
     *
     * @param x The data points to fix the model
     * @param y The labels to fit the model
     */
    public void fit(DoubleMatrix x, DoubleMatrix y){

    }

    public DoubleMatrix predict(){
        DoubleMatrix res = DoubleMatrix.EMPTY;
        return res;
    }
}
