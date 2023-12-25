package cxy.cxystem.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxystem.CxysTem;
import cxy.cxystem.client.CxysTemClient;
import cxy.cxystem.dto.PlayerTempState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ThirstRender {
    private static final Identifier THIRST_HUD = new Identifier(CxysTem.MOD_ID, "textures/gui/thirst/thirst_icons.png");

    public static void renderThird(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        PlayerTempState playerData = CxysTemClient.playerData;
        ClientPlayerEntity player = client.player;
        if (player == null || player.isCreative() || player.isSpectator()) {
            return;
        }
        int width = client.getWindow().getScaledWidth() / 2;
        int height = client.getWindow().getScaledHeight();
        int bounceFactor = 0;

        // Defining the texture
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, THIRST_HUD);

        // Get the ThirstManager for the player


        // Get the thirstValue
        int thirstValue = playerData.thirstValue;

        // If the player currently is in a hot biome for enough time or in The Nether
        int hotXFactor = 0;
        int hotYFactor = 0;

        if (playerData.playerTempStatus == 2) {
            hotYFactor = 9;
        } else if (playerData.playerTempStatus == 3) {
            hotXFactor = 36;
        }


        // If the player currently has the thirst effect


        // Create the Thirst Bar
        // Empty Thirst
        for (int i = 0; i < 10; i++) {
            bounceFactor = getBounceFactor(player, player.age, playerData);
            drawContext.drawTexture(THIRST_HUD,
                    (width + 82 - (i * 9) + i),
                    (height - 49 + bounceFactor),
                    hotYFactor,
                    0,
                    9,
                    9,
                    256,
                    256);
        }

        // Half Thirst
        for (int i = 0; i < 20; i++) {
            if (thirstValue != 0) {
                if (((thirstValue + 1) / 2) > i) {
                    bounceFactor = getBounceFactor(player, player.age, playerData);
                    drawContext.drawTexture(THIRST_HUD,
                            (width + 82 - (i * 9) + i),
                            (height - 49 + bounceFactor),
                            9 + hotXFactor,
                            9,
                            9,
                            9,
                            256,
                            256);
                } else {
                    break;
                }
            }
        }

        // Full Thirst
        for (int i = 0; i < 20; i++) {
            if (thirstValue != 0) {
                if ((thirstValue / 2) > i) {
                    bounceFactor = getBounceFactor(player, player.age, playerData);
                    drawContext.drawTexture(THIRST_HUD,
                            (width + 82 - (i * 9) + i),
                            (height - 49 + bounceFactor),
                            hotXFactor,
                            9,
                            9,
                            9,
                            256,
                            256);
                } else {
                    break;
                }
            }
        }


    }

    private static int getBounceFactor(PlayerEntity player, int ticks, PlayerTempState playerData) {
        if (playerData.playerTempStatus == 2) {
            return player.getWorld().random.nextInt(3) - 1;
        } else if (playerData.playerTempStatus == 3) {
            return player.getWorld().random.nextInt(5) - 1;
        } else {
            return 0;
        }
    }
}
