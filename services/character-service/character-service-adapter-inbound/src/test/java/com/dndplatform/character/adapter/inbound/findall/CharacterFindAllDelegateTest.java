package com.dndplatform.character.adapter.inbound.findall;

import com.dndplatform.character.adapter.inbound.findall.mapper.CharacterSummaryViewModelMapper;
import com.dndplatform.character.domain.CharacterFindAllService;
import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.view.model.vm.CharacterSummaryViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterFindAllDelegateTest {

    @Mock
    private CharacterFindAllService service;

    @Mock
    private CharacterSummaryViewModelMapper mapper;

    private CharacterFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterFindAllDelegate(service, mapper);
    }

    @Test
    void shouldFindAllCharactersWithPagination(
            @Random PagedResult pagedResult,
            @Random CharacterSummaryViewModel summaryViewModel
    ) {
        var page = pagedResult.page();
        var size = pagedResult.size();

        given(service.findAll(page, size)).willReturn(pagedResult);
        given(mapper.apply(any())).willReturn(summaryViewModel);

        var result = sut.findAll(page, size);

        assertThat(result.page()).isEqualTo(pagedResult.page());
        assertThat(result.size()).isEqualTo(pagedResult.size());
        assertThat(result.totalElements()).isEqualTo(pagedResult.totalElements());
        assertThat(result.totalPages()).isEqualTo(pagedResult.totalPages());
        assertThat(result.content()).hasSize(pagedResult.content().size());

        then(service).should().findAll(page, size);
        then(mapper).should(org.mockito.Mockito.times(pagedResult.content().size())).apply(any());
    }

    @Test
    void shouldReturnEmptyPagedResultWhenNoCharacters(@Random PagedResult pagedResult) {
        var emptyResult = new PagedResult(List.of(), pagedResult.page(), pagedResult.size(), 0L, 0);
        var page = emptyResult.page();
        var size = emptyResult.size();

        given(service.findAll(page, size)).willReturn(emptyResult);

        var result = sut.findAll(page, size);

        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);
        assertThat(result.totalElements()).isEqualTo(0L);
        assertThat(result.content()).isEmpty();

        then(service).should().findAll(page, size);
        then(mapper).shouldHaveNoInteractions();
    }
}
