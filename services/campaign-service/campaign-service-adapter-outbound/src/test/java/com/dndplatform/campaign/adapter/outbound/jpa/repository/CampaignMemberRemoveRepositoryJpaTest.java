package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CampaignMemberRemoveRepositoryJpaTest {

    @Mock
    private CampaignMemberPanacheRepository panacheRepository;

    private CampaignMemberRemoveRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignMemberRemoveRepositoryJpa(panacheRepository);
    }

    @Test
    void shouldRemoveMemberSuccessfully() {
        Long campaignId = 1L;
        Long userId = 2L;
        given(panacheRepository.deleteByCampaignIdAndUserId(campaignId, userId)).willReturn(1L);

        sut.remove(campaignId, userId);

        then(panacheRepository).should().deleteByCampaignIdAndUserId(campaignId, userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMemberDoesNotExist() {
        Long campaignId = 1L;
        Long userId = 99L;
        given(panacheRepository.deleteByCampaignIdAndUserId(campaignId, userId)).willReturn(0L);

        assertThatThrownBy(() -> sut.remove(campaignId, userId))
                .isInstanceOf(NotFoundException.class);

        then(panacheRepository).should().deleteByCampaignIdAndUserId(campaignId, userId);
    }
}
