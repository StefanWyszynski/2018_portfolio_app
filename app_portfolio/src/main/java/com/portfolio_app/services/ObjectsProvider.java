package com.portfolio_app.services;

import java.util.HashMap;
import java.util.Map;

/*
 * Copyright 2018, The Portfolio project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Stefan Wyszynski
 *
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
