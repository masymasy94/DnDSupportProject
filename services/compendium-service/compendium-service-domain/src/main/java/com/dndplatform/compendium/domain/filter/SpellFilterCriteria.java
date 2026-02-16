package com.dndplatform.compendium.domain.filter;

import com.dndplatform.common.filter.FilterCriteria;
import com.dndplatform.common.filter.Param;

import java.util.List;

import static com.dndplatform.common.filter.Param.in;
import static com.dndplatform.common.filter.Param.likeAny;
import static com.dndplatform.common.filter.Param.of;

public record SpellFilterCriteria(
        String search,
        List<Integer> levels,
        List<String> schools,
        Boolean concentration,
        Boolean ritual,
        int page,
        int pageSize
) implements FilterCriteria {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 50;

    public SpellFilterCriteria {
        if (page < 0) page = DEFAULT_PAGE;
        if (pageSize <= 0) pageSize = DEFAULT_PAGE_SIZE;
    }

    @Override
    public List<Param> toParams() {
        return List.of(
                likeAny("name,description", "search", search),
                in("level", "levels", levels),
                in("spellSchool.name", "schools", schools),
                of("concentration", concentration),
                of("ritual", ritual)
        );
    }
}
