package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.interfaces.ToolCompatibleData;
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

public class ToolCompatibleDataImpl extends AbstractBooleanData<ToolCompatibleData, ToolCompatibleData.Immutable> implements ToolCompatibleData {

    public ToolCompatibleDataImpl(boolean enabled) {
        super(MTKeys.IS_MT_TOOL, enabled);
    }

    @Override
    public Optional<ToolCompatibleData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ToolCompatibleDataImpl> data_ = dataHolder.get(ToolCompatibleDataImpl.class);
        if (data_.isPresent()) {
            ToolCompatibleDataImpl data = data_.get();
            ToolCompatibleDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ToolCompatibleData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ToolCompatibleData> from(DataView view) {
        if (view.contains(MTKeys.IS_MT_TOOL.getQuery())) {
            setValue(view.getBoolean(MTKeys.IS_MT_TOOL.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ToolCompatibleDataImpl copy() {
        return new ToolCompatibleDataImpl(getValue());
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

    public static class Immutable extends AbstractImmutableBooleanData<ToolCompatibleData.Immutable, ToolCompatibleData> implements ToolCompatibleData.Immutable {
        public Immutable(boolean enabled) {
            super(MTKeys.IS_MT_TOOL, enabled);
        }

        @Override
        public ToolCompatibleDataImpl asMutable() {
            return new ToolCompatibleDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.IS_MT_TOOL.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<ToolCompatibleData> implements ToolCompatibleData.Builder {
        public Builder() {
            super(ToolCompatibleData.class, 2);
        }

        @Override
        public ToolCompatibleDataImpl create() {
            return new ToolCompatibleDataImpl(false);
        }

        @Override
        public Optional<ToolCompatibleData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ToolCompatibleData> buildContent(DataView container) throws InvalidDataException {
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
