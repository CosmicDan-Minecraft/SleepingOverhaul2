package github.cosmicdan.sleepingoverhaul.forge.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import github.cosmicdan.sleepingoverhaul.mixin.proxy.PlayerMixinProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.theillusivec4.comforts.common.block.ComfortsBaseBlock;

import java.util.Optional;

public class BedRestMixinsForgeComfortsMod {}

@Mixin(ComfortsBaseBlock.class)
abstract class BedRestMixinsForgeComfortsModBaseComfortsBlock {
    /**
     * For Bed Rest; force day check to false if the player is not reallySleeping.
     * We perform the real check later in ServerState#tryReallySleepingRecv
     */
    @WrapOperation(
            method = "trySleep",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onPlayerSleepInBed(Lnet/minecraft/world/entity/player/Player;Ljava/util/Optional;)Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;"),
            remap = false
    )
    private static Player.BedSleepingProblem onTrySleepFirstCheck(Player player, Optional<BlockPos> pos, Operation<Player.BedSleepingProblem> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (!((PlayerMixinProxy) player).so2_$isReallySleeping())
                return null;
        }
        return original.call(player, pos);
    }

    /**
     * For Bed Rest; force day check to false if the player is not reallySleeping.
     * We perform the real check later in ServerState#tryReallySleepingRecv
     */
    @WrapOperation(
            method = "trySleep",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;fireSleepingTimeCheck(Lnet/minecraft/world/entity/player/Player;Ljava/util/Optional;)Z"),
            remap = false
    )
    private static boolean onTrySleepTimeCheck(Player player, Optional<BlockPos> sleepingLocation, Operation<Boolean> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (!((PlayerMixinProxy) player).so2_$isReallySleeping())
                return true;
        }
        return original.call(player, sleepingLocation);
    }
}
