package com.skywalker.utils;

import java.util.List;

/**
 * @author caonn@mediav.com
 * @version 16/6/23.
 */
public class ListUtils {
  public static int [] toArray(List<Integer> integers) {
    int [] res = new int[integers.size()];
    for(int i = 0; i < integers.size(); i++) {
      res[i] = integers.get(i);
    }
    return res;
  }
}
