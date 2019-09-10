package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableMappedData;
import org.spongepowered.api.data.manipulator.mutable.MappedData;

public interface ItemModifierListData extends DataManipulator<ItemModifierListData, ItemModifierListData.Immutable>, MappedData<String, Integer, ItemModifierListData, ItemModifierListData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ItemModifierListData>, ImmutableMappedData<String, Integer, Immutable, ItemModifierListData> {
    }

    interface Builder extends DataManipulatorBuilder<ItemModifierListData, Immutable> {
    }
}
