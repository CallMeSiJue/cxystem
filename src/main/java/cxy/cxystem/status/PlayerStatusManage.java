package cxy.cxystem.status;

import cxy.cxystem.dto.PlayerTempState;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;

/**
 * 建议服务端调用
 */
public class PlayerStatusManage {
    public static void reduceHunger(PlayerEntity player, double amount) {
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel((int) (hungerManager.getFoodLevel() - amount));
        // 注意检查饥饿度不要低于0
        if (hungerManager.getFoodLevel() < 0) {
            hungerManager.setFoodLevel(0);
        }
    }

    public static void inVeryCold(PlayerTempState state, PlayerEntity player) {
        if (state.freezeCount < 200) {
            state.freezeCount += 1;
        }
        if (state.freezeCount >= 140 && player.age % 40 == 0) {
            player.damage(player.getDamageSources().freeze(), 1.0f);
        }
    }

}
