package com.enderium.smartblockplacement.config;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient.DebugType;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static com.enderium.smartblockplacement.client.SmartBlockPlacementClient.MODID;
import static com.enderium.smartblockplacement.client.SmartBlockPlacementClient.sendLiteralToPlayer;

public class SmartBlockPlacementConfig {
    private static final Logger log = LoggerFactory.getLogger(SmartBlockPlacementConfig.class);

    public static class Keys {

        public static final String ENABLED_KEY = "fastplace-enabled";
        public static final String CREATIVE_ONLY_KEY = "creative-only";
    }

    public static class Defaults {

        public static final boolean ENABLED_VALUE = true;
        public static final boolean CREATIVE_ONLY_VALUE = false;
    }


    public static class Values {

        public static boolean fastPlaceEnabled = Defaults.ENABLED_VALUE;
        public static boolean creativeOnly = Defaults.CREATIVE_ONLY_VALUE;
    }


    public static void save() {
        Path path = getConfigPath();

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            Properties properties = new Properties();

            properties.setProperty(Keys.ENABLED_KEY, String.valueOf(Values.fastPlaceEnabled));
            properties.setProperty(Keys.CREATIVE_ONLY_KEY, String.valueOf(Values.creativeOnly));


            properties.store(writer, null);
        } catch (IOException e) {
            log.error("Error saving configuration", e);
            sendLiteralToPlayer(DebugType.ERROR, "Error saving configuration");
        }


    }

    public static void load() {
        Path path = getConfigPath();
        if (Files.notExists(path)) save();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Properties properties = new Properties();
            properties.load(reader);

            Values.fastPlaceEnabled = Boolean.parseBoolean(properties.getProperty(Keys.ENABLED_KEY, String.valueOf(Defaults.ENABLED_VALUE)));
            Values.creativeOnly = Boolean.parseBoolean(properties.getProperty(Keys.CREATIVE_ONLY_KEY, String.valueOf(Defaults.CREATIVE_ONLY_VALUE)));

        } catch (IOException e) {
            log.error("Error loading configuration", e);
            sendLiteralToPlayer(DebugType.ERROR, "Error loading configuration");
        }


    }

    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(MODID + ".properties");
    }


}
