package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerToolData;
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

public class IsMineTinkerToolDataImpl extends AbstractBooleanData<IsMineTinkerToolData, IsMineTinkerToolData.Immutable> implements IsMineTinkerToolData {

    public IsMineTinkerToolDataImpl(boolean enabled) {
        super(enabled, MTKeys.IS_MT_TOOL, false);
    }

    @Override
    public Value<Boolean> enabled() {
        return getValueGetter();
    }

    @Override
    public Optional<IsMineTinkerToolData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<IsMineTinkerToolDataImpl> data_ = dataHolder.get(IsMineTinkerToolDataImpl.class);
        if (data_.isPresent()) {
            IsMineTinkerToolDataImpl data = data_.get();
            IsMineTinkerToolDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<IsMineTinkerToolData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<IsMineTinkerToolData> from(DataView view) {
        if (view.contains(MTKeys.IS_MT_TOOL.getQuery())) {
            setValue(view.getBoolean(MTKeys.IS_MT_TOOL.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public IsMineTinkerToolDataImpl copy() {
        return new IsMineTinkerToolDataImpl(getValue());
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
        return super.toContainer().set(MTKeys.IS_MT_TOOL.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableBooleanData<IsMineTinkerToolData.Immutable, IsMineTinkerToolData> implements IsMineTinkerToolData.Immutable {
        public Immutable(boolean enabled) {
            super(enabled, MTKeys.IS_MT_TOOL, false);
        }

        @Override
        public IsMineTinkerToolDataImpl asMutable() {
            return new IsMineTinkerToolDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.IS_MT_TOOL.getQuery(), getValue());
        }

        @Override
        public ImmutableValue<Boolean> enabled() {
            return getValueGetter();
        }
    }

    public static class Builder extends AbstractDataBuilder<IsMineTinkerToolData> implements IsMineTinkerToolData.Builder {
        public Builder() {
            super(IsMineTinkerToolData.class, 2);
        }

        @Override
        public IsMineTinkerToolDataImpl create() {
            return new IsMineTinkerToolDataImpl(false);
        }

        @Override
        public Optional<IsMineTinkerToolData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<IsMineTinkerToolData> buildContent(DataView container) throws InvalidDataException {
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
            return content.set(DataQuery.of('.', "minetinker.is_tool"), content.get(DataQuery.of('.', "minetinker.is_tool"))).remove(DataQuery.of('.', "minetinker.is_tool"));
        }
    }

}
