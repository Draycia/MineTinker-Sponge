package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemModsData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableMappedData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractMappedData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.*;

public class MinetinkerItemModsDataImpl extends AbstractMappedData<String, Integer, MineTinkerItemModsData, MineTinkerItemModsData.Immutable> implements MineTinkerItemModsData {

    public MinetinkerItemModsDataImpl(Map<String, Integer> map) {
        super(map, MTKeys.ITEM_MODIFIERS);
    }

    @Override
    public Optional<MineTinkerItemModsData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MinetinkerItemModsDataImpl> data_ = dataHolder.get(MinetinkerItemModsDataImpl.class);
        if (data_.isPresent()) {
            MinetinkerItemModsDataImpl data = data_.get();
            MinetinkerItemModsDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MineTinkerItemModsData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MineTinkerItemModsData> from(DataView view) {
        if (view.contains(MTKeys.ITEM_MODIFIERS.getQuery())) {
            setValue((Map<String, Integer>)view.getMap(MTKeys.ITEM_MODIFIERS.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MinetinkerItemModsDataImpl copy() {
        return new MinetinkerItemModsDataImpl(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 2;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(MTKeys.ITEM_MODIFIERS.getQuery(), getValue());
    }

    @Override
    public Optional<Integer> get(String key) {
        return Optional.ofNullable(getValue().get(key));
    }

    @Override
    public Set<String> getMapKeys() {
        return getValue().keySet();
    }

    @Override
    public MineTinkerItemModsData put(String key, Integer value) {
        getValue().put(key, value);

        return this;
    }

    @Override
    public MineTinkerItemModsData putAll(Map<? extends String, ? extends Integer> map) {
        getValue().putAll(map);

        return this;
    }

    @Override
    public MineTinkerItemModsData remove(String key) {
        getValue().remove(key);

        return this;
    }

    public static class Immutable extends AbstractImmutableMappedData<String, Integer, MineTinkerItemModsData.Immutable, MineTinkerItemModsData> implements MineTinkerItemModsData.Immutable {

        public Immutable(Map<String, Integer> map) {
            super(map, MTKeys.ITEM_MODIFIERS);
        }

        @Override
        public MinetinkerItemModsDataImpl asMutable() {
            return new MinetinkerItemModsDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.ITEM_MODIFIERS.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<MineTinkerItemModsData> implements MineTinkerItemModsData.Builder {
        public Builder() {
            super(MineTinkerItemModsData.class, 2);
        }

        @Override
        public MinetinkerItemModsDataImpl create() {
            return new MinetinkerItemModsDataImpl(new HashMap<>());
        }

        @Override
        public Optional<MineTinkerItemModsData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MineTinkerItemModsData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

        public static class List1To2Updater implements DataContentUpdater {

            @Override
            public int getInputVersion() {
                return 1;
            }

            @Override
            public int getOutputVersion() {
                return 2;
            }

            @Override
            public DataView update(DataView content) {
                return content.set(DataQuery.of('.', "minetinker.modifiers"), content.get(DataQuery.of('.', "minetinker.modifiers"))).remove(DataQuery.of('.', "minetinker.modifiers"));
            }
        }
    }
}
