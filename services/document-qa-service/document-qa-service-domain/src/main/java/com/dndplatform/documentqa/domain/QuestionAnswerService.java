package com.dndplatform.documentqa.domain;

import com.dndplatform.documentqa.domain.model.QuestionAnswer;
import com.dndplatform.documentqa.domain.model.QuestionRequest;

public interface QuestionAnswerService {

    QuestionAnswer ask(QuestionRequest request);
}
