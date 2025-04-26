package github.cosmicdan.sleepingoverhaul.forge.mixin.injection;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.cosmicdan.sleepingoverhaul.mixin.proxy.PlayerMixinProxy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class BedRestMixinsForge {}

@Mixin(ServerPlayer.class)
abstract class BedRestMixinsForgeServerPlayer {

    /**
     * For Bed Rest; force day check to false if the player is not reallySleeping.
     * We perform the real check later in ServerState#tryReallySleepingRecv
     */
    @WrapOperation(
            method = "lambda$startSleepInBed$13",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDay()Z")
    )
    private boolean onCanPlayerStartSleepingEvent(Level instance, Operation<Boolean> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (!((PlayerMixinProxy) this).so2_$isReallySleeping())
                return false;
        }
        return original.call(instance);
    }
}

@Mixin(Player.class)
abstract class BedRestMixinsForgePlayer {

    /**
     * For Bed Rest; remove canEntityContinueSleeping check during tick but only if they're not reallySleeping.
     */
    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;canEntityContinueSleeping(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)Z", remap = false)
    )
    private boolean onTimeCheck(LivingEntity sleeper, Player.BedSleepingProblem problem, Operation<Boolean> original) {
        if (sleeper instanceof Player player && SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (!((PlayerMixinProxy) player).so2_$isReallySleeping())
                return true;
        }
        return original.call(sleeper, problem);
    }
}
