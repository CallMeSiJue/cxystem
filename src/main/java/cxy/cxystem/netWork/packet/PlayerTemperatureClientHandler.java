package cxy.cxystem.netWork.packet;

import cxy.cxystem.TemHandler;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.status.PalyerTempStatus;
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

    public static void receive() {
        ClientPlayNetworking.registerGlobalReceiver(
                NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION,
                (client, handler, buf, responseSender) -> {
                    // 从数据包中读取数据
                    double temperature = buf.readDouble();
                    log.info("客户端处理数据");
                    coolDown--;
                    if (coolDown == 0) {
                        // 在客户端主线程上执行操作
                        client.execute(() -> {

                            // 根据接收到的温度数据更新客户端，比如HUD
                            if (client.player != null) {
                                PalyerTempStatus playerTemperature = TemHandler.getPlayerTemperature(client.player, temperature);
                                client.player.sendMessage(Text.of(playerTemperature.getMessage()));
                            }
                        });
                        coolDown = 10;
                    }
                }
        );
    }
}
