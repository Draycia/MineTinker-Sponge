package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemLevelData;
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

public class MineTinkerItemLevelDataImpl extends AbstractSingleData<Integer, MineTinkerItemLevelData, MineTinkerItemLevelData.Immutable> implements MineTinkerItemLevelData {
    
    public MineTinkerItemLevelDataImpl(int value) {
        super(MTKeys.MINETINKER_LEVEL, value);
    }

    @Override
    public Optional<MineTinkerItemLevelData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MineTinkerItemLevelDataImpl> data_ = dataHolder.get(MineTinkerItemLevelDataImpl.class);
        if (data_.isPresent()) {
            MineTinkerItemLevelDataImpl data = data_.get();
            MineTinkerItemLevelDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MineTinkerItemLevelData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MineTinkerItemLevelData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getInt(MTKeys.MINETINKER_LEVEL.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MineTinkerItemLevelDataImpl copy() {
        return new MineTinkerItemLevelDataImpl(getValue());
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

    public static class Immutable extends AbstractImmutableSingleData<Integer, MineTinkerItemLevelData.Immutable, MineTinkerItemLevelData> implements MineTinkerItemLevelData.Immutable {
        public Immutable(int value) {
            super(MTKeys.MINETINKER_LEVEL, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_LEVEL, getValue()).asImmutable();
        }

        @Override
        public MineTinkerItemLevelDataImpl asMutable() {
            return new MineTinkerItemLevelDataImpl(getValue());
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

    public static class Builder extends AbstractDataBuilder<MineTinkerItemLevelData> implements MineTinkerItemLevelData.Builder {
        public Builder() {
            super(MineTinkerItemLevelData.class, 2);
        }

        @Override
        public MineTinkerItemLevelDataImpl create() {
            return new MineTinkerItemLevelDataImpl(0);
        }

        @Override
        public Optional<MineTinkerItemLevelData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MineTinkerItemLevelData> buildContent(DataView container) throws InvalidDataException {
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
