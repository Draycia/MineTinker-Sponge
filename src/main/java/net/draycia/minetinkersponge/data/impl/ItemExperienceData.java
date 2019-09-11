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

public class ItemExperienceData extends AbstractSingleData<Integer, ItemExperienceData, ItemExperienceData.Immutable> {
    
    public ItemExperienceData(int value) {
        super(MTKeys.MINETINKER_XP, value);
    }

    @Override
    public Optional<ItemExperienceData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ItemExperienceData> data_ = dataHolder.get(ItemExperienceData.class);
        if (data_.isPresent()) {
            ItemExperienceData data = data_.get();
            ItemExperienceData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ItemExperienceData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ItemExperienceData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getInt(MTKeys.MINETINKER_XP.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ItemExperienceData copy() {
        return new ItemExperienceData(getValue());
    }

    @Override
    protected Value<Integer> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_XP, getValue());
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

    public static class Immutable extends AbstractImmutableSingleData<Integer, ItemExperienceData.Immutable, ItemExperienceData> implements ImmutableDataManipulator<ItemExperienceData.Immutable, ItemExperienceData> {
        public Immutable(int value) {
            super(MTKeys.MINETINKER_XP, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_XP, getValue()).asImmutable();
        }

        @Override
        public ItemExperienceData asMutable() {
            return new ItemExperienceData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.MINETINKER_XP.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<ItemExperienceData> implements DataManipulatorBuilder<ItemExperienceData, ItemExperienceData.Immutable> {
        public Builder() {
            super(ItemExperienceData.class, 2);
        }

        @Override
        public ItemExperienceData create() {
            return new ItemExperienceData(0);
        }

        @Override
        public Optional<ItemExperienceData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ItemExperienceData> buildContent(DataView container) throws InvalidDataException {
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
            return content.set(DataQuery.of('.', "minetinker.minetinker_xp"), content.get(DataQuery.of('.', "minetinker.minetinker_xp"))).remove(DataQuery.of('.', "minetinker.minetinker_xp"));
        }
    }

}
