package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow private int rightClickDelay;

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public HitResult hitResult;

    @Shadow @Final public GameRenderer gameRenderer;

    @Shadow @Final public KeyboardHandler keyboardHandler;
    @Shadow @Final public Options options;

    @Unique
    public BlockPos lastDirectionPos;
    @Unique
    public Direction lastDirection;
    @Unique
    public double lastDistance=0;

    @Inject(method = "handleKeybinds", at = @At(value = "TAIL"))
    public void startHandleKey(CallbackInfo ci){
        if (!options.keyUse.isDown()) {
            lastDirectionPos=null;
            lastDirection=null;
            lastDistance=0;
        }
    }

    @Inject(method = "startUseItem", at = @At(value = "HEAD"), cancellable = true)
    public void checkPlace(CallbackInfo ci) {
        if (SmartBlockPlacementClient.getInstance().enabledSmartPlacement) {
            if (!checkValues()) {
                ci.cancel();
            }

        }
    }

    @Inject(method = "startUseItem", at = @At(value = "RETURN"))
    public void placeValues(CallbackInfo ci) {
        if (SmartBlockPlacementClient.getInstance().enabledSmartPlacement) {
            setPostValues();
        }

    }
    @Unique
    public boolean checkValues(){
        if (hitResult instanceof BlockHitResult result) {
            if (lastDirectionPos!=null&&lastDirectionPos.equals(result.getBlockPos())) {
                if (lastDirection == null) {
                    lastDirection = result.getDirection();
                }
                assert player != null;
                if (lastDirection == result.getDirection()) {
                    if (result.getBlockPos().getCenter().distanceTo(player.getEyePosition())-lastDistance<0.6) {
                        rightClickDelay = 0;
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Unique
    public void setPostValues(){
        assert player != null;
        if (hitResult instanceof BlockHitResult result) {
            lastDirectionPos = result.getBlockPos().relative(result.getDirection());
            lastDistance =Math.min(player.getEyePosition().distanceTo(lastDirectionPos.getCenter()),4);
            lastDirection=null;
        }
        rightClickDelay = 0;
    }
}
