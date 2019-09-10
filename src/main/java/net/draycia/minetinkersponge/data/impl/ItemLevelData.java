package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class ItemLevelData extends AbstractSingleData<Integer, ItemLevelData, ItemLevelData.Immutable> {
    
    public ItemLevelData(int value) {
        super(MTKeys.MINETINKER_LEVEL, value);
    }

    @Override
    public Optional<ItemLevelData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ItemLevelData> data_ = dataHolder.get(ItemLevelData.class);
        if (data_.isPresent()) {
            ItemLevelData data = data_.get();
            ItemLevelData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ItemLevelData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ItemLevelData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getInt(MTKeys.MINETINKER_LEVEL.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ItemLevelData copy() {
        return new ItemLevelData(getValue());
    }

    @Override
    protected Value<Integer> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_LEVEL, getValue());
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

    public static class Immutable extends AbstractImmutableSingleData<Integer, ItemLevelData.Immutable, ItemLevelData> implements ImmutableDataManipulator<ItemLevelData.Immutable, ItemLevelData> {
        public Immutable(int value) {
            super(MTKeys.MINETINKER_LEVEL, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_LEVEL, getValue()).asImmutable();
        }

        @Override
        public ItemLevelData asMutable() {
            return new ItemLevelData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.MINETINKER_LEVEL.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<ItemLevelData> implements DataManipulatorBuilder<ItemLevelData, ItemLevelData.Immutable> {
        public Builder() {
            super(ItemLevelData.class, 2);
        }

        @Override
        public ItemLevelData create() {
            return new ItemLevelData(0);
        }

        @Override
        public Optional<ItemLevelData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ItemLevelData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }

    public static class Int1To2Updater implements DataContentUpdater {

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
            return content.set(DataQuery.of('.', "minetinker.minetinker_level"), content.get(DataQuery.of('.', "minetinker.minetinker_level"))).remove(DataQuery.of('.', "minetinker.minetinker_level"));
        }
    }

}
