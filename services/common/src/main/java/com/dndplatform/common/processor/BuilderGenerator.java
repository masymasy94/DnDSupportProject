package com.dndplatform.common.processor;

import com.dndplatform.common.processor.model.BuildableType;
import com.dndplatform.common.processor.model.FieldInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.stream.Collectors;

/**
 * Generates builder class code using JavaPoet.
 *
 * Note: Standard annotation processing can only generate NEW files, not modify existing ones.
 * This generator creates template code that demonstrates what the builder should look like.
 * Users can either:
 * 1. Copy the generated builder code into their class/record
 * 2. Use a build tool plugin to automatically inject the code
 */
public class BuilderGenerator {

    /**
     * Generates a standalone builder class that extends the target type with builder methods.
     */
    public JavaFile generateBuilderClass(BuildableType buildable) {
        String builderClassName = buildable.className() + "Builder";

        TypeSpec.Builder builderClass = TypeSpec.classBuilder(builderClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("Generated builder for {@link $L}.\n", buildable.className())
            .addJavadoc("<p>\n")
            .addJavadoc("Usage:\n")
            .addJavadoc("<pre>\n")
            .addJavadoc("$L instance = $L.builder()\n", buildable.className(), buildable.className())
            .addJavadoc("    .withField(value)\n")
            .addJavadoc("    .build();\n")
            .addJavadoc("</pre>\n");

        // Add fields
        for (FieldInfo field : buildable.fields()) {
            FieldSpec fieldSpec = FieldSpec.builder(
                TypeName.get(field.type()),
                field.name(),
                Modifier.PRIVATE
            ).build();
            builderClass.addField(fieldSpec);
        }

        // Private constructor
        builderClass.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .build());

        // Add withXxx methods
        for (FieldInfo field : buildable.fields()) {
            builderClass.addMethod(createWithMethod(field, buildable, builderClassName));
        }

        // Add build() method
        builderClass.addMethod(createBuildMethod(buildable));

        // Add static builder() method
        builderClass.addMethod(createStaticBuilderMethodStandalone(builderClassName));

        // Add static toBuilder() method if enabled
        if (buildable.generateToBuilder()) {
            builderClass.addMethod(createStaticToBuilderMethod(buildable, builderClassName));
        }

        return JavaFile.builder(buildable.packageName(), builderClass.build())
            .indent("    ")
            .build();
    }

    private String generateDocumentation(BuildableType buildable) {
        StringBuilder doc = new StringBuilder();
        doc.append("Generated builder template for {@link ").append(buildable.className()).append("}.\n");
        doc.append("<p>\n");
        doc.append("Copy the following code into your ").append(buildable.isRecord() ? "record" : "class").append(":\n");
        doc.append("<ul>\n");
        doc.append("<li>The static {@code builder()} method</li>\n");
        if (buildable.generateToBuilder()) {
            doc.append("<li>The {@code toBuilder()} instance method</li>\n");
        }
        doc.append("<li>The entire {@code Builder} static class</li>\n");
        doc.append("</ul>\n");
        doc.append("<p>\n");
        doc.append("This template is generated because standard annotation processing cannot\n");
        doc.append("modify existing source files. Future versions may support automatic code injection\n");
        doc.append("through build tool plugins.\n");
        return doc.toString();
    }

    /**
     * Creates the static inner Builder class.
     */
    private TypeSpec createBuilderClass(BuildableType buildable) {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Builder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .addJavadoc("Builder for {@link " + buildable.className() + "}.");

        // Add fields
        for (FieldInfo field : buildable.fields()) {
            FieldSpec fieldSpec = FieldSpec.builder(
                TypeName.get(field.type()),
                field.name(),
                Modifier.PRIVATE
            ).build();
            builder.addField(fieldSpec);
        }

        // Private constructor
        builder.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .build());

        // Add withXxx methods
        for (FieldInfo field : buildable.fields()) {
            builder.addMethod(createWithMethod(field, buildable));
        }

        // Add build() method
        builder.addMethod(createBuildMethod(buildable));

        return builder.build();
    }

    /**
     * Creates a withXxx() method for a field.
     */
    private MethodSpec createWithMethod(FieldInfo field, BuildableType buildable) {
        String methodName = buildable.setterPrefix() + capitalize(field.name());

        return MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.bestGuess("Builder"))
            .addParameter(TypeName.get(field.type()), field.name())
            .addStatement("this.$N = $N", field.name(), field.name())
            .addStatement("return this")
            .build();
    }

    /**
     * Creates a withXxx() method for a field in standalone builder.
     */
    private MethodSpec createWithMethod(FieldInfo field, BuildableType buildable, String builderClassName) {
        String methodName = buildable.setterPrefix() + capitalize(field.name());

        return MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.bestGuess(builderClassName))
            .addParameter(TypeName.get(field.type()), field.name())
            .addStatement("this.$N = $N", field.name(), field.name())
            .addStatement("return this")
            .build();
    }

    /**
     * Creates the build() method.
     */
    private MethodSpec createBuildMethod(BuildableType buildable) {
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get(buildable.packageName(), buildable.className()));

        if (buildable.isRecord()) {
            // For records, call the canonical constructor
            String params = buildable.fields().stream()
                .map(FieldInfo::name)
                .collect(Collectors.joining(", "));

            buildMethod.addStatement("return new $T($L)",
                ClassName.get(buildable.packageName(), buildable.className()),
                params);
        } else {
            // For classes, use no-arg constructor + setters
            buildMethod.addStatement("$T instance = new $T()",
                ClassName.get(buildable.packageName(), buildable.className()),
                ClassName.get(buildable.packageName(), buildable.className()));

            for (FieldInfo field : buildable.fields()) {
                String setterName = "set" + capitalize(field.name());
                buildMethod.addStatement("instance.$N(this.$N)", setterName, field.name());
            }

            buildMethod.addStatement("return instance");
        }

        return buildMethod.build();
    }

    /**
     * Creates the static builder() method.
     */
    private MethodSpec createStaticBuilderMethod(BuildableType buildable) {
        return MethodSpec.methodBuilder("builder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(ClassName.bestGuess("Builder"))
            .addJavadoc("Creates a new builder for {@link " + buildable.className() + "}.")
            .addStatement("return new Builder()")
            .build();
    }

    /**
     * Creates the toBuilder() instance method.
     */
    private MethodSpec createToBuilderMethod(BuildableType buildable) {
        MethodSpec.Builder toBuilder = MethodSpec.methodBuilder("toBuilder")
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.bestGuess("Builder"))
            .addJavadoc("Creates a builder initialized with the current instance's values.");

        // Start with builder()
        toBuilder.addCode("return builder()");

        for (FieldInfo field : buildable.fields()) {
            String withMethod = buildable.setterPrefix() + capitalize(field.name());
            String accessor = buildable.isRecord()
                ? "this." + field.name() + "()"
                : "this." + field.accessorName();

            toBuilder.addCode("\n    .$L($L)", withMethod, accessor);
        }

        toBuilder.addCode(";\n");

        return toBuilder.build();
    }

    /**
     * Creates the static toBuilder() method for standalone builder.
     */
    private MethodSpec createStaticToBuilderMethod(BuildableType buildable, String builderClassName) {
        MethodSpec.Builder toBuilder = MethodSpec.methodBuilder("toBuilder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(ClassName.bestGuess(builderClassName))
            .addParameter(ClassName.get(buildable.packageName(), buildable.className()), "instance")
            .addJavadoc("Creates a builder initialized with the given instance's values.\n")
            .addJavadoc("@param instance the instance to copy values from\n")
            .addJavadoc("@return a new builder with values from the instance\n");

        // Start with builder()
        toBuilder.addCode("return builder()");

        for (FieldInfo field : buildable.fields()) {
            String withMethod = buildable.setterPrefix() + capitalize(field.name());
            String accessor = buildable.isRecord()
                ? "instance." + field.name() + "()"
                : "instance." + field.accessorName();

            toBuilder.addCode("\n    .$L($L)", withMethod, accessor);
        }

        toBuilder.addCode(";\n");

        return toBuilder.build();
    }

    /**
     * Creates the static builder() method for standalone builder.
     */
    private MethodSpec createStaticBuilderMethodStandalone(String builderClassName) {
        return MethodSpec.methodBuilder("builder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(ClassName.bestGuess(builderClassName))
            .addJavadoc("Creates a new builder instance.")
            .addStatement("return new $L()", builderClassName)
            .build();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}