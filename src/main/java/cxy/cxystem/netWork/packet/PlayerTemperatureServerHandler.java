package cxy.cxystem.netWork.packet;

import cxy.cxystem.TemHandler;
import cxy.cxystem.netWork.NetworkHandler;
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

        double tem = TemHandler.getEnvironmentTemperature(server, player);
        double playerFeelTemp = TemHandler.getPlayerFeelTemp(player, tem);

        PacketByteBuf sendData = PacketByteBufs.create();
        sendData.writeDouble(tem);
        sendData.writeDouble(playerFeelTemp);
        PlayerStatusManage.inVeryCold(player);
        log.info("玩家的Frozen:{}", player.getFrozenTicks());
        ServerPlayNetworking.send(player, NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION, sendData);
    }
}
