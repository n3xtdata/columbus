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
