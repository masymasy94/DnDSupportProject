package com.dndplatform.notificationservice.adapter.inbound.send;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.notificationservice.view.model.SendEmailResource;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SyncSendEmailResourceImplTest {

    @Mock
    private SendEmailResource delegate;

    @Mock
    private Response mockResponse;

    private SyncSendEmailResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SyncSendEmailResourceImpl(delegate);
    }

    @Test
    void shouldDelegateSyncSend(@Random EmailSendRequestViewModel request) {
        given(delegate.syncSend(request)).willReturn(mockResponse);

        var result = sut.syncSend(request);

        assertThat(result).isEqualTo(mockResponse);
        then(delegate).should().syncSend(request);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnDelegateResponse(@Random EmailSendRequestViewModel request) {
        given(delegate.syncSend(request)).willReturn(mockResponse);

        var result = sut.syncSend(request);

        assertThat(result).isSameAs(mockResponse);
    }
}
