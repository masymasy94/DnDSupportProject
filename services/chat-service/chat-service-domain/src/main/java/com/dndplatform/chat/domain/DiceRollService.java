package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.DiceRollResult;

public interface DiceRollService {
    DiceRollResult roll(String formula);
}
