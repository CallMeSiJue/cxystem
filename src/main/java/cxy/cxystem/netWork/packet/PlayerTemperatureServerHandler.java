package cxy.cxystem.netWork.packet;

import cxy.cxystem.TemHandler;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.persistence.PlayerTempSL;
import cxy.cxystem.status.PlayerStatusManage;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端接受数据并进行处理 返回
 *
 * @author Ascs
 */
public class PlayerTemperatureServerHandler {


    private static final Logger log = LoggerFactory.getLogger(PlayerTemperatureServerHandler.class);

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PlayerTempState playerState = PlayerTempSL.getPlayerState(player);
        PlayerTempState.serverReadData(playerState, buf);
        double tem = TemHandler.getEnvironmentTemperature(server, player);
        playerState.feelTemp = TemHandler.getPlayerFeelTemp(player, tem);
        playerState.playerTempStatus = TemHandler.getPlayerTemperatureStatus(player, playerState.feelTemp).getCode();

        // 处理伤害
        PlayerStatusManage.coldDamageIfNeed(player, playerState);
        PlayerStatusManage.hotDamageIfNeed(player, playerState);
        PlayerStatusManage.reduceHungryIfNeed(player, playerState);
        PlayerStatusManage.reduceThirstIfNeed(player, playerState);
        PlayerStatusManage.applyNauseaEffectIfNeed(player, playerState);
        // 发送数据包到客户端
        PacketByteBuf sendData = PacketByteBufs.create();
        sendData.writeDouble(tem);
        //

        //
        PlayerTempState.writeData(sendData, playerState);
        ServerPlayNetworking.send(player, NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION, sendData);
    }


}
