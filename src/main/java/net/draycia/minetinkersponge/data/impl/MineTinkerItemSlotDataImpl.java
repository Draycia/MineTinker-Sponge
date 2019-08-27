package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemSlotData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class MineTinkerItemSlotDataImpl extends AbstractSingleData<Integer, MineTinkerItemSlotData, MineTinkerItemSlotData.Immutable> implements MineTinkerItemSlotData {

    public MineTinkerItemSlotDataImpl(int value) {
        super(MTKeys.MINETINKER_SLOTS, value);
    }

    @Override
    public Optional<MineTinkerItemSlotData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MineTinkerItemSlotDataImpl> data_ = dataHolder.get(MineTinkerItemSlotDataImpl.class);
        if (data_.isPresent()) {
            MineTinkerItemSlotDataImpl data = data_.get();
            MineTinkerItemSlotDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MineTinkerItemSlotData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MineTinkerItemSlotData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getInt(MTKeys.MINETINKER_SLOTS.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MineTinkerItemSlotDataImpl copy() {
        return new MineTinkerItemSlotDataImpl(getValue());
    }

    @Override
    protected Value<Integer> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_SLOTS, getValue());
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

    public static class Immutable extends AbstractImmutableSingleData<Integer, MineTinkerItemSlotData.Immutable, MineTinkerItemSlotData> implements MineTinkerItemSlotData.Immutable {
        public Immutable(int value) {
            super(MTKeys.MINETINKER_SLOTS, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_SLOTS, getValue()).asImmutable();
        }

        @Override
        public MineTinkerItemSlotDataImpl asMutable() {
            return new MineTinkerItemSlotDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.MINETINKER_SLOTS.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<MineTinkerItemSlotData> implements MineTinkerItemSlotData.Builder {
        public Builder() {
            super(MineTinkerItemSlotData.class, 2);
        }

        @Override
        public MineTinkerItemSlotDataImpl create() {
            return new MineTinkerItemSlotDataImpl(0);
        }

        @Override
        public Optional<MineTinkerItemSlotData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MineTinkerItemSlotData> buildContent(DataView container) throws InvalidDataException {
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
            return content.set(DataQuery.of('.', "minetinker.minetinker_slots"), content.get(DataQuery.of('.', "minetinker.minetinker_slots"))).remove(DataQuery.of('.', "minetinker.minetinker_slots"));
        }
    }

}
