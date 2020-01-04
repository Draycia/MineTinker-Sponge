package net.draycia.minetinkersponge;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class SimpleBinderModule extends AbstractModule {

    private final MineTinkerSponge mineTinkerSponge;

    public SimpleBinderModule(MineTinkerSponge mineTinkerSponge) {
        this.mineTinkerSponge = mineTinkerSponge;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(MineTinkerSponge.class).toInstance(this.mineTinkerSponge);
    }

}
