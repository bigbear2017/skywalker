package com.skywalker.utils;

/**
 * @author caonn@mediav.com
 * @version 16/6/24.
 */
public class DoubleUtils {
  public static final int DOUBLE_BASE = 100;

  public static long longBase(double yi, int base) {
    return (long) (yi * base);
  }

  public static double undoLongBase(long yi, int base) {
    return yi * 1.0 / base;
  }

  public static int intBase(double yi, int base) {
    return (int) (yi * base);
  }

  public static double undoIntBase(int yi, int base) {
    return yi * 1.0 / base;
  }
}
