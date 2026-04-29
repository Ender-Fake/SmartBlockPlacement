package com.enderium.smartblockplacement.data;

import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class FastPlaceData {

    private static final Minecraft MC = Minecraft.getInstance();
    public static int tickPlacement = 0;
    public static int tickPlacementDelay = 4;

    public static boolean lastUseBlock = false;

    private static boolean isStart;

    private static BlockHitResult lastBlockHit;
    private static Vec3 lastHitPos;
    private static BlockPos lastDirectionPos;
    private static Direction lastDirection;
    private static double maxDistance = 0;


    public static boolean isEnabled() {
        if (SmartBlockPlacementConfig.Values.creativeOnly && (MC.gameMode == null || !MC.gameMode.getPlayerMode().isCreative()))
            return false;
        return SmartBlockPlacementConfig.Values.fastPlaceEnabled;
    }

    public static void startPlacementDelay() {
        tickPlacement = tickPlacementDelay;
    }


    public static boolean checkValues() {
        LocalPlayer player = MC.player;
        assert player != null;
        if (FastPlaceData.lastUseBlock) return true;
        if (!(player.getMainHandItem().getItem() instanceof BlockItem || player.getOffhandItem().getItem() instanceof BlockItem))
            return true;
        if (!(MC.hitResult instanceof BlockHitResult result)) return true;
        if (blockHitEquals(lastBlockHit, result)) return false;
        if (lastDirectionPos == null || !lastDirectionPos.equals(result.getBlockPos())) return true;
        if (lastDirection == null) {
            lastDirection = result.getDirection();
            return false;
        }
        if (lastDirection == result.getDirection())
            return (lastHitPos.distanceToSqr(player.getEyePosition()) > maxDistance);
        return false;
    }


    public static void setPostValues() {
        assert MC.player != null;
        isStart = true;
        if (MC.hitResult instanceof BlockHitResult result) {
            lastBlockHit = result;
            lastHitPos = result.getBlockPos().getCenter();
            lastDirectionPos = result.getBlockPos().relative(result.getDirection());
            if (maxDistance == 0)
                maxDistance = Math.max(lastHitPos.distanceToSqr(MC.player.getEyePosition()) + 1.5, 1);
            lastDirection = null;
        }
    }

    public static void reset() {
        if (!isStart) return;
        isStart = false;
        lastBlockHit = null;
        lastHitPos = null;
        lastDirectionPos = null;
        lastDirection = null;
        maxDistance = 0;
        FastPlaceData.tickPlacement = 0;

    }

    private static boolean blockHitEquals(BlockHitResult a, BlockHitResult b) {
        if (a == null || b == null) return false;
        return a.getBlockPos().equals(b.getBlockPos()) && a.getDirection() == b.getDirection();
    }

}
