package github.cosmicdan.sleepingoverhaul.forge.mixin.injection;

import com.mojang.datafixers.util.Either;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

public class BedRestMixinsForge {}

@Mixin(ServerPlayer.class)
abstract class BedRestMixinsForgeServerPlayer {

    /**
     * For Bed Rest, remove isDay check during tick. We perform the check later in ServerState#onReallySleepingRecv
     */
    @WrapOperation(
            method = "startSleepInBed",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;canPlayerStartSleeping(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/core/BlockPos;Lcom/mojang/datafixers/util/Either;)Lcom/mojang/datafixers/util/Either;", remap = false)
    )
    private Either<Player.BedSleepingProblem, Unit> onTimeCheck(ServerPlayer player, BlockPos pos, Either<Player.BedSleepingProblem, Unit> vanillaResult, Operation<Either<Player.BedSleepingProblem, Unit>> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get())
            return null;
        else
            return original.call(player, pos, vanillaResult);
    }
}

@Mixin(Player.class)
abstract class BedRestMixinsForgePlayer {

    /**
     * For Bed Rest, remove isDay check during tick. We perform the check later in ServerState#onReallySleepingRecv
     */
    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;canEntityContinueSleeping(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)Z", remap = false)
    )
    private boolean onTimeCheck(LivingEntity sleeper, Player.BedSleepingProblem problem, Operation<Boolean> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get())
            return true;
        else
            return original.call(sleeper, problem);
    }
}
