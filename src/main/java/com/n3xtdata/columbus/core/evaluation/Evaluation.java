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

package com.n3xtdata.columbus.core.evaluation;

import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import com.n3xtdata.columbus.utils.Params;

public class Evaluation {

  private EvaluationType type;

  private Params params;

  private EvaluationParams evaluationParams;

  public Evaluation() {
  }

  public EvaluationType getType() {
    return type;
  }

  public void setType(EvaluationType type) {
    this.type = type;
  }

  public Params getParams() {
    return params;
  }

  public void setParams(Params params) {
    this.params = params;
  }

  public void init() {
    this.evaluationParams = EvaluationParamsFactory.build(type, params);
  }

  public Status evaluate(ExecutionRuns runs) throws EvaluationException {
    return this.evaluationParams.evaluate(runs);
  }

  @Override
  public String toString() {
    return "Evaluation{" +
        "type=" + type +
        ", evaluationParams=" + evaluationParams +
        '}';
  }


}
