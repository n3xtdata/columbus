package com.n3xtdata.columbus.core.evaluation;

import com.n3xtdata.columbus.utils.Params;

public class EvaluationParamsFactory {

  public static EvaluationParams build(EvaluationType type, Params params) {
    switch (type) {
      case CUSTOM:
        return new CustomEvaluationParams(params);
      default:
        return null;
    }
  }
}