package cxy.cxystem.client;

import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.netWork.packet.PlayerTemperatureClientHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxysTemClient implements ClientModInitializer {

    private static final Logger log = LoggerFactory.getLogger(CxysTemClient.class);
    public static PlayerTempState playerData = new PlayerTempState();
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        // 注册监听器，以便在每个tick执行
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                tickCounter++;
                if (tickCounter >= 20) {
                    boolean paused = client.isInSingleplayer() && client.isPaused();
                    if (!paused && !client.player.isCreative() && !client.player.isSpectator()) {
                        log.info("执行 发送消息");
                        ClientPlayNetworking.send(NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION, PacketByteBufs.create());
                    }
                    tickCounter = 0; // 重置计数器

                }
            }
        });
        // 注册客户端处理
        PlayerTemperatureClientHandler.receive(playerData);
    }


}