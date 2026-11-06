package com.enderium.smartblockplacement.client;

//? if fabric {
import com.enderium.smartblockplacement.client.input.KeyRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SmartBlockPlacementClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SmartBlockPlacementClient.init();

        KeyRegister.register(SmartBlockPlacementClient.smartPlacementKey);
        ClientTickEvents.END_CLIENT_TICK.register(SmartBlockPlacementClient::clientTick);
    }
}
//?}
