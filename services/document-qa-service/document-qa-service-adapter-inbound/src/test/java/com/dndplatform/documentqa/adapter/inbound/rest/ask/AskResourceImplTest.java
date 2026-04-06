package com.dndplatform.documentqa.adapter.inbound.rest.ask;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.view.model.vm.AskRequest;
import com.dndplatform.documentqa.view.model.vm.QuestionAnswerViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class AskResourceImplTest {

    @Mock
    private AskDelegate delegate;

    private AskResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new AskResourceImpl(delegate);
    }

    @Test
    void shouldDelegateToDelegate(@Random AskRequest request,
                                  @Random QuestionAnswerViewModel expected) {
        given(delegate.ask(request)).willReturn(expected);

        QuestionAnswerViewModel result = sut.ask(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).ask(request);
        inOrder.verifyNoMoreInteractions();
    }
}
