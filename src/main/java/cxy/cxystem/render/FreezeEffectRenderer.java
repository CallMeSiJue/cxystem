package cxy.cxystem.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxystem.CxysTem;
import cxy.cxystem.client.CxysTemClient;
import cxy.cxystem.dto.PlayerTempState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class FreezeEffectRenderer {
    private static final Identifier FREEZE_TEXTURE = new Identifier(CxysTem.MOD_ID, "textures/effect/freeze.png");

    public static void register() {
        HudRenderCallback.EVENT.register(FreezeEffectRenderer::renderFreezeEffect);
    }


    private static void renderFreezeEffect(DrawContext drawContext, float tickDelta) {

        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerTempState playerData = CxysTemClient.playerData;
        if (mc.player == null) {
            return;
        }
        if (playerData.freezeCount > 40) {
            float freezePercent = getFreezePercent(playerData.freezeCount);
            int scaledWidth = mc.getWindow().getScaledWidth();
            int scaledHeight = mc.getWindow().getScaledHeight();
            renderOverlay(drawContext, FREEZE_TEXTURE, freezePercent, scaledWidth, scaledHeight);
        }
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

    private static float getFreezePercent(int freezeCount) {
        if (freezeCount > 200) {
            freezeCount = 200;
        }

        // 实现获取冻结百分比的逻辑
        // 例如：return player.getTicksFrozen() / (float) player.getMaxFreezeTime();
        return freezeCount / 200f;
    }


}
