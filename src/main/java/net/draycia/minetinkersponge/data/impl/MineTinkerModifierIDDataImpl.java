package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerModifierIDData;
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

public class MineTinkerModifierIDDataImpl extends AbstractSingleData<String, MineTinkerModifierIDData, MineTinkerModifierIDData.Immutable> implements MineTinkerModifierIDData {

    public MineTinkerModifierIDDataImpl(String data) {
        super(MTKeys.MODIFIER_ID, data);
    }

    @Override
    protected Value<String> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MODIFIER_ID, getValue());
    }

    @Override
    public Optional<MineTinkerModifierIDData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MineTinkerModifierIDDataImpl> data_ = dataHolder.get(MineTinkerModifierIDDataImpl.class);
        if (data_.isPresent()) {
            MineTinkerModifierIDDataImpl data = data_.get();
            MineTinkerModifierIDDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MineTinkerModifierIDData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MineTinkerModifierIDData> from(DataView view) {
        if (view.contains(MTKeys.MODIFIER_ID.getQuery())) {
            setValue(view.getString(MTKeys.MODIFIER_ID.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MineTinkerModifierIDDataImpl copy() {
        return new MineTinkerModifierIDDataImpl(getValue());
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
        return super.toContainer().set(MTKeys.MODIFIER_ID.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableSingleData<String, MineTinkerModifierIDData.Immutable, MineTinkerModifierIDData> implements MineTinkerModifierIDData.Immutable {
        public Immutable(String data) {
            super(MTKeys.MODIFIER_ID, data);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(MTKeys.MODIFIER_ID, getValue()).asImmutable();
        }

        @Override
        public MineTinkerModifierIDDataImpl asMutable() {
            return new MineTinkerModifierIDDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.MODIFIER_ID.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<MineTinkerModifierIDData> implements MineTinkerModifierIDData.Builder {
        public Builder() {
            super(MineTinkerModifierIDData.class, 2);
        }

        @Override
        public MineTinkerModifierIDDataImpl create() {
            return new MineTinkerModifierIDDataImpl("");
        }

        @Override
        public Optional<MineTinkerModifierIDData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MineTinkerModifierIDData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }

    public static class String1To2Updater implements DataContentUpdater {

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
            return content.set(DataQuery.of('.', "minetinker.modifier_if"), content.get(DataQuery.of('.', "minetinker.modifier_if"))).remove(DataQuery.of('.', "minetinker.modifier_if"));
        }
    }

}
