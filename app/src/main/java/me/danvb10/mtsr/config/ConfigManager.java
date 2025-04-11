package me.danvb10.mtsr.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@SuppressWarnings({"FieldMayBeFinal", "CanBeFinal", "FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
@Config(name = "mtsr")
public class ConfigManager implements ConfigData {

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("mtsr.general")
    public int version = 0;

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("mtsr.general")
    public boolean displayDebugData = false;

    @ConfigEntry.Category("mtsr.general")
    public boolean enabled = true;

}