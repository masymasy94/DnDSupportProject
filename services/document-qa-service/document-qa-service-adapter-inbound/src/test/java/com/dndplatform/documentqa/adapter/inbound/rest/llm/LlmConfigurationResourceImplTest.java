package com.dndplatform.documentqa.adapter.inbound.rest.llm;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.view.model.vm.CreateLlmConfigurationRequest;
import com.dndplatform.documentqa.view.model.vm.LlmConfigurationViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class LlmConfigurationResourceImplTest {

    @Mock
    private LlmConfigurationDelegate delegate;

    private LlmConfigurationResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new LlmConfigurationResourceImpl(delegate);
    }

    @Test
    void shouldDelegateListSystemConfigurations(@Random LlmConfigurationViewModel config1,
                                                @Random LlmConfigurationViewModel config2) {
        List<LlmConfigurationViewModel> expected = List.of(config1, config2);
        given(delegate.listSystemConfigurations()).willReturn(expected);

        List<LlmConfigurationViewModel> result = sut.listSystemConfigurations();

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).listSystemConfigurations();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateCreateSystemConfiguration(@Random CreateLlmConfigurationRequest request,
                                                  @Random LlmConfigurationViewModel expected) {
        given(delegate.createSystemConfiguration(request)).willReturn(expected);

        LlmConfigurationViewModel result = sut.createSystemConfiguration(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).createSystemConfiguration(request);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateActivateSystemConfiguration() {
        Long id = 5L;
        willDoNothing().given(delegate).activateSystemConfiguration(id);

        sut.activateSystemConfiguration(id);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).activateSystemConfiguration(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateDeleteSystemConfiguration() {
        Long id = 5L;
        willDoNothing().given(delegate).deleteSystemConfiguration(id);

        sut.deleteSystemConfiguration(id);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).deleteSystemConfiguration(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateListUserConfigurations(@Random LlmConfigurationViewModel config1,
                                              @Random LlmConfigurationViewModel config2) {
        Long userId = 1L;
        List<LlmConfigurationViewModel> expected = List.of(config1, config2);
        given(delegate.listUserConfigurations(userId)).willReturn(expected);

        List<LlmConfigurationViewModel> result = sut.listUserConfigurations(userId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).listUserConfigurations(userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateCreateUserConfiguration(@Random CreateLlmConfigurationRequest request,
                                               @Random LlmConfigurationViewModel expected) {
        given(delegate.createUserConfiguration(request)).willReturn(expected);

        LlmConfigurationViewModel result = sut.createUserConfiguration(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).createUserConfiguration(request);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateActivateUserConfiguration() {
        Long id = 5L;
        Long userId = 1L;
        willDoNothing().given(delegate).activateUserConfiguration(id, userId);

        sut.activateUserConfiguration(id, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).activateUserConfiguration(id, userId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateDeleteUserConfiguration() {
        Long id = 5L;
        Long userId = 1L;
        willDoNothing().given(delegate).deleteUserConfiguration(id, userId);

        sut.deleteUserConfiguration(id, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).deleteUserConfiguration(id, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
