package cxy.cxystem.config;

import cxy.cxystem.dto.ArmorTemp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempConfig {
    private static TempConfig instance = null;

    public Map<String, Integer> heatingBlocks;
    public Map<String, Integer> coolingBlocks;
    public List<ArmorTemp> helmetTempItems;

    public List<ArmorTemp> chestplateTempItems;

    public List<ArmorTemp> leggingTempItems;

    public List<ArmorTemp> bootTempItems;

    public Map<String, Integer> heldTempItems;

    private TempConfig() {
        // 初始化 heatingBlocks 和 coolingBlocks
        heatingBlocks = new HashMap<>(Map.of("Block{minecraft:torch}", 5,
                "Block{minecraft:fire}", 10,
                "Block{minecraft:lava}", 20,
                "Block{minecraft:campfire}", 10,
                "Block{minecraft:wall_torch}", 10,
                "Block{minecraft:soul_torch}", -5,
                "Block{minecraft:soul_wall_torch}", -5,
                "Block{minecraft:soul_campfire}", -10));

        coolingBlocks = new HashMap<>(Map.of("Block{minecraft:ice}", -10,
                "Block{minecraft:packed_ice}", -10,
                "Block{minecraft:blue_ice}", -20));

        helmetTempItems = List.of(new ArmorTemp("leather_helmet", 1d, 0.95));

        chestplateTempItems = List.of(new ArmorTemp("leather_chestplate", 3d, 0.85));

        leggingTempItems = List.of(new ArmorTemp("leather_leggings", 2d, 0.88));

        bootTempItems = List.of(new ArmorTemp("leather_boots", 1d, 0.92));

        heldTempItems = new HashMap<>(Map.of("torch", 5, "lava_bucket", 10, "soul_torch", -5));
    }

    public static TempConfig getInstance() {
        if (instance == null) {
            synchronized (TempConfig.class) {
                if (instance == null) {
                    instance = new TempConfig();
                }
            }
        }
        return instance;
    }
}