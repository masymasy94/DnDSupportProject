package com.dndplatform.compendium.adapter.inbound.feats.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.feats.findall.mapper.FeatViewModelMapper;
import com.dndplatform.compendium.domain.FeatFindByIdService;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
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
class FeatFindByIdDelegateTest {

    @Mock
    private FeatFindByIdService service;

    @Mock
    private FeatViewModelMapper mapper;

    private FeatFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new FeatFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Feat feat, @Random FeatViewModel expected) {
        given(service.findById(feat.id())).willReturn(feat);
        given(mapper.apply(feat)).willReturn(expected);

        FeatViewModel result = sut.findById(feat.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(feat.id());
        then(mapper).should(inOrder).apply(feat);
        inOrder.verifyNoMoreInteractions();
    }
}
