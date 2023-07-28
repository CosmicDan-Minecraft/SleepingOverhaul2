package com.cosmicdan.sleepingoverhaul.mixin.injection.client;

import com.cosmicdan.sleepingoverhaul.IClientState;
import com.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@Mixin(Camera.class)
public abstract class CameraMixin {
    private int cinematicSubstage = 0;

    @Shadow protected abstract void move(double d, double e, double f);

    @Shadow protected abstract double getMaxZoom(double d); // reminder: used for chase cam collision

    @Shadow protected abstract void setRotation(float f, float g);

    @Shadow public abstract BlockPos getBlockPosition();

    @SuppressWarnings("MethodWithTooManyParameters")
    @Inject(
            method = "setup",
            at = @At("TAIL"))
    private void afterCameraSetup(final BlockGetter levelIn, final Entity cameraEntity, final boolean isThirdPerson, final boolean isMirrored, final float partialTicks, final CallbackInfo ci) {
        //System.out.println(cameraEntity.getYRot();
        final int cineStage = SleepingOverhaul.clientState.getTimelapseCinematicStage();
        if (cineStage == 1) {
            // just skip to next cine stage for now
            SleepingOverhaul.clientState.advanceTimelapseCinematicStage();
        } else if (cineStage == 2) {
            if (levelIn instanceof ClientLevel level) {
                // level.getSunAngle is weird, so we calculate it ourselves
                final float timeOfDayAsFraction = (level.getDayTime() % 24000L) / 24000.0f;
                // rotate first
                setRotation((timeOfDayAsFraction * 360.0f * 2.0f) - 90, 0.0f);
                // move camera back and up
                move(-6.0, 2.0, 0.0);
                int moveCounter = 0;
                while (moveCounter < 10) {
                    move(-1.0, 1.0, 0.0);
                    if (level.canSeeSky(getBlockPosition()))
                        break;
                    moveCounter++;
                }
                // finally set FoV 90
                Minecraft.getInstance().gameRenderer.setPanoramicMode(true);
            }
        } else if (cineStage == 3) {
            // timelapse was playing but is now ended
            Minecraft.getInstance().gameRenderer.setPanoramicMode(false);
            // just go back to stopped cine stage for now
            SleepingOverhaul.clientState.advanceTimelapseCinematicStage();
        }
    }
}