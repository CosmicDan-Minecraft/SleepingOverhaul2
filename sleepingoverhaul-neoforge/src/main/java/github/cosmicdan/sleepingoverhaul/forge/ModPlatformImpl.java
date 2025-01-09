package github.cosmicdan.sleepingoverhaul.forge;

import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.EventHooks;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ModPlatformImpl {

    public static void registerConfigServer(ModConfigSpec spec) {
        SleepingOverhaulNeoForge.CONTAINER.registerConfig(ModConfig.Type.SERVER, spec);
    }

    public static void registerConfigClient(ModConfigSpec spec) {
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
