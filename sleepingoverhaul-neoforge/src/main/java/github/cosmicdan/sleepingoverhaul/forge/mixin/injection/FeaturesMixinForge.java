package github.cosmicdan.sleepingoverhaul.forge.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

public class FeaturesMixinForge {}

@Mixin(ServerPlayer.class)
abstract class FeaturesMixinsForgeServerPlayer{
    @Shadow
    public abstract ServerLevel serverLevel();

    /**
     * For feature to allow rest/sleep in any dimension
     */
    @WrapOperation(
            method = "lambda$startSleepInBed$13",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;natural()Z")
    )
    private boolean onStartSleepInBedNaturalDimensionCheck(DimensionType instance, Operation<Boolean> original) {
        boolean isNatural = instance.natural();
        if (!isNatural && SleepingOverhaul.serverConfig.featureAllowAnyDimension.get())
            isNatural = true;
        return isNatural;
    }

    /**
     * For feature to allow setting spawn in any dimension. Note that this is a *block* to the call; the actual check for setting spawn was done in BedBlock
     */
    @WrapOperation(
            method = "lambda$startSleepInBed$13",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/BlockPos;FZZ)V")
    )
    private void onStartSleepInBedSetRespawn(ServerPlayer instance, ResourceKey<Level> levelResourceKey, BlockPos dimension, float position, boolean angle, boolean forced, Operation<Void> original) {
        // note that the original call usually DOES set spawn since BedBlock does the check itself, so this acts as "block" if the feature is *not* enabled
        boolean canSetSpawn = serverLevel().dimensionType().bedWorks();
        if (!canSetSpawn && SleepingOverhaul.serverConfig.featureSetSpawnAnyDimension.get())
            canSetSpawn = true;
        if (canSetSpawn)
            original.call(instance, levelResourceKey, dimension, position, angle, forced);
    }
}
