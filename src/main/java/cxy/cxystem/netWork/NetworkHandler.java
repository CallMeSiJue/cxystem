package cxy.cxystem.netWork;

import cxy.cxystem.CxysTem;
import cxy.cxystem.netWork.packet.PlayerTemperatureServerHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkHandler {

    /**
     * 定义温度传输id
     */
    public static final Identifier PLAYER_TEMPERATURE_TICK_TRANSMISSION = new Identifier(CxysTem.MOD_ID, "player_temperature_tick_transmission");

    public static void registerC2SPackets() {

        ServerPlayNetworking.registerGlobalReceiver(PLAYER_TEMPERATURE_TICK_TRANSMISSION, PlayerTemperatureServerHandler::receive);


    }
}
