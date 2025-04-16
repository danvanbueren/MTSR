package me.danvb10.mtsr.config.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.core.Component;
import me.danvb10.mtsr.config.ConfigScreen;
import net.minecraft.text.Text;

import static me.danvb10.mtsr.config.components.RichWindowTypes.*;

public class ActivityMonitorWindow {
    private final RichWindow richWindow;
    private boolean fullscreen;

    public ActivityMonitorWindow(ConfigScreen parent) {
        this.richWindow = new RichWindow(parent, ACTIVITY_MONITOR_WINDOW);
    }

    public Component build() {
        richWindow
                .setWindowName("Activity Monitor")
                .setWindowTooltip("Activity Monitor")
                .setFullHeight(true)
                .setMinimizeIsDisabled(true);

        if (fullscreen) richWindow.setMaximized(true);

        richWindow
                .child(Components.label(Text.literal("a child")));

        return richWindow.build();
    }

    // Getters & Setters
    public boolean isFullscreen() {
        return fullscreen;
    }
    public ActivityMonitorWindow setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }
}
