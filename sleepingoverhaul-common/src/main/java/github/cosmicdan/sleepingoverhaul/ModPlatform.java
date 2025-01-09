package github.cosmicdan.sleepingoverhaul;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ModPlatform {
    @ExpectPlatform
    public static void registerConfigServer(ModConfigSpec spec) {}

    @ExpectPlatform
    public static void registerConfigClient(ModConfigSpec spec) {}

    /*
    @ExpectPlatform
    public static boolean canPlayerStartSleepNow(final Player player) {
        return false;
    }

    @ExpectPlatform
    public static boolean canPlayerContinueSleepNow(final Player player) {
        return false;
    }
     */
}
