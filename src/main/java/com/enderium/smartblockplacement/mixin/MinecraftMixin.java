package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;startUseItem()V"), cancellable = true)
    public void cancelUse(CallbackInfo ci){
        if (!SmartBlockPlacementClient.getEnabledSmartPlacement())return;
        ci.cancel();
    }

    @Redirect(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"))
    public InteractionResult checkItemd(MultiPlayerGameMode instance, LocalPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult){
        InteractionResult result=instance.useItemOn(localPlayer, interactionHand, blockHitResult);
        if (result.consumesAction())SmartBlockPlacementClient.tickPlacement = 0;
        return result;
    }
}
