package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.data.FastPlaceData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {


    //? if >26.1 {
    @Inject(method = "performUseItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResult;consumesAction()Z"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;useWithoutItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;")))
    public void useBlock(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "use") InteractionResult use) {
        if (Minecraft.getInstance().player == player) FastPlaceData.lastUseBlock = use.consumesAction();
    }
    //?} else {

    /*@Inject(method = "performUseItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResult;consumesAction()Z"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;useWithoutItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;")))
    public void useBlock(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 0) InteractionResult result) {
        if (Minecraft.getInstance().player == player) FastPlaceData.lastUseBlock = result.consumesAction();
    }
    *///?}

}
