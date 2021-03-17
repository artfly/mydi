package com.github.artfly.di;

public interface Factory<T> {
    T create(Class<T> clazz, ObjectsGraph objectsGraph);
}
