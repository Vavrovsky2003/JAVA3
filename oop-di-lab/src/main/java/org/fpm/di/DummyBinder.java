package org.fpm.di;

import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyBinder implements Binder {
    private final List<Class<?>> ListWithSingleClasses;

    public DummyBinder(){
        ListWithSingleClasses = new ArrayList<>();
        MapForInjection_Class_Class = new HashMap<>();
        MapForSingleton_Class_Object = new HashMap<>();
    }

    @Override
    public <T> void bind(Class<T> clazz){
        if(clazz.getAnnotation(Singleton.class) != null){
            try {
                Constructor<T> constructor = clazz.getConstructor();
                MapForSingleton_Class_Object.put(clazz, constructor.newInstance());
            }
            catch (Exception ignored){}
            return;
        }
        ListWithSingleClasses.add(clazz);
    }
    private final Map<Class<?>,Class<?>> MapForInjection_Class_Class;

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        MapForInjection_Class_Class.put(clazz, implementation);
    }

    private final Map<Class<?>, Object> MapForSingleton_Class_Object;
    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        MapForSingleton_Class_Object.put(clazz,instance);
    }

    public<T> Class<T> searchClass(Class<T> initial){
        if(ListWithSingleClasses.contains(initial))
            return initial;
        return null;
    }
    public <T> Class<? extends T> SearchDepClass(Class<T> initial){
        if (MapForInjection_Class_Class.containsKey(initial))
            return (Class<? extends T>) MapForInjection_Class_Class.get(initial);
        return null;
    }

    public <T> T SearchSingleClass(Class<T> initial){
        if (MapForSingleton_Class_Object.containsKey(initial))
            return (T) MapForSingleton_Class_Object.get(initial);
        return null;
    }
}
