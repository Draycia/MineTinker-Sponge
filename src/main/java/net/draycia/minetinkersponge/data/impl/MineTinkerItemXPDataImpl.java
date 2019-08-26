package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemXPData;
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

public class MineTinkerItemXPDataImpl extends AbstractSingleData<Integer, MineTinkerItemXPData, MineTinkerItemXPData.Immutable> implements MineTinkerItemXPData {
    
    public MineTinkerItemXPDataImpl(int value) {
        super(value, MTKeys.MINETINKER_XP);
    }

    @Override
    public Optional<MineTinkerItemXPData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MineTinkerItemXPDataImpl> data_ = dataHolder.get(MineTinkerItemXPDataImpl.class);
        if (data_.isPresent()) {
            MineTinkerItemXPDataImpl data = data_.get();
            MineTinkerItemXPDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MineTinkerItemXPData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MineTinkerItemXPData> from(DataView view) {
        if (view.contains(MTKeys.IS_MINETINKER.getQuery())) {
            setValue(view.getInt(MTKeys.MINETINKER_XP.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MineTinkerItemXPDataImpl copy() {
        return new MineTinkerItemXPDataImpl(getValue());
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

    public static class Immutable extends AbstractImmutableSingleData<Integer, MineTinkerItemXPData.Immutable, MineTinkerItemXPData> implements MineTinkerItemXPData.Immutable {
        public Immutable(int value) {
            super(value, MTKeys.MINETINKER_XP);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MINETINKER_XP, getValue()).asImmutable();
        }

        @Override
        public MineTinkerItemXPDataImpl asMutable() {
            return new MineTinkerItemXPDataImpl(getValue());
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

    public static class Builder extends AbstractDataBuilder<MineTinkerItemXPData> implements MineTinkerItemXPData.Builder {
        public Builder() {
            super(MineTinkerItemXPData.class, 2);
        }

        @Override
        public MineTinkerItemXPDataImpl create() {
            return new MineTinkerItemXPDataImpl(0);
        }

        @Override
        public Optional<MineTinkerItemXPData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MineTinkerItemXPData> buildContent(DataView container) throws InvalidDataException {
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
