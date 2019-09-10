package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ItemExperienceData extends DataManipulator<ItemExperienceData, ItemExperienceData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ItemExperienceData> {
    }

    interface Builder extends DataManipulatorBuilder<ItemExperienceData, Immutable> {

    }
}
