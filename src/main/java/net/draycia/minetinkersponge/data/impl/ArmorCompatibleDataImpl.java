package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.interfaces.ArmorCompatibleData;
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

public class ArmorCompatibleDataImpl extends AbstractBooleanData<ArmorCompatibleData, ArmorCompatibleData.Immutable> implements ArmorCompatibleData {

    public ArmorCompatibleDataImpl(boolean enabled) {
        super(MTKeys.IS_MT_ARMOR, enabled);
    }

    @Override
    public Optional<ArmorCompatibleData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ArmorCompatibleDataImpl> data_ = dataHolder.get(ArmorCompatibleDataImpl.class);
        if (data_.isPresent()) {
            ArmorCompatibleDataImpl data = data_.get();
            ArmorCompatibleDataImpl finalData = overlap.merge(this, data);
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
    public ArmorCompatibleDataImpl copy() {
        return new ArmorCompatibleDataImpl(getValue());
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

    public static class Immutable extends AbstractImmutableBooleanData<ArmorCompatibleData.Immutable, ArmorCompatibleData> implements ArmorCompatibleData.Immutable {
        public Immutable(boolean enabled) {
            super(MTKeys.IS_MT_ARMOR, enabled);
        }

        @Override
        public ArmorCompatibleDataImpl asMutable() {
            return new ArmorCompatibleDataImpl(getValue());
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

    public static class Builder extends AbstractDataBuilder<ArmorCompatibleData> implements ArmorCompatibleData.Builder {
        public Builder() {
            super(ArmorCompatibleData.class, 2);
        }

        @Override
        public ArmorCompatibleDataImpl create() {
            return new ArmorCompatibleDataImpl(false);
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
