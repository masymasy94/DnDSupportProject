package com.dndplatform.compendium.domain.filter;

import com.dndplatform.common.filter.FilterCriteria;
import com.dndplatform.common.filter.Param;

import java.util.List;

import static com.dndplatform.common.filter.Param.of;

public record MonsterFilterCriteria(
        String name,
        String type,
        String size,
        String challengeRating,
        String alignment,
        int page,
        int pageSize
) implements FilterCriteria {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 50;

    public MonsterFilterCriteria {
        if (page < 0) page = DEFAULT_PAGE;
        if (pageSize <= 0) pageSize = DEFAULT_PAGE_SIZE;
    }

    @Override
    public List<Param> toParams() {
        return List.of(
                Param.like("name", name),
                of("monsterType.name", "type", type),
                of("monsterSize.name", "size", size),
                of("challengeRating", challengeRating),
                of("alignment", alignment)
        );
    }
}
