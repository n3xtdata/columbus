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

package com.n3xtdata.columbus.notifications.mail;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  public JavaMailSender emailSender;

  public void sendSimpleMail(Collection<String> recipients, String subject, String text, String from) {

    String[] list = recipients.toArray(new String[recipients.size()]);
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(list);
      message.setSubject(subject);
      message.setText(text);
      message.setFrom(from);
      emailSender.send(message);
    } catch (MailException e) {
      logger.error("Could not send mail to " + recipients);
      logger.debug(e.getMessage());
    }
  }
}
