package com.dndplatform.documentqa.adapter.inbound.rest.ask;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.QuestionAnswerService;
import com.dndplatform.documentqa.domain.model.QuestionAnswer;
import com.dndplatform.documentqa.domain.model.QuestionRequest;
import com.dndplatform.documentqa.view.model.vm.AskRequest;
import com.dndplatform.documentqa.view.model.vm.QuestionAnswerViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class AskDelegateTest {

    @Mock
    private QuestionAnswerService service;

    private AskDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new AskDelegate(service);
    }

    @Test
    void shouldDelegateToService(@Random Long userId, @Random Long conversationId) {
        AskRequest request = new AskRequest(userId, conversationId, "What is the AC of a goblin?", List.of("doc-1"));
        QuestionAnswer answer = new QuestionAnswer(conversationId, "AC 15", List.of());

        given(service.ask(any(QuestionRequest.class))).willReturn(answer);

        QuestionAnswerViewModel result = sut.ask(request);

        assertThat(result).isNotNull();
        assertThat(result.answer()).isEqualTo("AC 15");
        assertThat(result.conversationId()).isEqualTo(conversationId);

        ArgumentCaptor<QuestionRequest> captor = ArgumentCaptor.forClass(QuestionRequest.class);
        then(service).should().ask(captor.capture());

        QuestionRequest captured = captor.getValue();
        assertThat(captured.userId()).isEqualTo(userId);
        assertThat(captured.question()).isEqualTo("What is the AC of a goblin?");
    }
}
