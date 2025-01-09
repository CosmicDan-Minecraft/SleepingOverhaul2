package github.cosmicdan.sleepingoverhaul.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import github.cosmicdan.sleepingoverhaul.ModPlatform;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ModPlatformImpl {
    public static void registerConfigServer(ModConfigSpec spec) {
        NeoForgeConfigRegistry.INSTANCE.register(SleepingOverhaul.MOD_ID, ModConfig.Type.SERVER, spec);
    }

    public static void registerConfigClient(ModConfigSpec spec) {
        NeoForgeConfigRegistry.INSTANCE.register(SleepingOverhaul.MOD_ID, ModConfig.Type.CLIENT, spec);
    }

    /*
    public static boolean canPlayerSleepNow(final Player player) {
        // TODO: Bed Groups
        boolean isDay = player.level().isDay();
        final Optional<BlockPos> bedPosMaybe = player.getSleepingPos();
        if (bedPosMaybe.isPresent()) {
            final BlockPos bedPos = bedPosMaybe.get();
            // Probably only work on server-side (same as Forge, vanilla MC thing). All the more reason for Bed Groups.
            InteractionResult result = EntitySleepEvents.ALLOW_SLEEP_TIME.invoker().allowSleepTime(player, bedPos, !isDay);
            if (result != InteractionResult.PASS) {
                return result.consumesAction();
            }
        }
        return !isDay;
    }
     */
}
