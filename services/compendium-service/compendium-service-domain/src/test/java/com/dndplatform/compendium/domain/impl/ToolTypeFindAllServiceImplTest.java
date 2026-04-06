package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.ToolTypeFindAllService;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ToolTypeFindAllServiceImplTest {

    @Mock
    private ToolTypeFindAllRepository repository;

    private ToolTypeFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new ToolTypeFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random ToolType toolType) {
        String category = "ARTISAN";
        List<ToolType> expected = List.of(toolType);
        given(repository.findAllToolTypes(category)).willReturn(expected);

        List<ToolType> result = sut.findAll(category);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllToolTypes(category);
        inOrder.verifyNoMoreInteractions();
    }
}
