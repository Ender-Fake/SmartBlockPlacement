package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin{

    @Shadow @Final
    Minecraft minecraft;

    @Unique
    public BlockHitResult lastBlockHit;
    @Unique
    public BlockPos lastDirectionPos;
    @Unique
    public Direction lastDirection;
    @Unique
    public double lastDistance=0;

    @Inject(method = "render", at = @At("TAIL"))
    public void tickUse(CallbackInfo ci){
        if (!SmartBlockPlacementClient.getEnabledSmartPlacement())return;
        if (minecraft.options.keyUse.isDown()){
            if (SmartBlockPlacementClient.tickPlacement==0&&checkValues()) {
                SmartBlockPlacementClient.tickPlacement = 4;
                ((MinecraftAccessor)minecraft).callStartUseItem();
                setPostValues();

            }
        }
        else  {
            lastBlockHit=null;
            lastDirectionPos=null;
            lastDirection=null;
            lastDistance=0;
        }
    }




    @Unique
    public boolean checkValues(){
        if (minecraft.hitResult instanceof BlockHitResult result) {
            if (lastBlockHit!=null&&lastBlockHit==result) {
                return false;
            }
            if (lastDirectionPos!=null&&lastDirectionPos.equals(result.getBlockPos())) {
                if (lastDirection == null) {
                    lastDirection = result.getDirection();
                }
                assert minecraft.player != null;
                if (lastDirection == result.getDirection()) {
                    return !(result.getBlockPos().getCenter().distanceTo(minecraft.player.getEyePosition()) - lastDistance < 0.6);
                }
            }
        }
        return true;
    }

    @Unique
    public void setPostValues(){
        assert minecraft.player != null;
        if (minecraft.hitResult instanceof BlockHitResult result) {
            lastBlockHit=result;
            lastDirectionPos = result.getBlockPos().relative(result.getDirection());
            lastDistance =Math.min(minecraft.player.getEyePosition().distanceTo(lastDirectionPos.getCenter()),4);
            lastDirection=null;
        }
    }
}
