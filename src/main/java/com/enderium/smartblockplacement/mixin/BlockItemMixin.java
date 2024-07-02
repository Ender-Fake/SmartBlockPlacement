package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {


    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;"))
    public void use(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir){
        //if (cir.getReturnValue().consumesAction())
        SmartBlockPlacementClient.tickPlacement = 0;
    }
}
