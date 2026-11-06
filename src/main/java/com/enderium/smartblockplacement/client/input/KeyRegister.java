package com.enderium.smartblockplacement.client.input;

import net.minecraft.client.KeyMapping;

public class KeyRegister {



    public static void register(KeyMapping mapping){
        //? if fabric{

        //? if >=26.1 {
        net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping(mapping);
        //?} else 
        //net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(mapping);


        //?}
    }


}
