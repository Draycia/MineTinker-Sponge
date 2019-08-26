package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerArmorData;
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
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class IsMineTinkerArmorDataImpl extends AbstractBooleanData<IsMineTinkerArmorData, IsMineTinkerArmorData.Immutable> implements IsMineTinkerArmorData {

    public IsMineTinkerArmorDataImpl(boolean enabled) {
        super(enabled, MTKeys.IS_MT_ARMOR, false);
    }

    @Override
    public Value<Boolean> enabled() {
        return getValueGetter();
    }

    @Override
    public Optional<IsMineTinkerArmorData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<IsMineTinkerArmorDataImpl> data_ = dataHolder.get(IsMineTinkerArmorDataImpl.class);
        if (data_.isPresent()) {
            IsMineTinkerArmorDataImpl data = data_.get();
            IsMineTinkerArmorDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<IsMineTinkerArmorData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<IsMineTinkerArmorData> from(DataView view) {
        if (view.contains(MTKeys.IS_MT_ARMOR.getQuery())) {
            setValue(view.getBoolean(MTKeys.IS_MT_ARMOR.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public IsMineTinkerArmorDataImpl copy() {
        return new IsMineTinkerArmorDataImpl(getValue());
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

    public static class Immutable extends AbstractImmutableBooleanData<IsMineTinkerArmorData.Immutable, IsMineTinkerArmorData> implements IsMineTinkerArmorData.Immutable {
        public Immutable(boolean enabled) {
            super(enabled, MTKeys.IS_MT_ARMOR, false);
        }

        @Override
        public IsMineTinkerArmorDataImpl asMutable() {
            return new IsMineTinkerArmorDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.IS_MT_ARMOR.getQuery(), getValue());
        }

        @Override
        public ImmutableValue<Boolean> enabled() {
            return getValueGetter();
        }
    }

    public static class Builder extends AbstractDataBuilder<IsMineTinkerArmorData> implements IsMineTinkerArmorData.Builder {
        public Builder() {
            super(IsMineTinkerArmorData.class, 2);
        }

        @Override
        public IsMineTinkerArmorDataImpl create() {
            return new IsMineTinkerArmorDataImpl(false);
        }

        @Override
        public Optional<IsMineTinkerArmorData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<IsMineTinkerArmorData> buildContent(DataView container) throws InvalidDataException {
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
