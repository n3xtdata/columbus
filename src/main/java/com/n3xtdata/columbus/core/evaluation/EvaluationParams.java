package com.n3xtdata.columbus.core.evaluation;

import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;

public interface EvaluationParams {

  Status evaluate(ExecutionRuns runs) throws EvaluationException;

}
