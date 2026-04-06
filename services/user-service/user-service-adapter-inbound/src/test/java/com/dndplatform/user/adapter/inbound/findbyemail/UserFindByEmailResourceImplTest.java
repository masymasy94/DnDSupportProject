package com.dndplatform.user.adapter.inbound.findbyemail;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.user.view.model.UserFindByEmailResource;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
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
class UserFindByEmailResourceImplTest {

    @Mock
    private UserFindByEmailResource delegate;

    private UserFindByEmailResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new UserFindByEmailResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindByEmail(@Random UserFindByEmailViewModel request,
                                   @Random UserViewModel expected) {
        given(delegate.findByEmail(request)).willReturn(expected);

        var result = sut.findByEmail(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findByEmail(request);
        inOrder.verifyNoMoreInteractions();
    }
}
