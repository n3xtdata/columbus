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

package com.n3xtdata.columbus.expressionlanguage;

import static org.junit.Assert.assertEquals;

import com.n3xtdata.columbus.core.evaluation.Status;
import org.junit.Test;

public class StatusTests {

  @Test
  public void shouldReturnTechnicalErrorWhenNotFound() {
    String status = "WHATEVER";
    assertEquals(Status.TECHNICAL_ERROR, Status.fromString(status));
  }

  @Test
  public void shouldReturnEnumWhenFound() {
    String status = "success";
    assertEquals(Status.SUCCESS, Status.fromString(status));
  }

}
