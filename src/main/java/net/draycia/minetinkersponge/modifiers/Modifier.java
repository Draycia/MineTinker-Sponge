package net.draycia.minetinkersponge.modifiers;

import org.spongepowered.api.item.ItemType;

import java.util.List;

public abstract class Modifier {

    public abstract String getName();
    public abstract String getKey();
    public abstract int getMaxLevel();
    public abstract List<ItemType> getCompatibleItems();

    public void onModifierRegister(Object plugin) {}
}
