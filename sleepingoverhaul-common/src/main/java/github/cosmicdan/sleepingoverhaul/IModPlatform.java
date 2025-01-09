package github.cosmicdan.sleepingoverhaul;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public interface IModPlatform {
    void registerConfigServer(ModConfigSpec spec);

    void registerConfigClient(ModConfigSpec spec);

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
