package cxy.cxystem.render;

import cxy.cxystem.CxysTem;
import cxy.cxystem.client.CxysTemClient;
import cxy.cxystem.status.PlayerTempStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class TempGlassRender {
    private static final Identifier TEMPERATE_GLASS = new Identifier(CxysTem.MOD_ID, "textures/glass_thermometer/temperate_glass.png");
    private static final Identifier COLD_GLASS = new Identifier(CxysTem.MOD_ID, "textures/glass_thermometer/cold_glass.png");
    private static final Identifier FROZEN_GLASS = new Identifier(CxysTem.MOD_ID, "textures/glass_thermometer/frozen_glass.png");
    private static final Identifier HOT_GLASS = new Identifier(CxysTem.MOD_ID, "textures/glass_thermometer/hot_glass.png");
    private static final Identifier BLAZING_GLASS = new Identifier(CxysTem.MOD_ID, "textures/glass_thermometer/blazing_glass.png");


    public static void renderGlass(DrawContext drawContext, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = (client.getWindow().getScaledWidth() / 2);
        int y = (client.getWindow().getScaledHeight() - 48);


        int statusCode = CxysTemClient.playerClientOldStatusCode;
        if (statusCode == PlayerTempStatus.COMFORTABLE.getCode()) {
            drawContext.drawTexture(TEMPERATE_GLASS, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
        } else if (statusCode == PlayerTempStatus.COOL.getCode()) {
            drawContext.drawTexture(COLD_GLASS, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
        } else if (statusCode == PlayerTempStatus.VERY_COOL.getCode()) {
            drawContext.drawTexture(FROZEN_GLASS, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
        } else if (statusCode == PlayerTempStatus.HOT.getCode()) {
            drawContext.drawTexture(HOT_GLASS, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
        } else if (statusCode == PlayerTempStatus.VERY_HOT.getCode()) {
            drawContext.drawTexture(BLAZING_GLASS, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
        }
    }
}
