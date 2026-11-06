package com.enderium.smartblockplacement.client;


//? if neoforge {
/*import com.enderium.smartblockplacement.config.SmartBlockPlacementConfigScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "smartblockplacement", dist = Dist.CLIENT)
public class SmartBlockPlacementClientNeoForge {


    public SmartBlockPlacementClientNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        SmartBlockPlacementClient.init();


        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, modListScreen) -> SmartBlockPlacementConfigScreen.createScreen(modListScreen));

        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, event -> {
            SmartBlockPlacementClient.clientTick(Minecraft.getInstance());
        });



    }


}
*///?}
