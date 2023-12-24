package cxy.cxystem.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxystem.CxysTem;
import cxy.cxystem.client.CxysTemClient;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.status.PlayerTempStatus;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 冰冻最状态处理器
 */
public class FreezeEffectRenderer {
    private static final Logger log = LoggerFactory.getLogger(FreezeEffectRenderer.class);
    private static final Identifier FREEZE_TEXTURE = new Identifier(CxysTem.MOD_ID, "textures/effect/freeze.png");
    private static final UUID POWDER_SNOW_SLOW_ID = UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ce");

    public static void register() {
        HudRenderCallback.EVENT.register(FreezeEffectRenderer::renderFreezeEffect);
    }

    private static void renderFreezeEffect(DrawContext drawContext, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerTempState playerData = CxysTemClient.playerData;

        if (mc.player == null) {
            return;
        }
        if (playerData.freezeCount > 0) {
            float freezePercent = getFreezePercent(playerData.freezeCount);

            int scaledWidth = mc.getWindow().getScaledWidth();
            int scaledHeight = mc.getWindow().getScaledHeight();
            log.info("尝试渲染时间：{}， 寒冷百分比{}，寒冷计数{}", LocalDateTime.now(), freezePercent, playerData.freezeCount);
            renderOverlay(drawContext, FREEZE_TEXTURE, freezePercent, scaledWidth, scaledHeight);

            //removePowderSnowSlow(mc.player);
            //addPowderSnowSlowIfNeeded(mc.player, playerData);

        }
    }

    private static float getFreezePercent(int freezeCount) {
        if (freezeCount > 200) {
            freezeCount = 200;
        }

        // 实现获取冻结百分比的逻辑
        // 例如：return player.getTicksFrozen() / (float) player.getMaxFreezeTime();
        return freezeCount / 200f;
    }

    public static void renderOverlay(DrawContext context, Identifier texture, float opacity, int x1, int x2) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        context.drawTexture(texture, 0, 0, -90, 0.0f, 0.0f, x1, x2, x1, x2);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
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
