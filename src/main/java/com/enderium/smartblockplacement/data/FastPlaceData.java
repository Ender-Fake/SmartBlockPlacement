package com.enderium.smartblockplacement.data;

import com.enderium.smartblockplacement.config.SmartBlockPlacementConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
    private static boolean missed;


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
        if (lastUseBlock) return true;
        if (!(player.getMainHandItem().getItem() instanceof BlockItem || player.getOffhandItem().getItem() instanceof BlockItem))
            return true;
        if (!(MC.hitResult instanceof BlockHitResult result)) return true;
        if (result.getType() == HitResult.Type.MISS) {
            missed = true;
            return false;
        }
        if (blockHitEquals(lastBlockHit, result)) return false;
        if (lastDirectionPos == null || !lastDirectionPos.equals(result.getBlockPos())) return true;
        if (lastDirection == null) {
            lastDirection = result.getDirection();
            return false;
        }
        if (lastDirection == result.getDirection())
            return (playerDistanceTo(lastHitPos) > maxDistance);
        return false;
    }


    public static void setPostValues() {
        LocalPlayer player = MC.player;
        assert player != null;
        isStart = true;
        if (MC.hitResult instanceof BlockHitResult result) {
            lastBlockHit = result;


            Vec3 hitPos = lastHitPos;
            lastHitPos = centerVec(result.getLocation());
            lastDirectionPos = result.getBlockPos().relative(result.getDirection());

            boolean forceUpdateDistance = false;

            if (missed) forceUpdateDistance = true;
            else if (hitPos != null) {
                Vec3 sV = lastHitPos.subtract(hitPos);
                if ((sV.x + sV.y + sV.z) > 1) forceUpdateDistance = true;
            }

            if (forceUpdateDistance || maxDistance == 0)
                maxDistance = Math.max(playerDistanceTo(lastHitPos) + 1.5, 1);
            lastDirection = null;
            missed=false;
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

    private static Vec3 centerVec(Vec3 vec) {
        return new Vec3(Math.floor(vec.x) + 0.5f, Math.floor(vec.y) + 0.5f, Math.floor(vec.z) + 0.5f);
    }

    private static double playerDistanceTo(Vec3 vec3) {
        LocalPlayer player = MC.player;
        assert player != null;
        vec3 = vec3.subtract(player.getEyePosition().subtract(player.position()));
        return player.distanceToSqr(vec3);
    }

}
