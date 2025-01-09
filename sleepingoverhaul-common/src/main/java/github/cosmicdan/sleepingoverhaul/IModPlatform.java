package github.cosmicdan.sleepingoverhaul;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public interface IModPlatform {
    void registerConfig(final ModConfig.Type type, final IConfigSpec<ForgeConfigSpec> spec);

    boolean canPlayerSleepNow(final Player player);
}
