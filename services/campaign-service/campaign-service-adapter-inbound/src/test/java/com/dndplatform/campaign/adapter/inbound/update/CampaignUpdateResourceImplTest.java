package com.dndplatform.campaign.adapter.inbound.update;

import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
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
class CampaignUpdateResourceImplTest {

    @Mock
    private CampaignUpdateDelegate delegate;

    private CampaignUpdateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignUpdateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdate(@Random UpdateCampaignRequest request,
                              @Random CampaignViewModel expected) {
        Long id = 3L;
        given(delegate.update(id, request)).willReturn(expected);

        CampaignViewModel result = sut.update(id, request);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).update(id, request);
        inOrder.verifyNoMoreInteractions();
    }
}
