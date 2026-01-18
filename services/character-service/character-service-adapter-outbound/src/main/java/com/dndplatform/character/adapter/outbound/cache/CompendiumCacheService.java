package com.dndplatform.character.adapter.outbound.cache;

import com.dndplatform.compendium.client.characterclass.CharacterClassFindByIdResourceRestClient;
import com.dndplatform.compendium.client.species.SpeciesFindByIdResourceRestClient;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CompendiumCacheService {

    private static final String SPECIES_KEY_PREFIX = "compendium:species:";
    private static final String CLASS_KEY_PREFIX = "compendium:class:";

    private final Logger log = Logger.getLogger(getClass().getName());

    private final RedisDataSource redisDataSource;
    private final SpeciesFindByIdResourceRestClient speciesClient;
    private final CharacterClassFindByIdResourceRestClient classClient;

    @ConfigProperty(name = "compendium.cache.ttl", defaultValue = "3600")
    int cacheTtlSeconds;

    private ValueCommands<String, String> valueCommands;

    @Inject
    public CompendiumCacheService(
            RedisDataSource redisDataSource,
            @RestClient SpeciesFindByIdResourceRestClient speciesClient,
            @RestClient CharacterClassFindByIdResourceRestClient classClient) {
        this.redisDataSource = redisDataSource;
        this.speciesClient = speciesClient;
        this.classClient = classClient;
    }

    @PostConstruct
    void init() {
        this.valueCommands = redisDataSource.value(String.class);
    }

    public Optional<String> getSpeciesName(int speciesId) {
        String key = SPECIES_KEY_PREFIX + speciesId;

        // Try cache first
        String cachedName = valueCommands.get(key);
        if (cachedName != null) {
            log.fine(() -> "Cache hit for species " + speciesId);
            return Optional.of(cachedName);
        }

        // Fetch from compendium service
        try {
            SpeciesViewModel species = speciesClient.findById(speciesId);
            if (species != null && species.name() != null) {
                valueCommands.setex(key, cacheTtlSeconds, species.name());
                log.info(() -> "Cached species " + speciesId + ": " + species.name());
                return Optional.of(species.name());
            }
        } catch (Exception e) {
            log.warning(() -> "Failed to fetch species " + speciesId + " from compendium: " + e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<String> getClassName(int classId) {
        String key = CLASS_KEY_PREFIX + classId;

        // Try cache first
        String cachedName = valueCommands.get(key);
        if (cachedName != null) {
            log.fine(() -> "Cache hit for class " + classId);
            return Optional.of(cachedName);
        }

        // Fetch from compendium service
        try {
            CharacterClassViewModel characterClass = classClient.findById(classId);
            if (characterClass != null && characterClass.name() != null) {
                valueCommands.setex(key, cacheTtlSeconds, characterClass.name());
                log.info(() -> "Cached class " + classId + ": " + characterClass.name());
                return Optional.of(characterClass.name());
            }
        } catch (Exception e) {
            log.warning(() -> "Failed to fetch class " + classId + " from compendium: " + e.getMessage());
        }

        return Optional.empty();
    }

    public void invalidateSpecies(int speciesId) {
        String key = SPECIES_KEY_PREFIX + speciesId;
        valueCommands.getdel(key);
        log.info(() -> "Invalidated cache for species " + speciesId);
    }

    public void invalidateClass(int classId) {
        String key = CLASS_KEY_PREFIX + classId;
        valueCommands.getdel(key);
        log.info(() -> "Invalidated cache for class " + classId);
    }
}
