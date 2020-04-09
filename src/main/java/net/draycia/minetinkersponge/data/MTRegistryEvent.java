package net.draycia.minetinkersponge.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.registry.AdditionalCatalogRegistryModule;
import org.spongepowered.api.registry.CatalogRegistryModule;

public class MTRegistryEvent<T extends CatalogType> implements GameRegistryEvent.Register<T> {

    private final Cause cause;
    private final Class<T> catalogType;
    private final AdditionalCatalogRegistryModule<T> registryModule;

    public MTRegistryEvent(Cause cause, Class<T> catalogType, AdditionalCatalogRegistryModule<T> registryModule) {
        this.cause = cause;
        this.catalogType = catalogType;
        this.registryModule = registryModule;
    }

    @Override
    public Class<T> getCatalogType() {
        return this.catalogType;
    }

    @Override
    public CatalogRegistryModule<T> getRegistryModule() {
        return this.registryModule;
    }

    @Override
    public void register(T catalogType) {
        this.registryModule.registerAdditionalCatalog(catalogType);
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public TypeToken<T> getGenericType() {
        return TypeToken.of(this.catalogType);
    }

}
