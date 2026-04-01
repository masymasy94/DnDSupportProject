package com.dndplatform.chat.domain.model;

import com.dndplatform.common.annotations.Builder;
import java.util.List;

@Builder
public record DiceGroup(
        int count,
        int sides,
        List<Integer> rolls,
        int subtotal
) {}
