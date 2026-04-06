package com.dndplatform.user.adapter.inbound.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserFindByIdResource;
import com.dndplatform.user.view.model.vm.UserViewModel;
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
class UserFindByIdResourceImplTest {

    @Mock
    private UserFindByIdResource delegate;

    private UserFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindById(@Random UserViewModel expected) {
        long id = 1L;
        given(delegate.findById(id)).willReturn(expected);

        var result = sut.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
