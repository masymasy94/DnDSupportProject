package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.view.model.vm.PagedCampaignsViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
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
class CampaignFindAllResourceImplTest {

    @Mock
    private CampaignFindAllDelegate delegate;

    private CampaignFindAllResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindAllResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindAll(@Random PagedCampaignsViewModel expected) {
        Long userId = 42L;
        int page = 0;
        int size = 20;
        given(delegate.findAll(userId, page, size)).willReturn(expected);

        PagedCampaignsViewModel result = sut.findAll(userId, page, size);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(userId, page, size);
        inOrder.verifyNoMoreInteractions();
    }
}
