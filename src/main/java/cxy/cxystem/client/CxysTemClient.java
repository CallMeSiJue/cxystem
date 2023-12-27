package cxy.cxystem.client;

import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.netWork.packet.PlayerTemperatureClientHandler;
import cxy.cxystem.render.FreezeEffectRenderer;
import cxy.cxystem.status.PlayerTempStatus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxysTemClient implements ClientModInitializer {

    private static final Logger log = LoggerFactory.getLogger(CxysTemClient.class);
    public static PlayerTempState playerData = new PlayerTempState();
    public static int playerClientOldStatusCode = 0;
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        // 注册监听器，以便在每个tick执行
        ClientTickEvents.START_CLIENT_TICK.register(client -> {

            // 处理状态造成的变化
            if (playerData.playerTempStatus == PlayerTempStatus.VERY_COOL.getCode()) {
                playerData.addFreezeCount(1);
            } else if (playerData.freezeCount > 0) {
                playerData.freezeCount -= 2;
            }
            if (playerData.playerTempStatus == PlayerTempStatus.VERY_HOT.getCode()) {
                playerData.addHotCount(1);
            } else if (playerData.hotCount > 0) {
                playerData.hotCount -= 2;
            }
            //

            // 数据处理
            if (client.world != null) {
                if (client.player == null) {
                    return;
                }
                tickCounter++;
                if (tickCounter >= 20) {
                    boolean paused = client.isInSingleplayer() && client.isPaused();
                    if (!paused && !client.player.isCreative() && !client.player.isSpectator()) {
                        log.info("执行 发送消息");
                        PacketByteBuf packetByteBuf = PacketByteBufs.create();

                        PlayerTempState.clientWriteData(packetByteBuf, playerData);
                        ClientPlayNetworking.send(NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION, packetByteBuf);
                    }
                    tickCounter = 0; // 重置计数器

                }
            }

        });
        // 注册客户端处理
        PlayerTemperatureClientHandler.receive(playerData);

        // 渲染注册
        FreezeEffectRenderer.register();

    }


}