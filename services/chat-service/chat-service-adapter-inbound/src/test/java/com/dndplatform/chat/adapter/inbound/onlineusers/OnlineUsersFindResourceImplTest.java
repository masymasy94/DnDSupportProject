package com.dndplatform.chat.adapter.inbound.onlineusers;

import com.dndplatform.chat.view.model.OnlineUsersFindResource;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class OnlineUsersFindResourceImplTest {

    @Mock
    private OnlineUsersFindResource delegate;

    private OnlineUsersFindResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new OnlineUsersFindResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindOnlineUsers(@Random Long userId1, @Random Long userId2) {
        var expected = Set.of(userId1, userId2);
        given(delegate.findOnlineUsers()).willReturn(expected);

        var result = sut.findOnlineUsers();

        assertThat(result).isEqualTo(expected);
        then(delegate).should().findOnlineUsers();
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
