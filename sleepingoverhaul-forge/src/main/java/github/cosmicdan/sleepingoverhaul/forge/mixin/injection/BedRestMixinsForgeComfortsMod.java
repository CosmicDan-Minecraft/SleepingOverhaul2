package github.cosmicdan.sleepingoverhaul.forge.mixin.injection;

import com.illusivesoulworks.comforts.common.block.BaseComfortsBlock;
import com.illusivesoulworks.comforts.platform.services.ISleepEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import github.cosmicdan.sleepingoverhaul.mixin.proxy.PlayerMixinProxy;
import net.minecraft.core.BlockPos;
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
            at = @At(value = "INVOKE", target = "Lcom/illusivesoulworks/comforts/platform/services/ISleepEvents;isAwakeTime(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;)Z"),
            remap = false
    )
    private static boolean onTrySleepTimeCheck(ISleepEvents instance, Player player, BlockPos blockPos, Operation<Boolean> original) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (!((PlayerMixinProxy) player).so2_$isReallySleeping())
                return false;
        }
        return original.call(instance, player, blockPos);
    }
}
