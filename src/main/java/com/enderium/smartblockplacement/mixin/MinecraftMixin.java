package com.enderium.smartblockplacement.mixin;

import com.enderium.smartblockplacement.client.SmartBlockPlacementClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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

    @Redirect(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack checkItem(LocalPlayer instance, InteractionHand interactionHand){
        ItemStack stack= instance.getItemInHand(interactionHand);
        if (stack.getItem() instanceof BlockItem) {
            SmartBlockPlacementClient.tickPlacement = 0;
        }
        return stack;
    }
}
