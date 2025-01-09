package github.cosmicdan.sleepingoverhaul.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import github.cosmicdan.sleepingoverhaul.IModPlatform;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ModPlatformFabric implements IModPlatform {
    @Override
    public void registerConfigServer(ModConfigSpec spec) {
        NeoForgeConfigRegistry.INSTANCE.register(SleepingOverhaul.MOD_ID, ModConfig.Type.SERVER, spec);
    }

    @Override
    public void registerConfigClient(ModConfigSpec spec) {
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
