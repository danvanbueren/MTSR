package me.danvb10.mtsr.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    public boolean myBooleanOption;

    public ModMenuIntegration() {
        this.myBooleanOption = true;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.of("Title1"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Category2"))
                        .tooltip(Text.of("Tooltip3"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Group4"))
                                .description(OptionDescription.of(Text.of("Description5")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Boolean Option6"))
                                        .description(OptionDescription.of(Text.of("Tooltip7")))
                                        .binding(true, () -> this.myBooleanOption, newVal -> this.myBooleanOption = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}