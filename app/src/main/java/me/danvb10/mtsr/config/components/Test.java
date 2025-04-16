package me.danvb10.mtsr.config.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import net.minecraft.text.Text;

public class Test {

    public static FlowLayout getTest() {
        FlowLayout test = Containers.verticalFlow(Sizing.content(5), Sizing.content(5));

        test.child(
                Components.label(
                        Text.literal(
                                "Header"
                        )
                )
        );

        test.child(
                Components.checkbox(
                        Text.literal("Example Checkbox1")
                )
        ).child(
                Components.checkbox(
                        Text.literal("Example Checkbox2")
                )
        ).child(
                Components.discreteSlider(
                        Sizing.fill(50), 10, 50
                )
        ).child(
                Components.dropdown(Sizing.content())
                        .checkbox(
                                Text.literal("Example Checkbox in Dropdown"),
                                true,
                                a -> {
                                }
                        )
                        .divider()
                        .text(
                                Text.literal("Example Text in Dropdown")
                        )
                        .button(
                                Text.literal("Example Button in Dropdown"),
                                a -> {
                                }
                        )
        ).child(
                Components.label(
                                Text.literal("Example Label")
                        )
                        .tooltip(Text.literal("Example Tooltip for Label"))
        ).child(
                Components.box(Sizing.fill(50), Sizing.fill(10))
        ).child(
                Containers.horizontalFlow(Sizing.fill(50), Sizing.fill(10))

                        .child(
                                Components.textBox(
                                                Sizing.fill(80),
                                                "Example Textbox"
                                        )
                                        .sizing(Sizing.fill(80), Sizing.fill(100))
                        ).child(
                                Components.button(
                                                Text.literal("Example Button"),
                                                a -> {}
                                        )
                                        .sizing(Sizing.fill(20), Sizing.fill(100))
                        ).padding(Insets.of(10))
                        .surface(Surface.DARK_PANEL)
        ).child(
                Components.button(
                        Text.literal("Example Button"),
                        a -> { a.active(false); }
                )
        );

        return test;
    }
}