package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.interfaces.ItemCompatibleData;
import net.draycia.minetinkersponge.data.MTKeys;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class ItemCompatibleDataImpl extends AbstractBooleanData<ItemCompatibleData, ItemCompatibleData.Immutable> implements ItemCompatibleData {
    
    public ItemCompatibleDataImpl(boolean enabled) {
        super(MTKeys.IS_MINETINKER, enabled);
    }

    @Override
    public Optional<ItemCompatibleData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ItemCompatibleDataImpl> data_ = dataHolder.get(ItemCompatibleDataImpl.class);
        if (data_.isPresent()) {
            ItemCompatibleDataImpl data = data_.get();
            ItemCompatibleDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ItemCompatibleData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ItemCompatibleData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getBoolean(MTKeys.IS_MINETINKER.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ItemCompatibleDataImpl copy() {
        return new ItemCompatibleDataImpl(getValue());
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
        return super.toContainer().set(MTKeys.IS_MINETINKER.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableBooleanData<ItemCompatibleData.Immutable, ItemCompatibleData> implements ItemCompatibleData.Immutable {
        public Immutable(boolean enabled) {
            super(MTKeys.IS_MINETINKER, enabled);
        }

        @Override
        public ItemCompatibleDataImpl asMutable() {
            return new ItemCompatibleDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.IS_MINETINKER.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<ItemCompatibleData> implements ItemCompatibleData.Builder {
        public Builder() {
            super(ItemCompatibleData.class, 2);
        }

        @Override
        public ItemCompatibleDataImpl create() {
            return new ItemCompatibleDataImpl(false);
        }

        @Override
        public Optional<ItemCompatibleData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ItemCompatibleData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }

    public static class BoolEnabled1To2Updater implements DataContentUpdater {

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
            return content.set(DataQuery.of('.', "minetinker.is_compatible"), content.get(DataQuery.of('.', "minetinker.is_compatible"))).remove(DataQuery.of('.', "minetinker.is_compatible"));
        }
    }

}
