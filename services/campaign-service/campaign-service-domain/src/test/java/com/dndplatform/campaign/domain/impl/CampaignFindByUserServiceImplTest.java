package com.dndplatform.campaign.domain.impl;

import com.dndplatform.campaign.domain.CampaignFindByUserService;
import com.dndplatform.campaign.domain.CampaignFindByUserServiceImpl;
import com.dndplatform.campaign.domain.model.PagedResult;
import com.dndplatform.campaign.domain.repository.CampaignFindByUserRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class CampaignFindByUserServiceImplTest {

    @Mock
    private CampaignFindByUserRepository repository;

    private CampaignFindByUserService sut;

    @BeforeEach
    void setUp() {
        sut = new CampaignFindByUserServiceImpl(repository);
    }

    @Test
    void shouldReturnPagedResultForUser(@Random PagedResult expected,
                                        @Random Long userId) {
        int page = 0;
        int size = 10;
        given(repository.findByUserId(userId, page, size)).willReturn(expected);

        PagedResult result = sut.findByUserId(userId, page, size);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findByUserId(userId, page, size);
    }
}
