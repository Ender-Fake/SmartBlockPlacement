package com.enderium.smartblockplacement.util;

import net.minecraft.network.chat.MutableComponent;

public class ComponentUtils {


    public static MutableComponent withColor(MutableComponent component, int color){
        return component.withStyle(style -> style.withColor(color));
    }

}
