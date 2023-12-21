package cxy.cxystem.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class FreezingEffect extends StatusEffect {

    public FreezingEffect() {
        super(StatusEffectCategory.HARMFUL, 0xADD8E6); // 颜色代码可以根据您的喜好更改
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient && entity.age % 40 == 0 && entity.isFrozen() && entity.canFreeze()) {
            entity.damage(entity.getDamageSources().freeze(), 1.0F);
        }
        // 每2秒减少玩家的饥饿度
        if (entity.age % 20 == 0) {
            if (entity instanceof PlayerEntity playerEntity) {
                playerEntity.addExhaustion(0.005F * (float) (amplifier + 1));
            }
        }

        // 每5秒造成伤害
        if (entity.age % 100 == 0) {

            entity.damage(entity.getWorld().getDamageSources().freeze(), 1.0F);
        }

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // 确保每个游戏刻都调用applyUpdateEffect
    }
}