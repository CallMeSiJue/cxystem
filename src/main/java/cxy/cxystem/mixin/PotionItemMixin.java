package cxy.cxystem.mixin;

import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.persistence.PlayerTempSL;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {

    // Add thirstLevel after drinking a water bottle
    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void vanillaThirst$hydratingWaterBottle(ItemStack stack, World world, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (livingEntity instanceof PlayerEntity player && !world.isClient()) {
            PlayerTempState playerState = PlayerTempSL.getPlayerState(player);
            playerState.addThirst(10);

        }
    }
}
