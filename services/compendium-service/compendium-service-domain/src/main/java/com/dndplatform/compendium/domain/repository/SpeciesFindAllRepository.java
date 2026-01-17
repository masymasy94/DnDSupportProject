package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Species;

import java.util.List;

public interface SpeciesFindAllRepository {
    List<Species> findAllSpecies();
}
