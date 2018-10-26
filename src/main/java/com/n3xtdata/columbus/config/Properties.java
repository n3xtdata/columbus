/*
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.n3xtdata.columbus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "com.n3xtdata.columbus")
public class Properties {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String home;

  private Notification notification;

  public String getHome() {

    return home;
  }

  public void setHome(String home) {

    if (home.endsWith("/")) {
      home = home.substring(0, this.home.length() - 1);
    }
    logger.info("Columbus home directory set: " + home);
    this.home = home;
  }

  public Notification getNotification() {
    return notification;
  }

  public void setNotification(Notification notification) {
    this.notification = notification;
  }

  public static class Notification {

    private Mail mail;

    public Mail getMail() {
      return mail;
    }

    public void setMail(Mail mail) {
      this.mail = mail;
    }
  }

  public static class Mail {

    private String sender;

    public String getSender() {
      return sender;
    }

    public void setSender(String sender) {
      this.sender = sender;
    }
  }


}
