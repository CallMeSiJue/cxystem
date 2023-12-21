package cxy.cxystem.status;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class PlayerStatusManage {
    public static void reduceHunger(PlayerEntity player, double amount) {
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel((int) (hungerManager.getFoodLevel() - amount));
        // 注意检查饥饿度不要低于0
        if (hungerManager.getFoodLevel() < 0) {
            hungerManager.setFoodLevel(0);
        }
    }

    public static void inVeryCold(PlayerEntity player) {
        player.setFrozenTicks(Optional.of(player.getFireTicks()).orElse(0) + 20);
    }

}
