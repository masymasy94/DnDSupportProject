package com.dndplatform.common.processor.model;

import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Represents a type annotated with @Builder.
 */
public record BuildableType(
    TypeElement element,       // The annotated type element
    String packageName,        // Package name
    String className,          // Simple class name
    String qualifiedName,      // Fully qualified name
    boolean isRecord,          // True if record, false if class
    List<FieldInfo> fields,    // Fields/components to build
    boolean generateToBuilder, // Whether to generate toBuilder()
    String setterPrefix        // Prefix for withXxx methods (e.g., "with")
) {}