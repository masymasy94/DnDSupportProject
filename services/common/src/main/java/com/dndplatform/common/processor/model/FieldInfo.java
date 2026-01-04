package com.dndplatform.common.processor.model;

import javax.lang.model.type.TypeMirror;

/**
 * Represents a field or record component to be included in the builder.
 */
public record FieldInfo(
    String name,           // Field/component name
    TypeMirror type,       // Field/component type
    String accessorName    // Getter method name for toBuilder (e.g., "getName()" or "name()")
) {}