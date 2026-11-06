package com.enderium.smartblockplacement;

import com.enderium.smartblockplacement.client.ModColors;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.enderium.smartblockplacement.util.ComponentUtils.withColor;

public class SmartBlockPlacementCommon {

    public static final String MODID = "smartblockplacement";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = /*$ mod_version*/ "2.1";
    public static final String MINECRAFT = /*$ minecraft*/ "26.3";

    public static final MutableComponent LABEL = withColor(Component.literal("[SmartBlockPlacement]"),ModColors.GREEN_MOD_COLOR);



    public static Identifier modId(String path){
        return MCCommon.id(MODID,path);
    }

}
