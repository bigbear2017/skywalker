package com.skywalker.utils;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author caonn@mediav.com
 * @version 16/6/21.
 */
public class MapUtils {
  public static <T1> void incrementMap(Map<T1, Integer> map, T1 key, Integer inc) {
    Integer value = map.get(key);
    if( value == null ) {
      map.put(key, inc);
      return;
    }
    map.put(key, value+inc);
  }

  public static <T1>  List<Map.Entry<T1, Integer>> sortMapByValue(Map<T1, Integer> map) {
    List<Map.Entry<T1, Integer>> entries = Lists.newArrayList(map.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<T1, Integer>>() {
      public int compare(Map.Entry<T1, Integer> o1, Map.Entry<T1, Integer> o2) {
        return o1.getValue().compareTo(o2.getValue());
      }
    });
    return entries;
  }
}
