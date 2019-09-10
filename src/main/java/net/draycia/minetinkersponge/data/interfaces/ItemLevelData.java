package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ItemLevelData extends DataManipulator<ItemLevelData, ItemLevelData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ItemLevelData> {
    }

    interface Builder extends DataManipulatorBuilder<ItemLevelData, Immutable> {

    }
}
