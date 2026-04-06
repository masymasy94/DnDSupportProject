package com.dndplatform.character.adapter.outbound.cache;

import com.dndplatform.compendium.client.characterclass.CharacterClassFindByIdResourceRestClient;
import com.dndplatform.compendium.client.species.SpeciesFindByIdResourceRestClient;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class CompendiumCacheServiceTest {

    @Mock
    private RedisDataSource redisDataSource;

    @Mock
    private SpeciesFindByIdResourceRestClient speciesClient;

    @Mock
    private CharacterClassFindByIdResourceRestClient classClient;

    @Mock
    @SuppressWarnings("unchecked")
    private ValueCommands<String, String> valueCommands;

    private CompendiumCacheService sut;

    @BeforeEach
    void setUp() {
        given(redisDataSource.value(String.class)).willReturn(valueCommands);
        sut = new CompendiumCacheService(redisDataSource, speciesClient, classClient, 3600);
        sut.init();
    }

    // ─── getSpeciesName ───────────────────────────────────────────────────────

    @Test
    void getSpeciesName_shouldReturnCachedValue_whenCacheHit() {
        given(valueCommands.get("compendium:species:1")).willReturn("Elf");

        Optional<String> result = sut.getSpeciesName(1);

        assertThat(result).contains("Elf");
        then(speciesClient).shouldHaveNoInteractions();
    }

    @Test
    void getSpeciesName_shouldNotWriteToCache_whenCacheHit() {
        given(valueCommands.get("compendium:species:3")).willReturn("Dwarf");

        sut.getSpeciesName(3);

        then(valueCommands).should().get("compendium:species:3");
        then(valueCommands).shouldHaveNoMoreInteractions();
    }

    @Test
    void getSpeciesName_shouldCallRestClient_whenCacheMiss() {
        given(valueCommands.get("compendium:species:2")).willReturn(null);
        SpeciesViewModel vm = new SpeciesViewModel(2, "Human", null, "OFFICIAL", true);
        given(speciesClient.findById(2)).willReturn(vm);

        Optional<String> result = sut.getSpeciesName(2);

        assertThat(result).contains("Human");
        then(speciesClient).should().findById(2);
    }

    @Test
    void getSpeciesName_shouldStoreInCache_whenCacheMissAndRestReturnsValue() {
        given(valueCommands.get("compendium:species:5")).willReturn(null);
        SpeciesViewModel vm = new SpeciesViewModel(5, "Halfling", null, "OFFICIAL", true);
        given(speciesClient.findById(5)).willReturn(vm);

        sut.getSpeciesName(5);

        then(valueCommands).should().setex(eq("compendium:species:5"), anyLong(), eq("Halfling"));
    }

    @Test
    void getSpeciesName_shouldReturnEmpty_whenRestClientThrows() {
        given(valueCommands.get("compendium:species:99")).willReturn(null);
        willThrow(new RuntimeException("compendium unavailable")).given(speciesClient).findById(99);

        Optional<String> result = sut.getSpeciesName(99);

        assertThat(result).isEmpty();
        then(valueCommands).should().get("compendium:species:99");
        then(valueCommands).shouldHaveNoMoreInteractions();
    }

    @Test
    void getSpeciesName_shouldReturnEmpty_whenRestClientReturnsNull() {
        given(valueCommands.get("compendium:species:7")).willReturn(null);
        given(speciesClient.findById(7)).willReturn(null);

        Optional<String> result = sut.getSpeciesName(7);

        assertThat(result).isEmpty();
        then(valueCommands).should().get(anyString());
        then(valueCommands).shouldHaveNoMoreInteractions();
    }

    @Test
    void getSpeciesName_shouldReturnEmpty_whenRestClientReturnsViewModelWithNullName() {
        given(valueCommands.get("compendium:species:8")).willReturn(null);
        SpeciesViewModel vm = new SpeciesViewModel(8, null, null, "OFFICIAL", true);
        given(speciesClient.findById(8)).willReturn(vm);

        Optional<String> result = sut.getSpeciesName(8);

        assertThat(result).isEmpty();
    }

    // ─── getClassName ─────────────────────────────────────────────────────────

    @Test
    void getClassName_shouldReturnCachedValue_whenCacheHit() {
        given(valueCommands.get("compendium:class:1")).willReturn("Fighter");

        Optional<String> result = sut.getClassName(1);

        assertThat(result).contains("Fighter");
        then(classClient).shouldHaveNoInteractions();
    }

    @Test
    void getClassName_shouldNotWriteToCache_whenCacheHit() {
        given(valueCommands.get("compendium:class:4")).willReturn("Rogue");

        sut.getClassName(4);

        then(valueCommands).should().get("compendium:class:4");
        then(valueCommands).shouldHaveNoMoreInteractions();
    }

    @Test
    void getClassName_shouldCallRestClient_whenCacheMiss() {
        given(valueCommands.get("compendium:class:2")).willReturn(null);
        CharacterClassViewModel vm = new CharacterClassViewModel(2, "Wizard", "d6", null, "OFFICIAL", true);
        given(classClient.findById(2)).willReturn(vm);

        Optional<String> result = sut.getClassName(2);

        assertThat(result).contains("Wizard");
        then(classClient).should().findById(2);
    }

    @Test
    void getClassName_shouldStoreInCache_whenCacheMissAndRestReturnsValue() {
        given(valueCommands.get("compendium:class:6")).willReturn(null);
        CharacterClassViewModel vm = new CharacterClassViewModel(6, "Paladin", "d10", null, "OFFICIAL", true);
        given(classClient.findById(6)).willReturn(vm);

        sut.getClassName(6);

        then(valueCommands).should().setex(eq("compendium:class:6"), anyLong(), eq("Paladin"));
    }

    @Test
    void getClassName_shouldReturnEmpty_whenRestClientThrows() {
        given(valueCommands.get("compendium:class:99")).willReturn(null);
        willThrow(new RuntimeException("compendium unavailable")).given(classClient).findById(99);

        Optional<String> result = sut.getClassName(99);

        assertThat(result).isEmpty();
        then(valueCommands).should().get("compendium:class:99");
        then(valueCommands).shouldHaveNoMoreInteractions();
    }

    @Test
    void getClassName_shouldReturnEmpty_whenRestClientReturnsNull() {
        given(valueCommands.get("compendium:class:10")).willReturn(null);
        given(classClient.findById(10)).willReturn(null);

        Optional<String> result = sut.getClassName(10);

        assertThat(result).isEmpty();
    }

    @Test
    void getClassName_shouldReturnEmpty_whenRestClientReturnsViewModelWithNullName() {
        given(valueCommands.get("compendium:class:11")).willReturn(null);
        CharacterClassViewModel vm = new CharacterClassViewModel(11, null, "d8", null, "OFFICIAL", true);
        given(classClient.findById(11)).willReturn(vm);

        Optional<String> result = sut.getClassName(11);

        assertThat(result).isEmpty();
    }

    // ─── invalidateSpecies ────────────────────────────────────────────────────

    @Test
    void invalidateSpecies_shouldCallGetdelWithCorrectKey() {
        sut.invalidateSpecies(3);

        then(valueCommands).should().getdel("compendium:species:3");
    }

    // ─── invalidateClass ──────────────────────────────────────────────────────

    @Test
    void invalidateClass_shouldCallGetdelWithCorrectKey() {
        sut.invalidateClass(7);

        then(valueCommands).should().getdel("compendium:class:7");
    }
}
