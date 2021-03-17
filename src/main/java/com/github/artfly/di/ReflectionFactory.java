package com.github.artfly.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ReflectionFactory<T> implements Factory<T> {

    private ReflectionFactory() {
    }

    public static <T> ReflectionFactory<T> create(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Component.class)) {
            throw new IllegalStateException("""
                    Class {} is not a subject for dependency injection.
                    Mark it with @Component to create automatically.
                    """.replace("{}", clazz.getCanonicalName()));
        }
        return new ReflectionFactory<>();
    }

    @Override
    public T create(Class<T> clazz, ObjectsGraph objectsGraph) {
        List<Constructor<?>> constructors = findConstructors(clazz.getDeclaredConstructors());
        if (constructors.isEmpty()) {
            throw new IllegalStateException("No public constructors marked with @Autowired for component " + clazz.getCanonicalName());
        }
        if (constructors.size() > 1) {
            throw new IllegalStateException("Too many @Autowired public constructors for component " + clazz.getCanonicalName());
        }
        Constructor<?> constructor = constructors.get(0);
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            args[i] = objectsGraph.create(parameterType);
        }
        //noinspection unchecked
        return (T) create(clazz, constructor, args);
    }

    private List<Constructor<?>> findConstructors(Constructor<?>[] constructors) {
        List<Constructor<?>> found = new ArrayList<>();
        for (Constructor<?> constructor : constructors) {
            if ((constructor.getModifiers() & Modifier.PUBLIC) == 0) continue;
            if (!constructor.isAnnotationPresent(Autowired.class)) continue;
            found.add(constructor);
        }
        return found;
    }


    private Object create(Class<?> clazz, Constructor<?> constructor, Object[] args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Cannot create class " + clazz.getCanonicalName(), e);
        }
    }
}
