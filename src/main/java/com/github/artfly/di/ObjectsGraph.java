package com.github.artfly.di;

import java.util.HashMap;
import java.util.Map;

public class ObjectsGraph {

    private final Map<Class<?>, Factory<?>> factories = new HashMap<>();

    public <T> void setup(Class<T> clazz, Factory<T> factory) {
        factories.put(clazz, factory);
    }

    public<T> T create(Class<T> clazz) {
        @SuppressWarnings("unchecked") Factory<T> factory = (Factory<T>) factories.get(clazz);
        if (factory == null) {
            factory = ReflectionFactory.create(clazz);
            factories.put(clazz, factory);
        }
        return factory.create(clazz, this);
    }
}
