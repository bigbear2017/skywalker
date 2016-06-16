package com.skywalker.yarn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;

/**
 * A simple Yarn client class
 * @author caonn@mediav.com
 * @version 16/6/16.
 */
public class Client {
  public void init(Configuration conf) throws YarnException, IOException {
    YarnClient yarnClient = YarnClient.createYarnClient();
    yarnClient.init(conf);
    yarnClient.start();

    YarnClientApplication app = yarnClient.createApplication();
    GetNewApplicationResponse appResponse =   app.getNewApplicationResponse();

    //set the application submission context
    ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
    ApplicationId appId = appContext.getApplicationId();

    Boolean keepContainers = true;
    String name = "";
    appContext.setKeepContainersAcrossApplicationAttempts(keepContainers);
    appContext.setApplicationName(name);
  }
}
