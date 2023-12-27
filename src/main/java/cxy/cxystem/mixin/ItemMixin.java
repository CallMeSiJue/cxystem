package cxy.cxystem.mixin;


import cxy.cxystem.config.TemFoodConfig;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.persistence.PlayerTempSL;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {


    // After eating or drinking an item
    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    private void vanillaThirst$addThirstIfItemIsHydrating(ItemStack stack, World world, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isFood() && livingEntity instanceof PlayerEntity player && !world.isClient()) {
            int thirstValue = 0;
            TemFoodConfig instance = TemFoodConfig.getInstance();
            Integer value = instance.waterContainingFood.get(stack.getItem().getTranslationKey());
            if (value != null) {
                thirstValue = value;
            }
            PlayerTempState playerState = PlayerTempSL.getPlayerState(player);
            playerState.addThirst(thirstValue);
        }
    }


    // Check if the player can consume hydrating items when the hunger bar is full but the thirst bar isn't
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/TypedActionResult;fail(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    private void vanillaThirst$canEatIfThirstBarNotFull(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (world.isClient()) {
            return;
        }
        PlayerTempState playerState = PlayerTempSL.getPlayerState(user);
        if (playerState.fullThirst()) {
            return;
        }
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        TemFoodConfig instance = TemFoodConfig.getInstance();

        if (instance.waterContainingFood.containsKey(itemStack.getTranslationKey())) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(itemStack));
        }


    }
}