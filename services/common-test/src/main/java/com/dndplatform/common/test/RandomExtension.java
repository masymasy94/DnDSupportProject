package com.dndplatform.common.test;

import org.instancio.Instancio;
import org.instancio.TypeTokenSupplier;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class RandomExtension implements ParameterResolver, BeforeEachCallback {

    @Override
    public boolean supportsParameter(ParameterContext paramCtx, ExtensionContext extCtx) {
        return paramCtx.isAnnotated(Random.class);
    }

    @Override
    public Object resolveParameter(ParameterContext paramCtx, ExtensionContext extCtx) {
        Type type = paramCtx.getParameter().getParameterizedType();
        return createForType(type);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var testInstance = context.getRequiredTestInstance();
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectRandom.class)) {
                    field.setAccessible(true);
                    field.set(testInstance, createForType(field.getGenericType()));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Creates a random instance respecting generic type information.
     * For raw types (e.g. {@code String}, {@code MyPojo}), uses {@code Instancio.create(Class)}.
     * For parameterized types (e.g. {@code List<String>}), uses a {@code TypeTokenSupplier}
     * that returns the reflected type so Instancio preserves the type arguments.
     */
    private static Object createForType(Type type) {
        if (type instanceof Class<?> clazz) {
            return Instancio.create(clazz);
        }
        TypeTokenSupplier<Object> supplier = () -> type;
        return Instancio.create(supplier);
    }
}
