package org.fpm.di;

import org.fpm.di.*;

public class DummyEnvironment implements Environment {

    @Override
    public Container configure(Configuration configuration) {
        DummyBinder dummyBinder = new DummyBinder();
        configuration.configure(dummyBinder);
        return new DummyContainer(dummyBinder);
    }
}
