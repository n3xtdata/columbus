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

package com.n3xtdata.columbus.notifications;

import com.n3xtdata.columbus.config.Properties;
import com.n3xtdata.columbus.notifications.mail.EmailService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private final static String SUBJECT = "Columbus Notification";
  private final EmailService emailService;
  private final Properties properties;

  @Autowired
  public NotificationServiceImpl(EmailService emailService, Properties properties) {
    this.emailService = emailService;
    this.properties = properties;
  }

  @Override
  public void sendNotification(Set<String> recipients, String text) {
    emailService.sendSimpleMail(recipients
        , SUBJECT
        , text
        , this.properties.getNotification().getMail().getSender());
  }
}
