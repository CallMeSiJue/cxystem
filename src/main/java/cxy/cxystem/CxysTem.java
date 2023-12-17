package cxy.cxystem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxysTem implements ModInitializer {

    private static final Logger log = LoggerFactory.getLogger(CxysTem.class);
    private int tickCount = 0; // 添加一个计数器来跟踪tick

    public static final  String MOD_ID= "cxys_tem";

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(this::onEndTick);
    }

    private void onEndTick(MinecraftServer server) {
        tickCount++; // 增加计数器

        if (tickCount >= 20) { // 检查是否达到20tick
            server.getPlayerManager().getPlayerList().forEach(player -> {
                // 获取玩家的位置
                BlockPos pos = player.getBlockPos();
                // 计算基础温度
                double baseTemperature = 26.0 - (pos.getY() - 63) * 0.1;
                // 检查天气影响
                if (player.getWorld().isRaining()) {
                    baseTemperature -= 10.0;
                }

                // 检查群系影响
                RegistryEntry<Biome> biome = player.getWorld().getBiome(pos);
                double biomeTemperatureEffect = getBiomeTemperatureEffect(biome.value());
                // 计算最终温度
                double finalTemperature = baseTemperature + biomeTemperatureEffect;
                log.info("当前温度:{}",finalTemperature);
                // 显示或使用最终温度
            });

            tickCount = 0; // 重置计数器
        }
    }

    private double getBiomeTemperatureEffect(Biome biome) {
        // 这里您可以根据群系返回不同的温度调整值
        // 如果群系没有特定的配置，返回0
        return 0.0;
    }
}
