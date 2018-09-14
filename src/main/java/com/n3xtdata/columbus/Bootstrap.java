/**
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.n3xtdata.columbus;

import com.n3xtdata.columbus.data.MetadataService;
import com.n3xtdata.columbus.executor.ExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

  private MetadataService metadataService;

  @Autowired
  public Bootstrap(MetadataService metadataService)  {
    this.metadataService = metadataService;
  }

  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    try {
      this.metadataService.loadAll();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
