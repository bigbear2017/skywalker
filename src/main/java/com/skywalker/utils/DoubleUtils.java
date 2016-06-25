package com.skywalker.utils;

/**
 * @author caonn@mediav.com
 * @version 16/6/24.
 */
public class DoubleUtils {
  public static long wrapperValue(double yi, int base) {
    return (long) (yi * base);
  }

  public static double unwrapperValue(long yi, int base) {
    return yi * 1.0 / base;
  }
}
