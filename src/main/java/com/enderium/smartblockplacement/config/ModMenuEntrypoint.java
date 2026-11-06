package com.enderium.smartblockplacement.config;

//? if fabric {
import com.enderium.smartblockplacement.ModLoaderAccess;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
//? if >=1.21.11
import com.terraformersmc.modmenu.util.NullScreenFactory;

public class ModMenuEntrypoint implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (ModLoaderAccess.INSTANCE.isModLoaded("cloth-config")) {
            return SmartBlockPlacementConfigScreen::createScreen;
        } else {
            //? if >=1.21.11 {
            return new NullScreenFactory<>();
            //?} else
            //return null;
        }
    }
}
//?}
