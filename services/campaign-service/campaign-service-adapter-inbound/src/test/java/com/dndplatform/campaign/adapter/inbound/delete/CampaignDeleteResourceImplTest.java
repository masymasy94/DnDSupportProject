package com.dndplatform.campaign.adapter.inbound.delete;

import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CampaignDeleteResourceImplTest {

    @Mock
    private CampaignDeleteDelegate delegate;

    private CampaignDeleteResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignDeleteResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDelete() {
        Long id = 5L;
        Long userId = 1L;
        willDoNothing().given(delegate).delete(id, userId);

        sut.delete(id, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).delete(id, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
