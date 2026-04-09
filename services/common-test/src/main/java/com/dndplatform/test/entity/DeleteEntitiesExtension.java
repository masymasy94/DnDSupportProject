package com.dndplatform.test.entity;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteEntitiesExtension implements AfterEachCallback {

    private static final Pattern NAMED_PARAM_PATTERN = Pattern.compile(":([a-zA-Z_][a-zA-Z0-9_]*)");

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        var method = context.getTestMethod().orElse(null);
        if (method == null) {
            return;
        }
        var annotations = method.getAnnotationsByType(DeleteEntities.class);
        if (annotations.length == 0) {
            return;
        }
        var testInstance = context.getRequiredTestInstance();
        var namedParams = collectNamedParams(testInstance);

        var em = CDI.current().select(EntityManager.class).get();
        var tx = CDI.current().select(UserTransaction.class).get();
        try {
            tx.begin();
            for (var annotation : annotations) {
                var entityName = annotation.from().getSimpleName();
                String jpql;
                if (annotation.where().isBlank()) {
                    jpql = "DELETE FROM " + entityName;
                } else {
                    jpql = "DELETE FROM " + entityName + " e WHERE " + annotation.where();
                }
                Query query = em.createQuery(jpql);
                if (!annotation.where().isBlank()) {
                    bindNamedParams(query, annotation.where(), namedParams);
                }
                int deleted = query.executeUpdate();
                if (annotation.expectedRowCount() >= 0) {
                    assertThat(deleted)
                            .as("Row count for: %s", jpql)
                            .isEqualTo(annotation.expectedRowCount());
                }
            }
            tx.commit();
        } catch (Exception e) {
            try { tx.rollback(); } catch (Exception ignored) {}
            throw new RuntimeException("Failed to delete entities", e);
        }
    }

    /**
     * Visible for testing.
     * Resolves the JPQL parameter name for a {@link NamedParam} field:
     * uses the explicit annotation value if non-empty, otherwise the field name.
     */
    static String resolveParamName(Field field) {
        var annotation = field.getAnnotation(NamedParam.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not annotated with @NamedParam");
        }
        return annotation.value().isEmpty() ? field.getName() : annotation.value();
    }

    /**
     * Visible for testing.
     * Walks the test instance class hierarchy and returns a map of
     * paramName -> currentValue for every {@link NamedParam}-annotated field.
     */
    static Map<String, Object> collectNamedParams(Object testInstance) {
        Map<String, Object> result = new HashMap<>();
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NamedParam.class)) {
                    field.setAccessible(true);
                    try {
                        result.put(resolveParamName(field), field.get(testInstance));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot read @NamedParam field " + field.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    private static void bindNamedParams(Query query, String whereClause, Map<String, Object> namedParams) {
        Matcher matcher = NAMED_PARAM_PATTERN.matcher(whereClause);
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (!namedParams.containsKey(paramName)) {
                throw new IllegalStateException(
                        "JPQL where clause references :" + paramName
                                + " but no @NamedParam field with that name exists on the test class. "
                                + "Available: " + namedParams.keySet());
            }
            query.setParameter(paramName, namedParams.get(paramName));
        }
    }
}
