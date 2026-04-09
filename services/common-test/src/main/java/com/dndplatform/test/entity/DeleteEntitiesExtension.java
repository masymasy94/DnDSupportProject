package com.dndplatform.test.entity;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteEntitiesExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            var annotations = method.getAnnotationsByType(DeleteEntities.class);
            if (annotations.length > 0) {
                var em = CDI.current().select(EntityManager.class).get();
                var tx = CDI.current().select(UserTransaction.class).get();
                try {
                    tx.begin();
                    for (var annotation : annotations) {
                        var entityName = annotation.from().getSimpleName();
                        var deleted = em.createQuery("DELETE FROM " + entityName).executeUpdate();
                        if (annotation.expectedRowCount() >= 0) {
                            assertThat(deleted).isEqualTo(annotation.expectedRowCount());
                        }
                    }
                    tx.commit();
                } catch (Exception e) {
                    try { tx.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException("Failed to delete entities", e);
                }
            }
        });
    }
}
