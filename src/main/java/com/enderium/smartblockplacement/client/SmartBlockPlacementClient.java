package com.enderium.smartblockplacement.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SmartBlockPlacementClient implements ClientModInitializer {
    private static SmartBlockPlacementClient INSTANCE;
    public static long pluginTick = 0;
    public static final String MODID = "smartblockplacement";
    private static File fileConfigDir;
    public boolean enabledSmartPlacement = false;
    public static int tickPlacement = 0;
    public static final KeyMapping.Category MainCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MODID,"main"));
    //private HashMap<Long, Queue<Runnable>> executorMap = new HashMap<>();

    /*public void addExecutor(Runnable runnable){
        addExecutor(0,runnable);
    }
    public void addExecutor(long tick,Runnable runnable){
        tick+=pluginTick;
        executorMap.putIfAbsent(tick, new ConcurrentLinkedQueue<>());
        executorMap.get(tick).add(runnable);
    }*/

    @Override
    public void onInitializeClient () {
        INSTANCE = this;

        fileConfigDir=FabricLoader.getInstance().getConfigDir().resolve(MODID +".json").toFile();
        enabledSmartPlacement=getEnabledSmartPlacement();

        KeyMapping binding = KeyBindingHelper.registerKeyBinding(new KeyMapping("key."+ MODID +".switch_smart_placement", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, MainCategory));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            pluginTick++;
            if (tickPlacement>0) tickPlacement--;
            if (binding.consumeClick()) {
                enabledSmartPlacement = !enabledSmartPlacement;


                client.gui.setOverlayMessage(Component.translatable("text.smartblockplacement.enabled_smart_placement",Component.translatable("text.smartblockplacement."+enabledSmartPlacement)), false);
                setEnabledSmartPlacement(enabledSmartPlacement);
            }
            /*if (enabledSmartPlacement){
                executorMap.keySet().stream().sorted().forEach(tick -> {
                    if (tick<=pluginTick) {
                        Queue<Runnable> queue=executorMap.remove(tick);
                        if (queue != null){
                            while (!queue.isEmpty()) {
                                queue.poll().run();
                            }
                        };

                    }
                });

            }*/
        });
    }


    public static SmartBlockPlacementClient getInstance() {
        return INSTANCE;
    }
    public static String getModId () {
        return MODID;
    }

    public static File getFileConfigDir () {
        return fileConfigDir;
    }

    public static void setEnabledSmartPlacement(boolean enable) {
        if (!fileConfigDir.exists()) {
            try {
                fileConfigDir.createNewFile();
            } catch (IOException e) {
                return;
            }
        }
        try {
            JsonWriter writer1=new JsonWriter(new FileWriter(fileConfigDir));
            writer1.jsonValue("{\"enabled\":"+enable+"}");
            writer1.flush();
            writer1.close();
        } catch (IOException ignored) {

        }
    }
    public static boolean getEnabledSmartPlacement() {
        try (JsonReader reader = new JsonReader(new FileReader(fileConfigDir))) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
            return obj.has("enabled") && obj.get("enabled").getAsBoolean();
        } catch (IOException ignored) {
            return false;
        }
    }
}
