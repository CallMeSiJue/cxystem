package cxy.cxystem;

import net.minecraft.server.MinecraftServer;

public class TemHandler {
    public static  final  double NORMAL_TEM=26.0;

    public static  final  double TEM_EACH_LEVEL=0.1;

    public static  final  int BASE_HIGHT=63;

    public Double getEnvironmentTemperature(MinecraftServer server){

        return NORMAL_TEM;
    }


    public Double getPlayerTemperature(MinecraftServer server){
        return NORMAL_TEM;
    }
}
