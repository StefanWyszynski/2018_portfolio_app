package com.portfolio_app.services;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stefan Wyszynski
 */
public class ObjectsProvider {
    private static ObjectsProvider instance = new ObjectsProvider();
    private Map<Class<? extends Object>, Object> servicesRegister;

    private ObjectsProvider() {
        servicesRegister = new HashMap<>();
    }

    public static ObjectsProvider getInstance() {
        return instance;
    }

    public <T> T get(Class<? extends T> object_class) {
        return (T) servicesRegister.get(object_class);
    }

    public <T> void registerObject(Class<T> object_class, T object) {
        servicesRegister.put(object_class, object);
    }
}
