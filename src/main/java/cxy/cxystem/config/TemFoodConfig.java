package cxy.cxystem.config;

import java.util.Map;

public class TemFoodConfig {
    private static TemFoodConfig instance = null;
    public Map<String, Integer> waterContainingFood;

    public TemFoodConfig() {
        this.waterContainingFood = Map.of("item.minecraft.melon_slice", 2,
                "item.minecraft.sweet_berries", 1,
                "item.minecraft.beetroot", 1,
                "item.minecraft.apple", 1,
                "item.minecraft.golden_apple", 2,
                "item.minecraft.carrot", 1,
                "item.minecraft.golden_carrot", 2,
                "item.minecraft.mushroom_stew", 6,
                "item.minecraft.beetroot_soup", 6,
                "item.minecraft.suspicious_stew", 6);
    }

    public static TemFoodConfig getInstance() {
        if (instance == null) {
            synchronized (TemFoodConfig.class) {
                if (instance == null) {
                    instance = new TemFoodConfig();
                }
            }
        }
        return instance;
    }
}
