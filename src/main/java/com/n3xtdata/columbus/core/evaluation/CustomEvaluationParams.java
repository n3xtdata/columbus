package com.n3xtdata.columbus.core.evaluation;

import com.n3xtdata.columbus.utils.Params;
import com.n3xtdata.columbus.expressionlanguage.RuleParser;
import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.util.List;

public class CustomEvaluationParams implements EvaluationParams {

  private List<String> rules;

  public CustomEvaluationParams(Params params) {
    this.rules = (List<String>) params.get("rules");
  }

  public List<String> getRules() {
    return rules;
  }

  public void setRules(List<String> rules) {
    this.rules = rules;
  }

  @Override
  public Status evaluate(ExecutionRuns runs) throws EvaluationException {

    RuleParser ruleParser = new RuleParser();
    for (String rule : rules) {
      String status = ruleParser.parseRule(rule, runs);
      if (status != null) {
        return Status.contains(status);
      }
    }

    throw new EvaluationException("No rules applicable");
  }

  @Override
  public String toString() {
    return "CustomEvaluationParams{" +
        "rules='" + rules + '\'' +
        '}';
  }
}
