package net.draycia.minetinkersponge.data;

import net.draycia.minetinkersponge.data.impl.*;
import net.draycia.minetinkersponge.data.interfaces.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.plugin.PluginContainer;

public class DataRegistrar {

    public DataRegistrar(PluginContainer container) {
        MTKeys.dummy();

        // MineTinker Compatible Items
        DataRegistration.builder()
                .dataName("Is MineTinker")
                .manipulatorId("is_minetinker")
                .dataClass(IsMineTinkerData.class)
                .dataImplementation(IsMineTinkerDataImpl.class)
                .immutableClass(IsMineTinkerData.Immutable.class)
                .immutableImplementation(IsMineTinkerDataImpl.Immutable.class)
                .builder(new IsMineTinkerDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerDataImpl.class, new IsMineTinkerDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Tools
        DataRegistration.builder()
                .dataName("Is MineTinker Tool")
                .manipulatorId("is_tool")
                .dataClass(IsMineTinkerToolData.class)
                .dataImplementation(IsMineTinkerToolDataImpl.class)
                .immutableClass(IsMineTinkerToolData.Immutable.class)
                .immutableImplementation(IsMineTinkerToolDataImpl.Immutable.class)
                .builder(new IsMineTinkerToolDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerToolDataImpl.class, new IsMineTinkerToolDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Armor
        DataRegistration.builder()
                .dataName("Is MineTinker Armor")
                .manipulatorId("is_armor")
                .dataClass(IsMineTinkerArmorData.class)
                .dataImplementation(IsMineTinkerArmorDataImpl.class)
                .immutableClass(IsMineTinkerArmorData.Immutable.class)
                .immutableImplementation(IsMineTinkerArmorDataImpl.Immutable.class)
                .builder(new IsMineTinkerArmorDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerArmorDataImpl.class, new IsMineTinkerArmorDataImpl.BoolEnabled1To2Updater());

        // MineTinker Item Level
        DataRegistration.builder()
                .dataName("Item Level")
                .manipulatorId("minetinker_level")
                .dataClass(MineTinkerItemLevelData.class)
                .dataImplementation(MineTinkerItemLevelDataImpl.class)
                .immutableClass(MineTinkerItemLevelData.Immutable.class)
                .immutableImplementation(MineTinkerItemLevelDataImpl.Immutable.class)
                .builder(new MineTinkerItemLevelDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(MineTinkerItemLevelDataImpl.class, new MineTinkerItemLevelDataImpl.Int1To2Updater());

        // MineTinker Item XP
        DataRegistration.builder()
                .dataName("Item XP")
                .manipulatorId("minetinker_xp")
                .dataClass(MineTinkerItemXPData.class)
                .dataImplementation(MineTinkerItemXPDataImpl.class)
                .immutableClass(MineTinkerItemXPData.Immutable.class)
                .immutableImplementation(MineTinkerItemXPDataImpl.Immutable.class)
                .builder(new MineTinkerItemXPDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(MineTinkerItemXPDataImpl.class, new MineTinkerItemXPDataImpl.Int1To2Updater());

        // List of Modifiers The Item Has
        DataRegistration.builder()
                .dataName("MineTinker Modifiers")
                .manipulatorId("mt_modifiers")
                .dataClass(MineTinkerItemModsData.class)
                .dataImplementation(MinetinkerItemModsDataImpl.class)
                .immutableClass(MineTinkerItemModsData.Immutable.class)
                .immutableImplementation(MinetinkerItemModsDataImpl.Immutable.class)
                .builder(new MinetinkerItemModsDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(MinetinkerItemModsDataImpl.class, new MinetinkerItemModsDataImpl.Builder.List1To2Updater());
    }

}
