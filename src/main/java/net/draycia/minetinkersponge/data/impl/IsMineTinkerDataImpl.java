package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerData;
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

public class IsMineTinkerDataImpl extends AbstractBooleanData<IsMineTinkerData, IsMineTinkerData.Immutable> implements IsMineTinkerData {
    
    public IsMineTinkerDataImpl(boolean enabled) {
        super(enabled, MTKeys.IS_MINETINKER, false);
    }

    @Override
    public Value<Boolean> enabled() {
        return getValueGetter();
    }

    @Override
    public Optional<IsMineTinkerData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<IsMineTinkerDataImpl> data_ = dataHolder.get(IsMineTinkerDataImpl.class);
        if (data_.isPresent()) {
            IsMineTinkerDataImpl data = data_.get();
            IsMineTinkerDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<IsMineTinkerData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<IsMineTinkerData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getBoolean(MTKeys.IS_MINETINKER.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public IsMineTinkerDataImpl copy() {
        return new IsMineTinkerDataImpl(getValue());
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

    public static class Immutable extends AbstractImmutableBooleanData<IsMineTinkerData.Immutable, IsMineTinkerData> implements IsMineTinkerData.Immutable {
        public Immutable(boolean enabled) {
            super(enabled, MTKeys.IS_MINETINKER, false);
        }

        @Override
        public IsMineTinkerDataImpl asMutable() {
            return new IsMineTinkerDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.IS_MINETINKER.getQuery(), getValue());
        }

        @Override
        public ImmutableValue<Boolean> enabled() {
            return getValueGetter();
        }
    }

    public static class Builder extends AbstractDataBuilder<IsMineTinkerData> implements IsMineTinkerData.Builder {
        public Builder() {
            super(IsMineTinkerData.class, 2);
        }

        @Override
        public IsMineTinkerDataImpl create() {
            return new IsMineTinkerDataImpl(false);
        }

        @Override
        public Optional<IsMineTinkerData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<IsMineTinkerData> buildContent(DataView container) throws InvalidDataException {
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
