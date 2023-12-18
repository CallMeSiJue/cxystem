package cxy.cxystem;

import cxy.cxystem.config.CustomBiomeTemperatures;
import cxy.cxystem.status.PalyerTempStatus;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TemHandler {
    /**
     * 基本高度温度
     */
    public static final double NORMAL_TEM = 23;
    /**
     * 没增加一格减少的温度
     */
    public static final double TEM_EACH_LEVEL = 0.0625;

    /**
     * 基本温度高度
     */
    public static final int BASE_HIGHT = 63;

    /**
     * 基本适应温度
     */
    public static final double BASIC_ADAPTATION_TEMP = 12;
    private static final double MAX_DAY_TEMPERATURE_ADJUSTMENT = 2.0; // 最大日间温度调整系数
    private static final double MIN_NIGHT_TEMPERATURE_ADJUSTMENT = -2.0; // 最小夜间温度调整系数

    public static Double getEnvironmentTemperature(MinecraftServer server, PlayerEntity player) {
        double reallyTemp = getPlayerReallyTemp(player);
        World world = player.getWorld();
        if (world.isRaining()) {
            reallyTemp = reallyTemp - 10;
        }
        return reallyTemp;
    }


    public static PalyerTempStatus getPlayerTemperature(ClientPlayerEntity player, Double envTemp) {
        double coolTemp = NORMAL_TEM - BASIC_ADAPTATION_TEMP;
        double hotTemp = NORMAL_TEM + BASIC_ADAPTATION_TEMP;
        double veryCoolTemp = NORMAL_TEM - 2 * BASIC_ADAPTATION_TEMP;
        double veryHotTemp = NORMAL_TEM + 2 * BASIC_ADAPTATION_TEMP;

        if (envTemp <= coolTemp && envTemp > veryCoolTemp) {
            return PalyerTempStatus.COOL;
        } else if (envTemp <= veryCoolTemp) {
            return PalyerTempStatus.VERY_COOL;
        } else if (envTemp > hotTemp && envTemp <= veryHotTemp) {
            return PalyerTempStatus.HOT;
        } else if (envTemp > veryHotTemp) {
            return PalyerTempStatus.VERY_HOT;
        } else {
            return PalyerTempStatus.COMFORTABLE;
        }

    }

    public static double getPlayerBiomeTemperature(PlayerEntity player) {
        World world = player.getWorld(); // 获取玩家所在的世界
        BlockPos pos = player.getBlockPos(); // 获取玩家的位置
        Biome biome = world.getBiome(pos).value(); // 获取该位置的群系
        return CustomBiomeTemperatures.getBiomeTemperature(world, biome);
    }

    public static double getPlayerReallyTemp(PlayerEntity player) {
        double y = player.getPos().getY();
        double playerBiomeTemperature = getPlayerBiomeTemperature(player);

        World world = player.getWorld();

        double reallyBiomeTemp = 50 * playerBiomeTemperature - 7.5;
        double timeTempEffect = calculateTemperatureCoefficient(world.getTimeOfDay()) * 20 * playerBiomeTemperature;
        return reallyBiomeTemp - (y - BASE_HIGHT) * TEM_EACH_LEVEL + timeTempEffect;

    }

    public static double calculateTemperatureCoefficient(long timeOfDay) {
        double coefficient;

        // 时间段1: 0 - 8000 ticks，温度系数从0线性增加到1
        if (timeOfDay >= 0 && timeOfDay < 8000) {
            coefficient = timeOfDay / 8000.0;
        }
        // 时间段2: 8000 - 20000 ticks，温度系数从1线性减少到-1
        else if (timeOfDay >= 8000 && timeOfDay < 20000) {
            coefficient = 1 - 2 * (timeOfDay - 8000) / 12000.0;
        }
        // 时间段3: 20000 - 24000 ticks，温度系数从-1线性增加回0
        else if (timeOfDay >= 20000 && timeOfDay < 24000) {
            coefficient = -1 + (timeOfDay - 20000) / 4000.0;
        }
        // 处理时间超过一天的情况，例如通过取余重置时间
        else {
            timeOfDay = timeOfDay % 24000;
            coefficient = calculateTemperatureCoefficient(timeOfDay);
        }

        return coefficient;
    }
}
