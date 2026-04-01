package com.dndplatform.chat.domain.model;

import com.dndplatform.common.annotations.Builder;
import java.util.List;

@Builder
public record DiceRollResult(
        String formula,
        List<DiceGroup> groups,
        int modifier,
        int total
) {}
