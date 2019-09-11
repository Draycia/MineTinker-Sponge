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

public class ModifierSlotData extends AbstractSingleData<Integer, ModifierSlotData, ModifierSlotData.Immutable> {

    public ModifierSlotData(int value) {
        super(MTKeys.MINETINKER_SLOTS, value);
    }

    @Override
    public Optional<ModifierSlotData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ModifierSlotData> data_ = dataHolder.get(ModifierSlotData.class);
        if (data_.isPresent()) {
            ModifierSlotData data = data_.get();
            ModifierSlotData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ModifierSlotData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ModifierSlotData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getInt(MTKeys.MINETINKER_SLOTS.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ModifierSlotData copy() {
        return new ModifierSlotData(getValue());
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

    public static class Immutable extends AbstractImmutableSingleData<Integer, ModifierSlotData.Immutable, ModifierSlotData> implements ImmutableDataManipulator<ModifierSlotData.Immutable, ModifierSlotData> {
        public Immutable(int value) {
            super(MTKeys.MINETINKER_SLOTS, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_SLOTS, getValue()).asImmutable();
        }

        @Override
        public ModifierSlotData asMutable() {
            return new ModifierSlotData(getValue());
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

    public static class Builder extends AbstractDataBuilder<ModifierSlotData> implements DataManipulatorBuilder<ModifierSlotData, ModifierSlotData.Immutable> {
        public Builder() {
            super(ModifierSlotData.class, 2);
        }

        @Override
        public ModifierSlotData create() {
            return new ModifierSlotData(0);
        }

        @Override
        public Optional<ModifierSlotData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ModifierSlotData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }

    public static class VersionUpdater implements DataContentUpdater {

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
