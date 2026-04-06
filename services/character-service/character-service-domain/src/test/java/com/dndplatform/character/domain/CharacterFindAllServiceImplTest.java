package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.domain.repository.CharacterFindAllRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterFindAllServiceImplTest {

    @Mock
    private CharacterFindAllRepository repository;

    private CharacterFindAllServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterFindAllServiceImpl(repository);
    }

    @Test
    void shouldDelegateFindAllToRepository(@Random PagedResult expected) {
        given(repository.findAll(0, 10)).willReturn(expected);

        var result = sut.findAll(0, 10);

        assertThat(result).isEqualTo(expected);
        then(repository).should().findAll(0, 10);
    }
}
