package com.dndplatform.common.processor;

import com.dndplatform.common.annotations.Builder;
import com.dndplatform.common.processor.model.BuildableType;
import com.dndplatform.common.processor.model.FieldInfo;
import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Annotation processor for @Builder annotation.
 * Generates builder pattern implementations for annotated classes and records.
 */
@SupportedAnnotationTypes("com.dndplatform.common.annotations.Builder")
public class BuilderProcessor extends AbstractProcessor {

    private BuilderGenerator builderGenerator;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (builderGenerator == null) {
            builderGenerator = new BuilderGenerator();
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            if (!(element instanceof TypeElement typeElement)) {
                continue;
            }

            try {
                BuildableType buildableType = extractBuildableType(typeElement);
                validateElement(typeElement, buildableType);

                JavaFile javaFile = builderGenerator.generateBuilderClass(buildableType);
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Failed to write generated builder class: " + e.getMessage(),
                    element
                );
            } catch (IllegalStateException e) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    e.getMessage(),
                    element
                );
            }
        }

        return true;
    }

    private BuildableType extractBuildableType(TypeElement typeElement) {
        Builder builderAnnotation = typeElement.getAnnotation(Builder.class);

        String packageName = processingEnv.getElementUtils()
            .getPackageOf(typeElement)
            .getQualifiedName()
            .toString();

        String className = typeElement.getSimpleName().toString();
        String qualifiedName = typeElement.getQualifiedName().toString();
        boolean isRecord = typeElement.getKind() == ElementKind.RECORD;

        List<FieldInfo> fields = isRecord
            ? extractRecordComponents(typeElement)
            : extractClassFields(typeElement);

        return new BuildableType(
            typeElement,
            packageName,
            className,
            qualifiedName,
            isRecord,
            fields,
            builderAnnotation.toBuilder(),
            builderAnnotation.setterPrefix()
        );
    }

    private List<FieldInfo> extractRecordComponents(TypeElement recordElement) {
        List<FieldInfo> fields = new ArrayList<>();

        for (RecordComponentElement component : recordElement.getRecordComponents()) {
            String name = component.getSimpleName().toString();
            String accessorName = name + "()";

            fields.add(new FieldInfo(name, component.asType(), accessorName));
        }

        return fields;
    }

    private List<FieldInfo> extractClassFields(TypeElement classElement) {
        List<FieldInfo> fields = new ArrayList<>();

        for (VariableElement field : ElementFilter.fieldsIn(classElement.getEnclosedElements())) {
            // Skip static and final fields
            if (field.getModifiers().contains(Modifier.STATIC) ||
                field.getModifiers().contains(Modifier.FINAL)) {
                continue;
            }

            String name = field.getSimpleName().toString();
            String accessorName = deriveGetterName(name, field.asType().toString());

            fields.add(new FieldInfo(name, field.asType(), accessorName));
        }

        return fields;
    }

    private String deriveGetterName(String fieldName, String typeName) {
        String capitalized = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        // Use 'is' prefix for boolean fields
        if ("boolean".equals(typeName)) {
            return "is" + capitalized + "()";
        }

        return "get" + capitalized + "()";
    }

    private void validateElement(TypeElement element, BuildableType buildableType) {
        // Check if element is a valid target (class or record)
        if (element.getKind().isInterface() ||
            element.getKind() == ElementKind.ENUM ||
            element.getKind() == ElementKind.ANNOTATION_TYPE) {
            throw new IllegalStateException(
                "@Builder can only be applied to concrete classes or records"
            );
        }

        // Check if abstract class
        if (element.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new IllegalStateException(
                "@Builder cannot be applied to abstract classes"
            );
        }

        // For classes, validate constructor requirements
        if (!buildableType.isRecord()) {
            validateClassRequirements(element, buildableType);
        }
    }

    private void validateClassRequirements(TypeElement element, BuildableType buildableType) {
        boolean hasNoArgConstructor = hasConstructor(element, 0);
        boolean hasAllArgsConstructor = hasConstructor(element, buildableType.fields().size());

        if (!hasNoArgConstructor && !hasAllArgsConstructor) {
            throw new IllegalStateException(
                "Class must have either a no-arg constructor or an all-args constructor matching all fields"
            );
        }

        // If using no-arg constructor, validate setters exist
        if (hasNoArgConstructor && !hasAllArgsConstructor) {
            validateSetters(element, buildableType);
        }

        // If toBuilder is enabled, validate getters exist
        if (buildableType.generateToBuilder()) {
            validateGetters(element, buildableType);
        }
    }

    private boolean hasConstructor(TypeElement element, int parameterCount) {
        for (ExecutableElement constructor : ElementFilter.constructorsIn(element.getEnclosedElements())) {
            if (constructor.getParameters().size() == parameterCount) {
                return true;
            }
        }
        return false;
    }

    private void validateSetters(TypeElement element, BuildableType buildableType) {
        for (FieldInfo field : buildableType.fields()) {
            String setterName = "set" + capitalize(field.name());
            boolean hasSetterMethod = hasSetter(element, setterName);

            if (!hasSetterMethod) {
                throw new IllegalStateException(
                    "Field '" + field.name() + "' must have a setter method '" + setterName + "' for builder support"
                );
            }
        }
    }

    private void validateGetters(TypeElement element, BuildableType buildableType) {
        for (FieldInfo field : buildableType.fields()) {
            String getterName = field.accessorName().replace("()", "");
            boolean hasGetterMethod = hasGetter(element, getterName);

            if (!hasGetterMethod) {
                throw new IllegalStateException(
                    "Field '" + field.name() + "' must have a getter method '" + getterName + "' for toBuilder() support"
                );
            }
        }
    }

    private boolean hasSetter(TypeElement element, String setterName) {
        for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
            if (method.getSimpleName().toString().equals(setterName) &&
                method.getParameters().size() == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean hasGetter(TypeElement element, String getterName) {
        for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
            if (method.getSimpleName().toString().equals(getterName) &&
                method.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}