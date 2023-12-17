package cxy.cxystem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CxysTemClient implements ClientModInitializer {
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                tickCounter++;
                if (tickCounter >= 100) { // 每100 ticks发送一次消息，约等于5秒
                    sendMessage(client);
                    tickCounter = 0; // 重置计数器
                }
            }
        });
    }

    private void sendMessage(MinecraftClient client) {
        if (client.player != null) {
            client.player.sendMessage(Text.of("我感觉有点冷")); // 发送消息

        }
    }
}