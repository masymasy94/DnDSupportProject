package com.dndplatform.campaign.adapter.inbound.create;

import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
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
class CampaignCreateResourceImplTest {

    @Mock
    private CampaignCreateDelegate delegate;

    private CampaignCreateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignCreateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCreate(@Random CreateCampaignRequest request,
                              @Random CampaignViewModel expected) {
        given(delegate.create(request)).willReturn(expected);

        CampaignViewModel result = sut.create(request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).create(request);
        inOrder.verifyNoMoreInteractions();
    }
}
