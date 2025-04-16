package me.danvb10.mtsr.config.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.CollapsibleContainer;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import me.danvb10.mtsr.config.ConfigScreen;
import me.danvb10.mtsr.config.ConfigScreenRichWindowMaximized;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.UUID;

import static me.danvb10.mtsr.ClientEntrypoint.LOGGER;

public class RichWindow {

    private String windowName, windowTooltip;
    private final UUID uuid;
    private final ConfigScreen owner;
    private final RichWindowTypes richWindowType;
    private boolean fullHeight, maximized, minimizeIsDisabled;
    private final ArrayList<Component> children, prerequisiteChildren;
    private CollapsibleContainer hideableComponent, hideableComponentContentWhileHidden;

    // Constructor
    public RichWindow(ConfigScreen owner, RichWindowTypes richWindowType) {
        this.owner = owner;
        this.richWindowType = richWindowType;
        this.setWindowProps();
        this.uuid = UUID.randomUUID();

        this.fullHeight = false;
        this.maximized = false;
        this.minimizeIsDisabled = false;

        this.children = new ArrayList<>();
        this.prerequisiteChildren = buildPrerequisiteChildren();
    }

    // Assign title and tooltip
    private void setWindowProps() {
        switch (richWindowType) {
            case GENERAL_SETTINGS_WINDOW:
                this.windowName = "General Settings";
                this.windowTooltip = "...";
                break;
            case MODEL_SETTINGS_WINDOW:
                this.windowName = "Model Settings";
                this.windowTooltip = "...";
                break;
            case QUICK_ACTIONS_WINDOW:
                this.windowName = "Quick Actions";
                this.windowTooltip = "...";
                break;
            case TEXTURE_MANAGER_WINDOW:
                this.windowName = "Texture Manager";
                this.windowTooltip = "...";
                break;
            case ACTIVITY_MONITOR_WINDOW:
                this.windowName = "Activity Monitor";
                this.windowTooltip = "...";
                break;
            case ACTIVITY_LOG_WINDOW:
                this.windowName = "Activity Log";
                this.windowTooltip = "...";
                break;
            default:
                this.windowName = "Unknown Window";
                this.windowTooltip = "...";
                break;
        }

    }

    // Helper method for console logging
    private void log(String message) {
        LOGGER.info("RichWindow[" + this.uuid.toString() + "]: " + message);
    }

    // Build title bar component prefix
    private ArrayList<Component> buildPrerequisiteChildren() {
        Component title =
                Components.label(Text.literal(this.windowName))
                        .tooltip(Text.literal(this.windowTooltip))
                ;

        Component minButton =
                Components.button(Text.literal("-"), button -> {this.hideableComponent.toggleExpansion();})
                        .verticalSizing(Sizing.fixed(10))
                        .horizontalSizing(Sizing.fixed(10))
                        .margins(Insets.right(5))
                ;

        Component maxButton =
                Components.button(Text.literal("[]"), this::toggleMaximized)
                        .verticalSizing(Sizing.fixed(10))
                        .horizontalSizing(Sizing.fixed(10))
                ;

        FlowLayout minMaxButtons = Containers.horizontalFlow(Sizing.content(), Sizing.content());
        if (!minimizeIsDisabled) minMaxButtons.child(minButton);
        minMaxButtons.child(maxButton);
        minMaxButtons.horizontalAlignment(HorizontalAlignment.RIGHT);

        Component parentTitle =
                Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .child(
                                Containers.horizontalFlow(Sizing.fill(70), Sizing.content())
                                        .child(title)
                        )
                        .child(
                                Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                                        .child(minMaxButtons)
                                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        )
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .verticalAlignment(VerticalAlignment.CENTER)
                ;

        Component titleDivider =
                Components.box(Sizing.fill(100), Sizing.fixed(1))
                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
                        .margins(Insets.vertical(5))
                ;

        ArrayList<Component> c = new ArrayList<>();
        c.add(parentTitle);
        c.add(titleDivider);
        return c;
    }

    // Handle toggle maximization
    private void toggleMaximized(ButtonComponent button) {
        assert MinecraftClient.getInstance() != null;

        if (this.maximized) {
            log("Minimizing " + this.richWindowType);

            // Minimize
            Screen configScreen = new ConfigScreen(this.owner);
            MinecraftClient.getInstance().setScreen(configScreen);
        } else {
            log("Maximizing " + this.richWindowType);

            // Maximize
            RichWindow richWindow = new RichWindow(this.owner, this.richWindowType)
                    .setFullHeight(true)
                    .setMaximized(true)
                    .setMinimizeIsDisabled(true);

            Screen maxedScreen = new ConfigScreenRichWindowMaximized(this.owner)
                    .child(richWindow.build());

            MinecraftClient.getInstance().setScreen(maxedScreen);
        }
    }

    // Build and return full component
    public Component build() {

        FlowLayout flowLayout;
        if (fullHeight) {
            flowLayout = Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100));
        } else {
            flowLayout = Containers.verticalFlow(Sizing.fill(100), Sizing.content());
        }

        ArrayList<Component> c = new ArrayList<>(this.prerequisiteChildren);

        this.hideableComponent = (CollapsibleContainer) Containers
                .collapsible(Sizing.content(), Sizing.content(), Text.literal(""), true)
                .children(this.children);

        c.add(this.hideableComponent);

        ParentComponent rootLayout = flowLayout.children(c);

        rootLayout
                .padding(Insets.of(8))
                .surface(Surface.DARK_PANEL);

        return rootLayout.margins(Insets.of(0, 4, 0, 0));
    }

    // Getters & Setters
    public boolean isFullHeight() {
        return this.fullHeight;
    }
    public RichWindow setFullHeight(boolean fullHeight) {
        this.fullHeight = fullHeight;
        return this;
    }
    public boolean isMaximized() {
        return this.maximized;
    }
    public RichWindow setMaximized(boolean maximized) {
        this.maximized = maximized;
        return this;
    }
    public boolean isMinimizeIsDisabled() {
        return this.minimizeIsDisabled;
    }
    public RichWindow setMinimizeIsDisabled(boolean minimizeIsDisabled) {
        this.minimizeIsDisabled = minimizeIsDisabled;
        return this;
    }

    // Helper method to add children
    public RichWindow child(Component component) {
        this.children.add(component);
        return this;
    }

}
