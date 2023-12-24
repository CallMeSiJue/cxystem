package cxy.cxystem.netWork.packet;

import cxy.cxystem.TemHandler;
import cxy.cxystem.dto.PlayerStateDTO;
import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.persistence.PlayerTempSL;
import cxy.cxystem.status.PlayerTempStatus;
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
        NetworkHandler.readData(playerState, buf);
        
        double tem = TemHandler.getEnvironmentTemperature(server, player);
        double playerFeelTemp = TemHandler.getPlayerFeelTemp(player, tem);
        PlayerTempStatus status = TemHandler.getPlayerTemperatureStatus(player, playerFeelTemp);


        // 发送数据包到客户端
        PacketByteBuf sendData = PacketByteBufs.create();
        sendData.writeDouble(tem);
        //
        PlayerStateDTO dto = new PlayerStateDTO();
        dto.setFeelTemp(playerFeelTemp);
        dto.setPlayerTempStatus(status.getCode());
        dto.setFreezeCount(playerState.freezeCount);
        //
        NetworkHandler.writeData(sendData, dto);
        ServerPlayNetworking.send(player, NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION, sendData);
    }
}
