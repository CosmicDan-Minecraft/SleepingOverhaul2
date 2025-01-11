package github.cosmicdan.sleepingoverhaul.fabric;

import com.mojang.datafixers.util.Either;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import github.cosmicdan.sleepingoverhaul.IModPlatform;
import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;

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

    @Override
    public boolean canPlayerStartSleepNow(ServerPlayer serverPlayer) {
        return canPlayerContinueSleepNow(serverPlayer);
    }

    @Override
    public boolean canPlayerContinueSleepNow(Player player) {
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
