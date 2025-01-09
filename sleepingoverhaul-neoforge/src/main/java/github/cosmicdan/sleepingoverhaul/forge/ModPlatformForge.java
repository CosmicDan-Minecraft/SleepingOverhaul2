package github.cosmicdan.sleepingoverhaul.forge;

import github.cosmicdan.sleepingoverhaul.IModPlatform;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ModPlatformForge implements IModPlatform {
    @Override
    public void registerConfigServer(ModConfigSpec spec) {
        SleepingOverhaulNeoForge.CONTAINER.registerConfig(ModConfig.Type.SERVER, spec);
    }

    @Override
    public void registerConfigClient(ModConfigSpec spec) {
        SleepingOverhaulNeoForge.CONTAINER.registerConfig(ModConfig.Type.CLIENT, spec);
    }

    /*
    public static boolean canPlayerStartSleepNow(final Player player) {

    }

    public static boolean canPlayerContinueSleepNow(final Player player) {
        // Only works on server side! If called on client, will always return false. All the more reason to use [TODO] Bed Groups.
        // Note that below is what NeoForged does, but we remove the hardcoded check for a BedBlock
        //boolean hasBed = player.getSleepingPos().map(pos -> player.level().getBlockState(pos).isBed(player.level(), pos, player)).orElse(false);
        boolean hasBed = player.getSleepingPos().isPresent();
        return EventHooks.canEntityContinueSleeping(player, hasBed ? null : Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
    }
     */
}
