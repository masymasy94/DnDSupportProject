package com.dndplatform.user.adapter.inbound.search;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserSearchResource;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
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
class UserSearchResourceImplTest {

    @Mock
    private UserSearchResource delegate;

    private UserSearchResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserSearchResourceImpl(delegate);
    }

    @Test
    void shouldDelegateSearch(@Random PagedUserViewModel expected) {
        String query = "gandalf";
        int page = 0;
        int size = 20;
        given(delegate.search(query, page, size)).willReturn(expected);

        var result = sut.search(query, page, size);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).search(query, page, size);
        inOrder.verifyNoMoreInteractions();
    }
}
