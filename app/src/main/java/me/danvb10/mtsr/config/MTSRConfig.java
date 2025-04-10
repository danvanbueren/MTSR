package me.danvb10.mtsr.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "mtsr")
public class MTSRConfig implements ConfigData {
    public boolean enableUpscaling = true;
    public boolean upscaleUI = true;
    public boolean upscaleParticles = true;
    public boolean excludeFonts = true;
    public int scaleFactor = 4;
    public String modelPath = "esrgan/models/4x-mtsr.pth";

    public static MTSRConfig get() {
        return AutoConfig.getConfigHolder(MTSRConfig.class).getConfig();
    }

    public static void init() {
        AutoConfig.register(MTSRConfig.class, Toml4jConfigSerializer::new);
    }
}
