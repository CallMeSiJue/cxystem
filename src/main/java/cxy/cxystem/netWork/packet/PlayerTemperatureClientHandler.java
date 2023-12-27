package cxy.cxystem.netWork.packet;

import cxy.cxystem.client.CxysTemClient;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端处理 服务端发来的数据包并进行处理
 *
 * @author Ascs
 */

public class PlayerTemperatureClientHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerTemperatureClientHandler.class);

    public static void receive(PlayerTempState playerData) {

        ClientPlayNetworking.registerGlobalReceiver(
                NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION,
                (client, handler, buf, responseSender) -> {
                    // 从数据包中读取数据
                    double temperature = buf.readDouble();
                    PlayerTempState.readData(playerData, buf);

                    log.info("  的环境温度：{},体感温度：{}", temperature, playerData.feelTemp);
                    // 在客户端主线程上执行操作
                    client.execute(() -> {

                        // 根据接收到的温度数据更新客户端，比如HUD
                        if (client.player != null) {


                            if (playerData.playerTempStatus != CxysTemClient.playerClientOldStatusCode) {
                                CxysTemClient.playerClientOldStatusCode = playerData.playerTempStatus;
                            }

                        }
                    });


                }
        );
    }
}
