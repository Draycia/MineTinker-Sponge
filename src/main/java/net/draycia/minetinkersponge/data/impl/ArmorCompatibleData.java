package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class ArmorCompatibleData extends AbstractBooleanData<ArmorCompatibleData, ArmorCompatibleData.Immutable> implements DataManipulator<ArmorCompatibleData, ArmorCompatibleData.Immutable> {

    public ArmorCompatibleData(boolean enabled) {
        super(MTKeys.IS_MT_ARMOR, enabled);
    }

    @Override
    public Optional<ArmorCompatibleData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ArmorCompatibleData> data_ = dataHolder.get(ArmorCompatibleData.class);
        if (data_.isPresent()) {
            ArmorCompatibleData data = data_.get();
            ArmorCompatibleData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ArmorCompatibleData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ArmorCompatibleData> from(DataView view) {
        if (view.contains(MTKeys.IS_MT_ARMOR.getQuery())) {
            setValue(view.getBoolean(MTKeys.IS_MT_ARMOR.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ArmorCompatibleData copy() {
        return new ArmorCompatibleData(getValue());
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
        return super.toContainer().set(MTKeys.IS_MT_ARMOR.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableBooleanData<ArmorCompatibleData.Immutable, ArmorCompatibleData> implements ImmutableDataManipulator<ArmorCompatibleData.Immutable, ArmorCompatibleData> {
        public Immutable(boolean enabled) {
            super(MTKeys.IS_MT_ARMOR, enabled);
        }

        @Override
        public ArmorCompatibleData asMutable() {
            return new ArmorCompatibleData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.IS_MT_ARMOR.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<ArmorCompatibleData> implements DataManipulatorBuilder<ArmorCompatibleData, ArmorCompatibleData.Immutable> {
        public Builder() {
            super(ArmorCompatibleData.class, 2);
        }

        @Override
        public ArmorCompatibleData create() {
            return new ArmorCompatibleData(false);
        }

        @Override
        public Optional<ArmorCompatibleData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ArmorCompatibleData> buildContent(DataView container) throws InvalidDataException {
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
            return content.set(DataQuery.of('.', "minetinker.is_armor"), content.get(DataQuery.of('.', "minetinker.is_armor"))).remove(DataQuery.of('.', "minetinker.is_armor"));
        }
    }

}
