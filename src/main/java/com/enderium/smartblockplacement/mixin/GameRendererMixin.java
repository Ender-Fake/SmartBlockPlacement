package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.data.FastPlaceData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    Minecraft minecraft;


    @Inject(method = "render", at = @At("TAIL"))
    public void tickUse(CallbackInfo ci) {
        if (!FastPlaceData.isEnabled()) return;
        if (minecraft.player == null) return;
        if (minecraft.options.keyUse.isDown() && !minecraft.player.isUsingItem()) {
            if (FastPlaceData.tickPlacement == 0 && FastPlaceData.checkValues()) {
                FastPlaceData.startPlacementDelay();
                ((MinecraftAccessor) minecraft).callStartUseItem();
                FastPlaceData.setPostValues();
            }
        } else FastPlaceData.reset();
    }


}
