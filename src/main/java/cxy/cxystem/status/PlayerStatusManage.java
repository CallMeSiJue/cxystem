package cxy.cxystem.status;

import cxy.cxystem.CxysTem;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.render.FreezeEffectRenderer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 建议服务端调用
 */
public class PlayerStatusManage {

    private static final Logger log = LoggerFactory.getLogger(FreezeEffectRenderer.class);
    private static final Identifier FREEZE_TEXTURE = new Identifier(CxysTem.MOD_ID, "textures/effect/freeze.png");
    private static final UUID POWDER_SNOW_SLOW_ID = UUID.fromString("1eaf83ff-7207-4526-b37a-d7a07b3ec4ce");

    public static void reduceHunger(PlayerEntity player, double amount) {
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel((int) (hungerManager.getFoodLevel() - amount));
        // 注意检查饥饿度不要低于0
        if (hungerManager.getFoodLevel() < 0) {
            hungerManager.setFoodLevel(0);
        }
    }

    public static void addPowderSnowSlowIfNeeded(PlayerEntity player, PlayerTempState playerData) {
        int i;
        if (playerData.freezeCount > 0) {
            EntityAttributeInstance entityAttributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (entityAttributeInstance == null) {
                return;
            }
            float f = -0.05f * getFreezePercent(playerData.freezeCount);
            entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(POWDER_SNOW_SLOW_ID, "Powder snow slow", f, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    public static float getFreezePercent(int freezeCount) {
        if (freezeCount > 200) {
            freezeCount = 200;
        }

        // 实现获取冻结百分比的逻辑
        // 例如：return player.getTicksFrozen() / (float) player.getMaxFreezeTime();
        return freezeCount / 200f;
    }

    public static void removePowderSnowSlow(PlayerEntity player) {
        EntityAttributeInstance entityAttributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (entityAttributeInstance == null) {
            return;
        }
        if (entityAttributeInstance.getModifier(POWDER_SNOW_SLOW_ID) != null) {
            entityAttributeInstance.removeModifier(POWDER_SNOW_SLOW_ID);
        }
    }


    public static void reduceHungryIfNeed(PlayerEntity player, PlayerTempState playerData) {
        if (playerData.playerTempStatus == PlayerTempStatus.COOL.getCode()) {
            player.getHungerManager().addExhaustion(0.1f);
        } else if (playerData.playerTempStatus == PlayerTempStatus.VERY_COOL.getCode()) {
            player.getHungerManager().addExhaustion(0.2f);
        }
    }
}
