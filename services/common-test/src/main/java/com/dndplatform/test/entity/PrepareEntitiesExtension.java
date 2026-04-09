package com.dndplatform.test.entity;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PrepareEntitiesExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            var annotation = method.getAnnotation(PrepareEntities.class);
            if (annotation != null) {
                var provider = instantiate(annotation.value());
                var em = CDI.current().select(EntityManager.class).get();
                var tx = CDI.current().select(UserTransaction.class).get();
                try {
                    tx.begin();
                    provider.provideEntities().forEach(em::persist);
                    em.flush();
                    tx.commit();
                } catch (Exception e) {
                    try { tx.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException("Failed to prepare entities", e);
                }
            }
        });
    }

    private TestEntityProvider instantiate(Class<? extends TestEntityProvider> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + clazz.getName(), e);
        }
    }
}
