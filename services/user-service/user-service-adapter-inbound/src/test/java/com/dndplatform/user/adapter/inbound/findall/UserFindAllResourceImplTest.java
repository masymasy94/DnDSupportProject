package com.dndplatform.user.adapter.inbound.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserFindAllResource;
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
class UserFindAllResourceImplTest {

    @Mock
    private UserFindAllResource delegate;

    private UserFindAllResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindAllResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindAll(@Random PagedUserViewModel expected) {
        int page = 0;
        int size = 20;
        given(delegate.findAll(page, size)).willReturn(expected);

        var result = sut.findAll(page, size);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(page, size);
        inOrder.verifyNoMoreInteractions();
    }
}
