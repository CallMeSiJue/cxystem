package cxy.cxystem.status;

import cxy.cxystem.CxysTem;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.render.FreezeEffectRenderer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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

    public static void coldDamageIfNeed(ServerPlayerEntity player, PlayerTempState playerState) {
        if (playerState.freezeCount > 140) {
            player.damage(player.getWorld().getDamageSources().freeze(), 0.5f);
            if (player.isDead()) {
                playerState.reset();
            }
        }
    }

    public static void hotDamageIfNeed(ServerPlayerEntity player, PlayerTempState playerState) {
        if (playerState.thirstValue == 0) {
            player.damage(player.getWorld().getDamageSources().hotFloor(), 0.5f);
            if (player.isDead()) {
                playerState.reset();
            }
        }
    }

    public static boolean hasHireResistance(ServerPlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
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

    public static void reduceThirstIfNeed(ServerPlayerEntity player, PlayerTempState playerData) {
        //  有火焰保护效果的 不考虑减少 口渴值
        if (hasHireResistance(player)) {
            return;
        }
        if (playerData.playerTempStatus == PlayerTempStatus.HOT.getCode() && playerData.thirstCount <= 0) {
            playerData.thirstValue -= 1;
            if (playerData.thirstValue < 0) {
                playerData.thirstValue = 0;
            }
            playerData.thirstCount = 10;
        } else if (playerData.playerTempStatus == PlayerTempStatus.VERY_HOT.getCode() && playerData.thirstCount <= 0) {
            playerData.thirstValue -= 2;
            if (playerData.thirstValue < 0) {
                playerData.thirstValue = 0;
            }
            playerData.thirstCount = 10;
        } else {
            playerData.thirstCount -= 1;
        }

    }

    public static void applyNauseaEffectIfNeed(PlayerEntity player, PlayerTempState playerData) {
        if (playerData.thirstValue != 0) {
            return;
        }
        int duration = 200; // 持续时间，以tick为单位，20 tick = 1秒
        int amplifier = playerData.playerTempStatus - 1; // 效果等级，1 表示 Nausea II

        StatusEffectInstance nausea = new StatusEffectInstance(StatusEffects.NAUSEA, duration, amplifier);
        player.addStatusEffect(nausea);
    }
}
