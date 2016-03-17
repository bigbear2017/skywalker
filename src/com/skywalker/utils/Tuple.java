package com.skywalker.utils;

/** This is a simple tuple implementation in Java.
 * @author caonn
 * @version 16-3-10.
 */
public class Tuple<X, Y> {
  private X x;
  private Y y;

  public Tuple(X x, Y y) {
    this.x = x;
    this.y = y;
  }
  public X first(){ return x; }
  public Y second() { return y;}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Tuple<?, ?> tuple = (Tuple<?, ?>) o;

    if (!x.equals(tuple.x)) return false;
    return y.equals(tuple.y);

  }

  @Override
  public int hashCode() {
    int result = x.hashCode();
    result = 31 * result + y.hashCode();
    return result;
  }

}
