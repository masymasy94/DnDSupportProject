package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.*;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class DifficultyCalculateServiceImpl implements DifficultyCalculateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private static final int[] EASY =   {25,50,75,125,250,300,350,450,550,600,800,1000,1100,1250,1400,1600,2000,2100,2400,2800};
    private static final int[] MEDIUM = {50,100,150,250,500,600,750,900,1100,1200,1600,2000,2200,2500,2800,3200,3900,4200,4900,5700};
    private static final int[] HARD =   {75,150,225,375,750,900,1100,1400,1600,1900,2400,3000,3400,3800,4300,4800,5900,6300,7300,8500};
    private static final int[] DEADLY = {100,200,400,500,1100,1400,1700,2100,2400,2800,3600,4500,5100,5700,6400,7200,8800,9500,10900,12700};

    private static final Pattern XP_PATTERN = Pattern.compile("\"xp\"\\s*:\\s*\"?(\\d+)");

    private final EncounterFindByIdRepository findRepository;

    @Inject
    public DifficultyCalculateServiceImpl(EncounterFindByIdRepository findRepository) {
        this.findRepository = findRepository;
    }

    @Override
    public DifficultyResult calculate(Long encounterId) {
        log.info(() -> "Calculating difficulty for encounter %d".formatted(encounterId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        int partyLevel = encounter.partyLevel();
        int partySize = encounter.partySize();
        int levelIndex = Math.max(0, Math.min(partyLevel - 1, 19));

        Map<DifficultyRating, Integer> thresholds = new LinkedHashMap<>();
        thresholds.put(DifficultyRating.EASY, EASY[levelIndex] * partySize);
        thresholds.put(DifficultyRating.MEDIUM, MEDIUM[levelIndex] * partySize);
        thresholds.put(DifficultyRating.HARD, HARD[levelIndex] * partySize);
        thresholds.put(DifficultyRating.DEADLY, DEADLY[levelIndex] * partySize);

        int monsterCount = 0;
        int totalMonsterXp = 0;

        if (encounter.participants() != null) {
            for (EncounterParticipant p : encounter.participants()) {
                if (p.type() == ParticipantType.MONSTER) {
                    monsterCount++;
                    totalMonsterXp += extractXp(p.sourceJson());
                }
            }
        }

        double multiplier = getMultiplier(monsterCount);
        int adjustedXp = (int) (totalMonsterXp * multiplier);

        DifficultyRating rating;
        if (adjustedXp >= thresholds.get(DifficultyRating.DEADLY)) {
            rating = DifficultyRating.DEADLY;
        } else if (adjustedXp >= thresholds.get(DifficultyRating.HARD)) {
            rating = DifficultyRating.HARD;
        } else if (adjustedXp >= thresholds.get(DifficultyRating.MEDIUM)) {
            rating = DifficultyRating.MEDIUM;
        } else if (adjustedXp >= thresholds.get(DifficultyRating.EASY)) {
            rating = DifficultyRating.EASY;
        } else {
            rating = DifficultyRating.TRIVIAL;
        }

        DifficultyResult result = DifficultyResultBuilder.builder()
                .withRating(rating)
                .withTotalMonsterXp(totalMonsterXp)
                .withAdjustedXp(adjustedXp)
                .withPartyXpThresholds(thresholds)
                .withPartyLevel(partyLevel)
                .withPartySize(partySize)
                .build();

        log.info(() -> "Difficulty for encounter %d: %s (adjusted XP: %d)".formatted(encounterId, rating, adjustedXp));
        return result;
    }

    private int extractXp(String sourceJson) {
        if (sourceJson == null || sourceJson.isBlank()) return 0;
        Matcher matcher = XP_PATTERN.matcher(sourceJson);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private double getMultiplier(int monsterCount) {
        if (monsterCount <= 0) return 1.0;
        if (monsterCount == 1) return 1.0;
        if (monsterCount == 2) return 1.5;
        if (monsterCount <= 6) return 2.0;
        if (monsterCount <= 10) return 2.5;
        if (monsterCount <= 14) return 3.0;
        return 4.0;
    }
}
