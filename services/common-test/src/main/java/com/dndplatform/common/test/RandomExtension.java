package com.dndplatform.common.test;

import org.instancio.Instancio;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Field;

public class RandomExtension implements ParameterResolver, BeforeEachCallback {

    @Override
    public boolean supportsParameter(ParameterContext paramCtx, ExtensionContext extCtx) {
        return paramCtx.isAnnotated(Random.class);
    }

    @Override
    public Object resolveParameter(ParameterContext paramCtx, ExtensionContext extCtx) {
        return Instancio.create(paramCtx.getParameter().getType());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var testInstance = context.getRequiredTestInstance();
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectRandom.class)) {
                    field.setAccessible(true);
                    Object value = Instancio.create(field.getType());
                    field.set(testInstance, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
