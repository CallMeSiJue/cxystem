package cxy.cxystem.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxystem.CxysTem;
import cxy.cxystem.client.CxysTemClient;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.status.PlayerStatusManage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FreezeEffectRenderer {
    private static final Identifier FREEZE_TEXTURE = new Identifier(CxysTem.MOD_ID, "textures/effect/freeze.png");

    public static void register() {
        HudRenderCallback.EVENT.register(FreezeEffectRenderer::renderFreezeEffect);
        HudRenderCallback.EVENT.register(ThirstRender::renderThird);
        HudRenderCallback.EVENT.register(TempGlassRender::renderGlass);
    }


    private static void renderFreezeEffect(DrawContext drawContext, float tickDelta) {

        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerTempState playerData = CxysTemClient.playerData;
        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator()) {
            return;
        }
        if (playerData.freezeCount > 0) {
            float freezePercent = PlayerStatusManage.getFreezePercent(playerData.freezeCount);
            int scaledWidth = mc.getWindow().getScaledWidth();
            int scaledHeight = mc.getWindow().getScaledHeight();
            renderOverlay(drawContext, FREEZE_TEXTURE, freezePercent, scaledWidth, scaledHeight);
        }

        PlayerStatusManage.removePowderSnowSlow(mc.player);
        PlayerStatusManage.addPowderSnowSlowIfNeeded(mc.player, playerData);

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


}
