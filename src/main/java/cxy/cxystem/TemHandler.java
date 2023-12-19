package cxy.cxystem;

import com.google.common.util.concurrent.AtomicDouble;
import cxy.cxystem.config.CustomBiomeTemperatures;
import cxy.cxystem.config.TempConfig;
import cxy.cxystem.status.PalyerTempStatus;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 温度处理类
 */
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

    public static Double getEnvironmentTemperature(MinecraftServer server, PlayerEntity player) {
        TempConfig tempConfig = TempConfig.getInstance();
        AtomicDouble reallyTemp = new AtomicDouble(getPlayerReallyTemp(player));

        Biome.Precipitation precip = player.getWorld().getBiome(player.getBlockPos()).value().getPrecipitation(player.getBlockPos());

        //下雪还是 下雨
        if (precip == Biome.Precipitation.RAIN) {
            if (player.getWorld().isRaining()) {
                if (player.isWet() && !player.isTouchingWater()) {
                    reallyTemp.addAndGet(-8);
                }
            }
        } else if (precip == Biome.Precipitation.SNOW) {
            if (player.getWorld().isRaining()) {
                reallyTemp.addAndGet(-16);
            }
        }


        // 判断加温 分块
        Vec3d pos = player.getPos();
        Stream<BlockState> heatBlockBox = player.getWorld().getStatesInBox(Box.of(pos, 4, 4, 4));
        heatBlockBox.forEach((state) -> {
            tempConfig.heatingBlocks.forEach((b, t) -> {
                if (Objects.equals(state.getBlock().toString(), b)) {

                    if (state.isOf(Blocks.CAMPFIRE) || state.isOf(Blocks.SOUL_CAMPFIRE)) { //hard code to keep unlit campfires from heating.
                        if (state.get(CampfireBlock.LIT)) {
                            reallyTemp.addAndGet(t);
                        }
                    } else {
                        reallyTemp.addAndGet(t);
                    }

                }
            });
        });

        // 判断 降温 分块
        Stream<BlockState> coldBlockBox = player.getWorld().getStatesInBox(Box.of(pos, 2, 3, 2));
        coldBlockBox.forEach((state) -> {
            tempConfig.coolingBlocks.forEach((b, t) -> {
                if (Objects.equals(state.getBlock().toString(), b)) {
                    reallyTemp.addAndGet(t);
                }
            });
        });

        // 判断 手持 温度
        tempConfig.heldTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getMainHandStack().getItem().toString(), it)) {
                reallyTemp.addAndGet(t);
            }
            if (Objects.equals(player.getInventory().offHand.get(0).getItem().toString(), it)) {
                reallyTemp.addAndGet(t);
            }
        });

        return reallyTemp.get();
    }


    /**
     * 计算玩家体感温度状态
     *
     * @param player
     * @param envTemp
     * @return
     */
    public static PalyerTempStatus getPlayerTemperature(ClientPlayerEntity player, Double envTemp) {
        double coolTemp = NORMAL_TEM - BASIC_ADAPTATION_TEMP;
        double hotTemp = NORMAL_TEM + BASIC_ADAPTATION_TEMP;
        double veryCoolTemp = coolTemp - BASIC_ADAPTATION_TEMP;
        double veryHotTemp = hotTemp + BASIC_ADAPTATION_TEMP;

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
        double timeTempEffect = calculateTemperatureCoefficient(world.getTimeOfDay()) * 20 * Math.abs(playerBiomeTemperature);
        return reallyBiomeTemp - (y - BASE_HIGHT) * TEM_EACH_LEVEL + timeTempEffect;

    }

    /**
     * 计算时间温度系数
     *
     * @param timeOfDay
     * @return
     */
    public static double calculateTemperatureCoefficient(long timeOfDay) {

        if (timeOfDay > 24000) {
            timeOfDay = timeOfDay % 24000;
        }
        double period = 24000;
        double b = 2 * Math.PI / period;

        // 使波形在x=8000时处于2π（一个完整周期的起点）
        double c = timeOfDay - 8000;

        // 计算并返回余弦函数的值
        return Math.cos(c * b);


    }
}
