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

package com.n3xtdata.columbus.executor;

import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.data.MetadataService;
import com.n3xtdata.columbus.evaluation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final MetadataService metadataService;

  @Autowired
  public ExecutionServiceImpl(MetadataService metadataService) {

    this.metadataService = metadataService;
  }

  @Override
  public Status execute(String checkLabel) throws Exception {

    Check check = this.metadataService.getCheckByLabel(checkLabel);

    return check.execute();
  }
}