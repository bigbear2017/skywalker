package com.skywalker.utils;

/**
 *  Option parser
 * @author caonn@mediav.com
 * @version 16/6/21.
 */
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class OptionParser {
  public OptionParser() {
  }

  public static <T> T parse(T bean, String... args) {
    CmdLineParser parser = new CmdLineParser(bean);

    try {
      parser.parseArgument(args);
      return bean;
    } catch (CmdLineException var4) {
      parser.printUsage(System.err);
      throw new IllegalArgumentException("Parse arguments failed", var4);
    }
  }
}
