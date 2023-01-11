package org.fpm.di;

import javax.inject.Inject;
import java.lang.reflect.Constructor;

public class DummyContainer implements Container {
    DummyBinder dummyBinder;
    public DummyContainer(DummyBinder dummyBinder) {
        this.dummyBinder = dummyBinder;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        Class<T> searchClass = dummyBinder.searchClass(clazz);
        if (searchClass!=null)
            return CreateNewInstance(clazz);
        Class<? extends T> searchDepClass = dummyBinder.SearchDepClass(clazz);
        if(searchDepClass!=null)
            return CreateExtends(searchDepClass);

        T searchSingleClass = dummyBinder.SearchSingleClass(clazz);
        if(searchSingleClass!=null)
            return searchSingleClass;

        return CreateNewInstance(clazz);
    }


    private <T> T CreateExtends(Class<? extends T> clazz) {
        T returnedObject = getComponent(clazz);
        if (returnedObject!=null) {
            return returnedObject;
        }
        return CreateNewInstance(clazz);
    }

    private <T> T CreateNewInstance(Class<T> clazz) {
        T returned = CheckForInject(clazz);
        if (returned!=null)
            return returned;
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception ignored) {
        }
        return null;
    }

    private <T> T CheckForInject(Class<T> clazz){
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getAnnotation(Inject.class)!=null){
                try {
                    return (T) constructor.newInstance(getComponent(constructor.getParameterTypes()[0]));
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }
}
