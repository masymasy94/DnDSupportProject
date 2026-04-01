package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.DifficultyRating;
import com.dndplatform.combat.domain.model.DifficultyResult;
import com.dndplatform.combat.view.model.vm.DifficultyResultViewModel;
import com.dndplatform.combat.view.model.vm.DifficultyResultViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class DifficultyResultViewModelMapper implements Function<DifficultyResult, DifficultyResultViewModel> {

    @Override
    public DifficultyResultViewModel apply(DifficultyResult result) {
        Map<String, Integer> thresholds = new LinkedHashMap<>();
        if (result.partyXpThresholds() != null) {
            for (Map.Entry<DifficultyRating, Integer> entry : result.partyXpThresholds().entrySet()) {
                thresholds.put(entry.getKey().name(), entry.getValue());
            }
        }

        return DifficultyResultViewModelBuilder.builder()
                .withRating(result.rating() != null ? result.rating().name() : null)
                .withTotalMonsterXp(result.totalMonsterXp())
                .withAdjustedXp(result.adjustedXp())
                .withPartyXpThresholds(thresholds)
                .withPartyLevel(result.partyLevel())
                .withPartySize(result.partySize())
                .build();
    }
}
