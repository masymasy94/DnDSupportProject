package com.dndplatform.character.adapter.inbound.create.generator;

import java.util.Map;

public record CharacterFieldMap(
        Map<String, String> textFields,
        Map<String, Boolean> checkboxFields
) {}
