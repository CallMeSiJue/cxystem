package cxy.cxystem.config;

import cxy.cxystem.CxysTem;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;

public class ModConfig extends Config {

    @ConfigEntry(comment = "默认的玩家适应温度范围. (Default: 8)")
    public double baseAdaptTemp = 8;

    public ModConfig() {
        super(ConfigOptions.mod(CxysTem.MOD_ID));
    }
}
