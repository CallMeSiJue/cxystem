package cxy.cxystem;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class TemHandler {
    public static final double NORMAL_TEM = 26.0;

    public static final double TEM_EACH_LEVEL = 0.1;

    public static final int BASE_HIGHT = 63;

    public static Double getEnvironmentTemperature(MinecraftServer server, ServerPlayerEntity player) {
        double y = player.getPos().getY();
        double envTemp = 26 - (y - BASE_HIGHT) * TEM_EACH_LEVEL;
        World world = player.getWorld();

        if (world.isRaining()) {
            envTemp = envTemp - 10;
        }
        return envTemp;
    }


    public static Double getPlayerTemperature(MinecraftServer server) {
        return NORMAL_TEM;
    }
}
