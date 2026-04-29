package com.enderium.smartblockplacement.client;

import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig;
import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig.Values;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static com.enderium.smartblockplacement.data.FastPlaceData.tickPlacement;

public class SmartBlockPlacementClient implements ClientModInitializer {
    private static SmartBlockPlacementClient INSTANCE;

    public static final String MODID = "smartblockplacement";
    public static final MutableComponent LABEL = Component.literal("[SmartBlockPlacement]").withColor(ModColors.GREEN_MOD_COLOR);


    public static final String mainCategory = "category.smartblockplacement.main";
    public static final KeyMapping smartPlacementKey = new KeyMapping("key.smartblockplacement.switch_smart_placement", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, mainCategory);


    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        SmartBlockPlacementConfig.load();


        KeyMapping binding = KeyBindingHelper.registerKeyBinding(smartPlacementKey);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (tickPlacement > 0) tickPlacement--;
            if (binding.consumeClick()) {
                Values.fastPlaceEnabled = !Values.fastPlaceEnabled;
                client.gui.setOverlayMessage(Component.translatable("text.smartblockplacement.enabled_smart_placement", Component.translatable("text.smartblockplacement." + Values.fastPlaceEnabled).withColor(Values.fastPlaceEnabled ? ModColors.GREEN_MOD_COLOR : ModColors.RED_MOD_COLOR)).withColor(ModColors.WHITE_MOD_COLOR), false);
                SmartBlockPlacementConfig.save();
            }
        });
    }


    public static SmartBlockPlacementClient getInstance() {
        return INSTANCE;
    }


    public static void sendLiteralToPlayer(DebugType type, String msg) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (type == null) player.sendSystemMessage(LABEL.append(" ").append(Component.literal(msg)));
        else
            player.sendSystemMessage(LABEL.withColor(type.color).append(" ").append(Component.literal(msg)).withColor(type.color));
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
