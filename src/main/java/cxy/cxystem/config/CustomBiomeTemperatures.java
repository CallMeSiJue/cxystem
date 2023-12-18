package cxy.cxystem.config;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class CustomBiomeTemperatures {
    private static final Map<Identifier, Double> customTemperatures = new HashMap<>();

    static {
        // 在这里初始化自定义的群系温度系数
        customTemperatures.put(new Identifier("minecraft:desert"), 1.1);
        customTemperatures.put(new Identifier("minecraft:savanna"), 1.0);
        customTemperatures.put(new Identifier("minecraft:savanna_plateau"), 1.05);
        customTemperatures.put(new Identifier("minecraft:windswept_hills"), 1.04);
        customTemperatures.put(new Identifier("minecraft:badlands"), 1.3);
        customTemperatures.put(new Identifier("minecraft:eroded_badlands"), 1.3);
        customTemperatures.put(new Identifier("minecraft:wooded_badlands"), 1.3);
        // ... 添加更多自定义群系温度
    }

    public static double getBiomeTemperature(World world, Biome biome) {
        Registry<Biome> biomes = world.getRegistryManager().get(RegistryKeys.BIOME);
        Identifier id = biomes.getId(biome);
        // 如果在自定义映射中找到了群系，就返回自定义的温度
        if (customTemperatures.containsKey(id)) {
            return customTemperatures.get(id);
        }
        // 否则，返回官方的群系温度系数
        return biome.getTemperature();
    }
}
