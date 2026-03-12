package com.dndplatform.documentqa.view.model;

import com.dndplatform.documentqa.view.model.vm.AskRequest;
import com.dndplatform.documentqa.view.model.vm.QuestionAnswerViewModel;
import jakarta.validation.Valid;

public interface AskResource {

    QuestionAnswerViewModel ask(@Valid AskRequest request);
}
