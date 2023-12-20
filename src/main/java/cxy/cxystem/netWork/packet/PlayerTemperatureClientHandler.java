package cxy.cxystem.netWork.packet;

import cxy.cxystem.TemHandler;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.status.PlayerTempStatus;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端处理 服务端发来的数据包并进行处理
 *
 * @author Ascs
 */

public class PlayerTemperatureClientHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerTemperatureClientHandler.class);
    public static int coolDown = 10;

    public static void receive(PlayerTempState playerData) {

        ClientPlayNetworking.registerGlobalReceiver(
                NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION,
                (client, handler, buf, responseSender) -> {
                    // 从数据包中读取数据
                    double temperature = buf.readDouble();
                    NetworkHandler.readData(playerData, buf);
                    log.info("客户端处理数据");
                    coolDown--;
                    log.info("  的环境温度：{},体感温度：{}", temperature, playerData.feelTemp);
                    if (coolDown == 0) {
                        // 在客户端主线程上执行操作
                        client.execute(() -> {

                            // 根据接收到的温度数据更新客户端，比如HUD
                            if (client.player != null) {

                                PlayerTempStatus newStatus = TemHandler.getPlayerTemperatureStatus(client.player, playerData.feelTemp);
                                if (playerData.playerTempStatus != newStatus.getCode()) {
                                    client.player.sendMessage(Text.of(newStatus.getMessage()));
                                }
                            }
                        });
                        coolDown = 10;
                    }
                }
        );
    }
}
