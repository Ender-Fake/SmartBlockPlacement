package com.enderium.smartblockplacement.client;

import com.enderium.smartblockplacement.SmartBlockPlacementCommon;
import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig;
import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig.Values;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;

import java.awt.*;

import static com.enderium.smartblockplacement.SmartBlockPlacementCommon.LABEL;
import static com.enderium.smartblockplacement.data.FastPlaceData.tickPlacement;
import static com.enderium.smartblockplacement.util.ComponentUtils.withColor;

public class SmartBlockPlacementClient {




    //? if >=1.21.11 {
    public static final KeyMapping.Category mainCategory = KeyMapping.Category.register(SmartBlockPlacementCommon.modId("main"));
    //?} else
    //public static final String mainCategory = "category.smartblockplacement.main";

    public static final KeyMapping smartPlacementKey = new KeyMapping("key.smartblockplacement.switch_smart_placement", InputConstants.KEY_GRAVE, mainCategory);


    public static void init(){
        SmartBlockPlacementConfig.load();

    }




    public static void clientTick(Minecraft client){
        if (tickPlacement > 0) tickPlacement--;
        if (smartPlacementKey.consumeClick()) {
            Values.fastPlaceEnabled = !Values.fastPlaceEnabled;

            MutableComponent enableComponent=withColor(Component.translatable("text.smartblockplacement." + Values.fastPlaceEnabled),Values.fastPlaceEnabled ? ModColors.GREEN_MOD_COLOR : ModColors.RED_MOD_COLOR);
            MutableComponent component = withColor(Component.translatable("text.smartblockplacement.enabled_smart_placement", enableComponent),ModColors.WHITE_MOD_COLOR);
            //? if >=26.2 {
            client.gui.hud.setOverlayMessage(component, false);
            //?} else
            //client.gui.setOverlayMessage(component, false);
            SmartBlockPlacementConfig.save();
        }
    }


    public static void sendLiteralToPlayer(DebugType type, String msg) {
        ChatListener listener;
        //? if >=26.2 {
        listener = Minecraft.getInstance().gui.chatListener();
        //?} else
        //listener = Minecraft.getInstance().getChatListener();

        if (type == null) listener.handleSystemMessage(LABEL.append(" ").append(Component.literal(msg)),false);
        else
            listener.handleSystemMessage(withColor(withColor(LABEL,type.color).append(" ").append(Component.literal(msg)),type.color), false);
    }

    public enum DebugType {
        INFO(new Color(211, 255, 198).getRGB()),
        WARNING(DyeColor.YELLOW),
        ERROR(DyeColor.RED);

        public final int color;

        DebugType(DyeColor color) {
            this.color = color.getTextColor();
        }

        DebugType(int color) {
            this.color = color;
        }

    }

}
