package com.github.artfly.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class ReflectionFactory<T> implements Factory<T> {

    @Override
    public T create(Class<T> clazz, ObjectsGraph objectsGraph) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if ((constructor.getModifiers() & Modifier.PUBLIC) == 0) continue;
            // TODO: invoking first found constructor
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                args[i] = objectsGraph.create(parameterType);
            }
            //noinspection unchecked
            return (T) create(clazz, constructor, args);
        }
        throw new IllegalStateException("Cannot create class " + clazz.getCanonicalName() + ". No suitable constructor");
    }

    private Object create(Class<?> clazz, Constructor<?> constructor, Object[] args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Cannot create class " + clazz.getCanonicalName(), e);
        }
    }
}
