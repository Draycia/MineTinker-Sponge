package net.draycia.minetinkersponge.data.impl;

import net.draycia.minetinkersponge.data.MTKeys;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableMappedData;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableMappedData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractMappedData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class ItemModifierListData extends AbstractMappedData<String, Integer, ItemModifierListData, ItemModifierListData.Immutable> {

    public ItemModifierListData(Map<String, Integer> map) {
        super(MTKeys.ITEM_MODIFIERS, map);
    }

    @Override
    public Optional<ItemModifierListData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ItemModifierListData> data_ = dataHolder.get(ItemModifierListData.class);
        if (data_.isPresent()) {
            ItemModifierListData data = data_.get();
            ItemModifierListData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<ItemModifierListData> from(DataContainer container) {
        return from((DataView) container);
    }

    @SuppressWarnings("unchecked")
    public Optional<ItemModifierListData> from(DataView view) {
        if (view.contains(MTKeys.ITEM_MODIFIERS.getQuery())) {
            setValue((Map<String, Integer>)view.getMap(MTKeys.ITEM_MODIFIERS.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ItemModifierListData copy() {
        return new ItemModifierListData(getValue());
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
        return super.toContainer().set(MTKeys.ITEM_MODIFIERS.getQuery(), getValue());
    }

    @Override
    public Optional<Integer> get(String key) {
        return Optional.ofNullable(getValue().get(key));
    }

    @Override
    public Set<String> getMapKeys() {
        return getValue().keySet();
    }

    @Override
    public ItemModifierListData put(String key, Integer value) {
        getValue().put(key, value);

        return this;
    }

    @Override
    public ItemModifierListData putAll(Map<? extends String, ? extends Integer> map) {
        getValue().putAll(map);

        return this;
    }

    @Override
    public ItemModifierListData remove(String key) {
        getValue().remove(key);

        return this;
    }

    public static class Immutable extends AbstractImmutableMappedData<String, Integer, ItemModifierListData.Immutable, ItemModifierListData> implements ImmutableDataManipulator<ItemModifierListData.Immutable, ItemModifierListData>, ImmutableMappedData<String, Integer, ItemModifierListData.Immutable, ItemModifierListData> {

        public Immutable(Map<String, Integer> map) {
            super(MTKeys.ITEM_MODIFIERS, map);
        }

        @Override
        public ItemModifierListData asMutable() {
            return new ItemModifierListData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MTKeys.ITEM_MODIFIERS.getQuery(), getValue());
        }
    }

    public static class Builder extends AbstractDataBuilder<ItemModifierListData> implements DataManipulatorBuilder<ItemModifierListData, ItemModifierListData.Immutable> {
        public Builder() {
            super(ItemModifierListData.class, 2);
        }

        @Override
        public ItemModifierListData create() {
            return new ItemModifierListData(new TreeMap<>());
        }

        @Override
        public Optional<ItemModifierListData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ItemModifierListData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
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
                return content.set(DataQuery.of('.', "minetinker.modifiers"), content.get(DataQuery.of('.', "minetinker.modifiers"))).remove(DataQuery.of('.', "minetinker.modifiers"));
            }
        }
    }
}
