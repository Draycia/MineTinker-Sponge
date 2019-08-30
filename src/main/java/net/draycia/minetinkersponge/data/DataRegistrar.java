package net.draycia.minetinkersponge.data;

import net.draycia.minetinkersponge.data.impl.*;
import net.draycia.minetinkersponge.data.interfaces.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;

public class DataRegistrar {

    public static void registerDataManipulators() {
        MTKeys.dummy();

        // MineTinker Compatible Items
        DataRegistration.builder()
                .name("Is MineTinker")
                .id("is_minetinker")
                .dataClass(IsMineTinkerData.class)
                .dataImplementation(IsMineTinkerDataImpl.class)
                .immutableClass(IsMineTinkerData.Immutable.class)
                .immutableImplementation(IsMineTinkerDataImpl.Immutable.class)
                .builder(new IsMineTinkerDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerDataImpl.class, new IsMineTinkerDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Tools
        DataRegistration.builder()
                .name("Is MineTinker Tool")
                .id("is_tool")
                .dataClass(IsMineTinkerToolData.class)
                .dataImplementation(IsMineTinkerToolDataImpl.class)
                .immutableClass(IsMineTinkerToolData.Immutable.class)
                .immutableImplementation(IsMineTinkerToolDataImpl.Immutable.class)
                .builder(new IsMineTinkerToolDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerToolDataImpl.class, new IsMineTinkerToolDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Armor
        DataRegistration.builder()
                .name("Is MineTinker Armor")
                .id("is_armor")
                .dataClass(IsMineTinkerArmorData.class)
                .dataImplementation(IsMineTinkerArmorDataImpl.class)
                .immutableClass(IsMineTinkerArmorData.Immutable.class)
                .immutableImplementation(IsMineTinkerArmorDataImpl.Immutable.class)
                .builder(new IsMineTinkerArmorDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerArmorDataImpl.class, new IsMineTinkerArmorDataImpl.BoolEnabled1To2Updater());

        // MineTinker Item Level
        DataRegistration.builder()
                .name("Item Level")
                .id("minetinker_level")
                .dataClass(MineTinkerItemLevelData.class)
                .dataImplementation(MineTinkerItemLevelDataImpl.class)
                .immutableClass(MineTinkerItemLevelData.Immutable.class)
                .immutableImplementation(MineTinkerItemLevelDataImpl.Immutable.class)
                .builder(new MineTinkerItemLevelDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(MineTinkerItemLevelDataImpl.class, new MineTinkerItemLevelDataImpl.Int1To2Updater());

        // MineTinker Item XP
        DataRegistration.builder()
                .name("Item XP")
                .id("minetinker_xp")
                .dataClass(MineTinkerItemXPData.class)
                .dataImplementation(MineTinkerItemXPDataImpl.class)
                .immutableClass(MineTinkerItemXPData.Immutable.class)
                .immutableImplementation(MineTinkerItemXPDataImpl.Immutable.class)
                .builder(new MineTinkerItemXPDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(MineTinkerItemXPDataImpl.class, new MineTinkerItemXPDataImpl.Int1To2Updater());

        // MineTinker Item XP
        DataRegistration.builder()
                .name("Item Modifier Slots")
                .id("minetinker_slots")
                .dataClass(MineTinkerItemSlotData.class)
                .dataImplementation(MineTinkerItemSlotDataImpl.class)
                .immutableClass(MineTinkerItemSlotData.Immutable.class)
                .immutableImplementation(MineTinkerItemSlotDataImpl.Immutable.class)
                .builder(new MineTinkerItemSlotDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(MineTinkerItemSlotDataImpl.class, new MineTinkerItemSlotDataImpl.Int1To2Updater());

        // MineTinker Modifier ID
        DataRegistration.builder()
                .name("Modifier ID")
                .id("modifier_id")
                .dataClass(MineTinkerModifierIDData.class)
                .dataImplementation(MineTinkerModifierIDDataImpl.class)
                .immutableClass(MineTinkerModifierIDData.Immutable.class)
                .immutableImplementation(MineTinkerModifierIDDataImpl.Immutable.class)
                .builder(new MineTinkerModifierIDDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(MineTinkerModifierIDDataImpl.class, new MineTinkerModifierIDDataImpl.String1To2Updater());

        // List of Modifiers The Item Has
        DataRegistration.builder()
                .name("MineTinker Modifiers")
                .id("mt_modifiers")
                .dataClass(MineTinkerItemModsData.class)
                .dataImplementation(MinetinkerItemModsDataImpl.class)
                .immutableClass(MineTinkerItemModsData.Immutable.class)
                .immutableImplementation(MinetinkerItemModsDataImpl.Immutable.class)
                .builder(new MinetinkerItemModsDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(MinetinkerItemModsDataImpl.class, new MinetinkerItemModsDataImpl.Builder.List1To2Updater());
    }
}
