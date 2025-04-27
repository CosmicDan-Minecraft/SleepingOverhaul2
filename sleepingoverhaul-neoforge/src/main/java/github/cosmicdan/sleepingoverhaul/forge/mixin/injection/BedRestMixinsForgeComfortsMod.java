package github.cosmicdan.sleepingoverhaul.forge.mixin.injection;

import com.illusivesoulworks.comforts.common.block.BaseComfortsBlock;
import com.illusivesoulworks.comforts.platform.services.ISleepEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import github.cosmicdan.sleepingoverhaul.mixin.proxy.PlayerMixinProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class BedRestMixinsForgeComfortsMod {}

@Mixin(BaseComfortsBlock.class)
abstract class BedRestMixinsForgeComfortsModBaseComfortsBlock {
    /**
     * For Bed Rest; force day check to false if the player is not reallySleeping.
     * We perform the real check later in ServerState#tryReallySleepingRecv
     */
    @WrapOperation(
            method = "trySleep",
            at = @At(value = "INVOKE", target = "Lcom/illusivesoulworks/comforts/platform/services/ISleepEvents;isAwakeTime(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;)Z")
    )
    private static boolean onTrySleepTimeCheck(ISleepEvents instance, Player player, BlockPos blockPos, Operation<Boolean> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (!((PlayerMixinProxy) player).so2_$isReallySleeping())
                return false;
        }
        return original.call(instance, player, blockPos);
    }

    /**
     * As above, but for overriding Forge event call instead of hardcoded time check.
     */
    @WrapOperation(
            method = "trySleep",
            at = @At(value = "INVOKE", target = "Lcom/illusivesoulworks/comforts/platform/services/ISleepEvents;getSleepResult(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/core/BlockPos;Lcom/mojang/datafixers/util/Either;)Lcom/mojang/datafixers/util/Either;")
    )
    private static Either<Player.BedSleepingProblem, Unit> onTrySleepGetSleepResult(ISleepEvents instance, ServerPlayer serverPlayer, BlockPos blockPos, Either<Player.BedSleepingProblem, Unit> bedSleepingProblemUnitEither, Operation<Either<Player.BedSleepingProblem, Unit>> original) {
        final Either<Player.BedSleepingProblem, Unit> result = original.call(instance, serverPlayer, blockPos, bedSleepingProblemUnitEither);
        if (result.left().isPresent()) {
            final Player.BedSleepingProblem problem = result.left().get();
            if (problem == Player.BedSleepingProblem.NOT_POSSIBLE_NOW) {
                if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
                    if (!((PlayerMixinProxy) serverPlayer).so2_$isReallySleeping())
                        return Either.right(Unit.INSTANCE);
                }
            }
        }
        return result;
    }
}
