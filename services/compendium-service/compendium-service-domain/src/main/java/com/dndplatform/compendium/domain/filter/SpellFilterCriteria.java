package com.dndplatform.compendium.domain.filter;

import com.dndplatform.common.filter.FilterCriteria;
import com.dndplatform.common.filter.Param;

import java.util.List;

import static com.dndplatform.common.filter.Param.of;

public record SpellFilterCriteria(
        Integer level,
        String school,
        Boolean concentration,
        Boolean ritual
) implements FilterCriteria {

    @Override
    public List<Param> toParams() {
        return List.of(
                of("level", level),
                of("spellSchool.name", "school", school),
                of("concentration", concentration),
                of("ritual", ritual)
        );
    }
}
