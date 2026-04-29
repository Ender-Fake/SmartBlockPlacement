package com.enderium.smartblockplacement.config;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient;
import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig.Defaults;
import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig.Values;
import com.enderium.smartblockplacement.data.FastPlaceData;
import com.enderium.smartblockplacement.text.TextGetter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public class SmartBlockPlacementConfigScreen {

    public static class Text {
        public static final TextGetter TITLE = TextGetter.of("text.smartblockplacement.config_menu.title");
        public static final TextGetter FAST_PLACE = TextGetter.of("text.smartblockplacement.config_menu.values.fast_place");
        public static final TextGetter FAST_PLACE_TOOLTIP_WARNING = TextGetter.of("text.smartblockplacement.config_menu.values.fast_place.tooltip_warning");
        public static final TextGetter FAST_PLACE_ENABLED = TextGetter.of("text.smartblockplacement.config_menu.values.fast_place.enabled");
        public static final TextGetter FAST_PLACE_DISABLED = TextGetter.of("text.smartblockplacement.config_menu.values.fast_place.disabled");
        public static final TextGetter FAST_PLACE_BUTTON = TextGetter.of("key.smartblockplacement.switch_smart_placement");
        public static final TextGetter PLACE_DELAY = TextGetter.of("text.smartblockplacement.config_menu.values.place_delay");
        public static final TextGetter PLACE_DELAY_VALUE = TextGetter.of("text.smartblockplacement.config_menu.values.place_delay.value");
        public static final TextGetter PLACE_DELAY_TOOLTIP = TextGetter.of("text.smartblockplacement.config_menu.values.place_delay.tooltip");
        public static final TextGetter PLACE_DELAY_TOOLTIP_WARNING = TextGetter.of("text.smartblockplacement.config_menu.values.place_delay.tooltip_warning");
        public static final TextGetter PLACE_DELAY_TOOLTIP_SAVE = TextGetter.of("text.smartblockplacement.config_menu.values.place_delay.tooltip_save");
        public static final TextGetter CREATIVE_ONLY = TextGetter.of("text.smartblockplacement.config_menu.values.creative_only");


    }

    private static final int RED_COLOR = 16733525;
    private static final int DARKNESS_RED_COLOR = new Color(0xD64949).getRGB();
    private static final int YELLOW_COLOR = 16777045;
    private static final int DARKNESS_YELLOW_COLOR = new Color(0xC9BD3A).getRGB();
    private static final int GREEN_COLOR = 5635925;


    public static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.TITLE.get());
        builder.setSavingRunnable(SmartBlockPlacementConfig::save);
        ConfigCategory general = builder.getOrCreateCategory(Text.TITLE.get());
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();


        general.addEntry(entryBuilder.startBooleanToggle(Text.FAST_PLACE.get(), Values.fastPlaceEnabled).setDefaultValue(Defaults.ENABLED_VALUE)
                .setTooltip(Text.FAST_PLACE_TOOLTIP_WARNING.get().withColor(DARKNESS_RED_COLOR))
                .setYesNoTextSupplier(isActive -> isActive ? Text.FAST_PLACE_ENABLED.get().withColor(GREEN_COLOR) : Text.FAST_PLACE_DISABLED.get().withColor(RED_COLOR))
                .setSaveConsumer(replace -> Values.fastPlaceEnabled = replace).build());

        general.addEntry(entryBuilder.fillKeybindingField(Text.FAST_PLACE_BUTTON.get(), SmartBlockPlacementClient.smartPlacementKey).build());


        general.addEntry(entryBuilder.startBooleanToggle(Text.CREATIVE_ONLY.get(), Values.creativeOnly)
                .setDefaultValue(Defaults.CREATIVE_ONLY_VALUE).setSaveConsumer(replace -> Values.creativeOnly = replace).build());


        general.addEntry(entryBuilder.startIntSlider(Text.PLACE_DELAY.get(), FastPlaceData.tickPlacementDelay, 1, 10).setDefaultValue(4)
                .setTooltip(Text.PLACE_DELAY_TOOLTIP.get(), Text.PLACE_DELAY_TOOLTIP_WARNING.get().withColor(DARKNESS_RED_COLOR), Text.PLACE_DELAY_TOOLTIP_SAVE.get().withColor(DARKNESS_YELLOW_COLOR))
                .setTextGetter(Text.PLACE_DELAY_VALUE::get).setSaveConsumer(replace -> FastPlaceData.tickPlacementDelay = replace).build());

        return builder.build();
    }


}
