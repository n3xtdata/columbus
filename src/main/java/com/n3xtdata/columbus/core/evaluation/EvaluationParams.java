package com.n3xtdata.columbus.core.evaluation;

import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;

interface EvaluationParams {

  Status evaluate(ExecutionRuns runs) throws EvaluationException;

}
