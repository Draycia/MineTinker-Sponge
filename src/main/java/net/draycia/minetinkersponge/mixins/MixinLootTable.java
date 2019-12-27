package net.draycia.minetinkersponge.mixins;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.MTConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

@Mixin(LootTable.class)
public abstract class MixinLootTable {

    @Inject(method = "generateLootForPools", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onLootGeneration(Random rand, LootContext context, CallbackInfoReturnable<List> info, List list) {
        if (!MTConfig.CONVERT_LOOT_TABLES) {
            return;
        }

        for (ItemStack itemStack : (List<ItemStack>)list) {
            ModManager.getInstance().convertItemStack(((org.spongepowered.api.item.inventory.ItemStack)(Object)itemStack), true);
        }
    }

}
