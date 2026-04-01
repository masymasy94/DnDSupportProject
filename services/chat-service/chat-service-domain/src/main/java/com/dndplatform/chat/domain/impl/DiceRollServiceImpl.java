package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.DiceRollService;
import com.dndplatform.chat.domain.model.DiceGroup;
import com.dndplatform.chat.domain.model.DiceGroupBuilder;
import com.dndplatform.chat.domain.model.DiceRollResult;
import com.dndplatform.chat.domain.model.DiceRollResultBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class DiceRollServiceImpl implements DiceRollService {

    private static final Pattern DICE_PATTERN = Pattern.compile("(\\d*d\\d+)");
    private static final Pattern MODIFIER_PATTERN = Pattern.compile("(?:^|(?<=\\d))([+-]\\d+)$");
    private static final Pattern TOKEN_PATTERN = Pattern.compile("(\\d*d\\d+|[+-]?\\d+)");

    @Override
    public DiceRollResult roll(String formula) {
        if (formula == null || formula.isBlank()) {
            throw new IllegalArgumentException("Dice formula cannot be empty");
        }

        String normalized = formula.trim().toLowerCase();

        List<DiceGroup> groups = new ArrayList<>();
        int modifier = 0;
        boolean foundDice = false;

        Matcher tokenMatcher = TOKEN_PATTERN.matcher(normalized);
        List<String> tokens = new ArrayList<>();
        while (tokenMatcher.find()) {
            tokens.add(tokenMatcher.group(1));
        }

        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Invalid dice formula: " + formula);
        }

        for (String token : tokens) {
            if (token.contains("d")) {
                foundDice = true;
                DiceGroup group = parseDiceGroup(token);
                groups.add(group);
            } else {
                modifier += Integer.parseInt(token);
            }
        }

        if (!foundDice) {
            throw new IllegalArgumentException("Invalid dice formula: no dice groups found in " + formula);
        }

        int total = groups.stream().mapToInt(DiceGroup::subtotal).sum() + modifier;

        return DiceRollResultBuilder.builder()
                .withFormula(formula.trim())
                .withGroups(groups)
                .withModifier(modifier)
                .withTotal(total)
                .build();
    }

    private DiceGroup parseDiceGroup(String token) {
        String[] parts = token.split("d");
        int count = (parts[0] == null || parts[0].isEmpty()) ? 1 : Integer.parseInt(parts[0]);
        int sides = Integer.parseInt(parts[1]);

        if (count <= 0 || sides <= 0) {
            throw new IllegalArgumentException("Invalid dice group: " + token);
        }

        List<Integer> rolls = new ArrayList<>(count);
        int subtotal = 0;
        for (int i = 0; i < count; i++) {
            int roll = ThreadLocalRandom.current().nextInt(1, sides + 1);
            rolls.add(roll);
            subtotal += roll;
        }

        return DiceGroupBuilder.builder()
                .withCount(count)
                .withSides(sides)
                .withRolls(rolls)
                .withSubtotal(subtotal)
                .build();
    }
}
