package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class ItemCompatibleData extends AbstractBooleanData<ItemCompatibleData, ItemCompatibleData.Immutable> {
    
    public ItemCompatibleData(boolean enabled) {
        super(MTKeys.IS_MINETINKER, enabled);
    }

    @Override
    public Optional<ItemCompatibleData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ItemCompatibleData> data_ = dataHolder.get(ItemCompatibleData.class);
        if (data_.isPresent()) {
            ItemCompatibleData data = data_.get();
            ItemCompatibleData finalData = overlap.merge(this, data);
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
    public ItemCompatibleData copy() {
        return new ItemCompatibleData(getValue());
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

    public static class Immutable extends AbstractImmutableBooleanData<ItemCompatibleData.Immutable, ItemCompatibleData> implements ImmutableDataManipulator<ItemCompatibleData.Immutable, ItemCompatibleData> {
        public Immutable(boolean enabled) {
            super(MTKeys.IS_MINETINKER, enabled);
        }

        @Override
        public ItemCompatibleData asMutable() {
            return new ItemCompatibleData(getValue());
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

    public static class Builder extends AbstractDataBuilder<ItemCompatibleData> implements DataManipulatorBuilder<ItemCompatibleData, ItemCompatibleData.Immutable> {
        public Builder() {
            super(ItemCompatibleData.class, 2);
        }

        @Override
        public ItemCompatibleData create() {
            return new ItemCompatibleData(false);
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
