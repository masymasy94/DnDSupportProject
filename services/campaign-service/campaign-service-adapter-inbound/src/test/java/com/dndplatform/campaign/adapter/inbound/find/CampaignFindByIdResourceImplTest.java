package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
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
class CampaignFindByIdResourceImplTest {

    @Mock
    private CampaignFindByIdDelegate delegate;

    private CampaignFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindByIdResourceImpl(delegate);
    }

    @Test
    void shouldDelegateFindById(@Random CampaignViewModel expected) {
        Long id = 7L;
        given(delegate.findById(id)).willReturn(expected);

        CampaignViewModel result = sut.findById(id);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}
