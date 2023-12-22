package cxy.cxystem.render;

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
        if (playerData.freezeCount > 40) {
            double freezePercent = getFreezePercent(playerData.freezeCount);
            int x = (int) (mc.getWindow().getScaledWidth() * freezePercent);
            int y = (int) (mc.getWindow().getScaledHeight() * freezePercent);
            drawContext.drawTexture(FREEZE_TEXTURE, 0, 0, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), x, y);
        }
    }

    private static double getFreezePercent(int freezeCount) {
        // 实现获取冻结百分比的逻辑
        // 例如：return player.getTicksFrozen() / (float) player.getMaxFreezeTime();
        double v = -0.005 * freezeCount + 1.7;
        if (v < 1) {
            return 1;
        } else if (v > 1.5) {
            return 1.5;
        }
        return v;
    }


}
