package cxy.cxystem.netWork.packet;

import cxy.cxystem.netWork.NetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
                            if (temperature < 16 && client.player != null) {
                                client.player.sendMessage(Text.of("我感到寒冷"));
                            }
                        });
                        coolDown = 10;
                    }
                }
        );
    }
}
