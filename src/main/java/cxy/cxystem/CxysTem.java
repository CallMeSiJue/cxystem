package cxy.cxystem;

import cxy.cxystem.dto.PlayerTempState;
import cxy.cxystem.netWork.NetworkHandler;
import cxy.cxystem.persistence.PlayerTempSL;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxysTem implements ModInitializer {

    public static final String MOD_ID = "cxys_tem";
    public static final Identifier INITIAL_SYNC = new Identifier(MOD_ID, "initial_sync");
    private static final Logger log = LoggerFactory.getLogger(CxysTem.class);

    @Override
    public void onInitialize() {
        NetworkHandler.registerC2SPackets();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerTempState playerState = PlayerTempSL.getPlayerState(handler.getPlayer());
            PacketByteBuf data = PacketByteBufs.create();
            Double environmentTemperature = TemHandler.getEnvironmentTemperature(server, handler.getPlayer());
            data.writeDouble(environmentTemperature);
            //

            //
            NetworkHandler.writeData(data, playerState);
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), NetworkHandler.PLAYER_TEMPERATURE_TICK_TRANSMISSION, data);
            });
        });


    }


}
