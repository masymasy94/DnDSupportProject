package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Alignment;

import java.util.List;

public interface AlignmentFindAllRepository {
    List<Alignment> findAllAlignment();
}
