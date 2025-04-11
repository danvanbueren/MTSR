package me.danvb10.mtsr.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.danvb10.mtsr.config.ConfigManager;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import static me.danvb10.mtsr.ClientEntrypoint.LOGGER;

public class Config {

    public static ConfigManager config;
    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private static ConfigHolder<ConfigManager> configHolder;

    public static void init() {

        configHolder = AutoConfig.register(ConfigManager.class, (definition, configClass) -> new GsonConfigSerializer<>(definition, configClass, gson));
        config = configHolder.getConfig();

        LOGGER.debug("Loaded configuration.");
    }

    public static void save() {
        configHolder.save();
    }

}