package com.dndplatform.compendium.adapter.inbound.feats.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.feats.findall.mapper.FeatViewModelMapper;
import com.dndplatform.compendium.domain.FeatFindAllService;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class FeatFindAllDelegateTest {

    @Mock
    private FeatFindAllService service;

    @Mock
    private FeatViewModelMapper mapper;

    private FeatFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new FeatFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Feat f1, @Random Feat f2,
                                 @Random FeatViewModel vm1, @Random FeatViewModel vm2) {
        given(service.findAll()).willReturn(List.of(f1, f2));
        given(mapper.apply(f1)).willReturn(vm1);
        given(mapper.apply(f2)).willReturn(vm2);

        List<FeatViewModel> result = sut.findAll();

        assertThat(result).containsExactly(vm1, vm2);
        then(service).should().findAll();
        then(mapper).should().apply(f1);
        then(mapper).should().apply(f2);
    }
}
