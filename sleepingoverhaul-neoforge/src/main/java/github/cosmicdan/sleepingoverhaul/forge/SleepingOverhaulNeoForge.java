package github.cosmicdan.sleepingoverhaul.forge;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import github.cosmicdan.sleepingoverhaul.mixin.proxy.PlayerMixinProxy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;

@Mod(SleepingOverhaul.MOD_ID)
public class SleepingOverhaulNeoForge {
    private final SleepingOverhaul INSTANCE;
    public static ModContainer CONTAINER;

    public SleepingOverhaulNeoForge(ModContainer container, IEventBus modBus) {
        CONTAINER = container;
        INSTANCE = new SleepingOverhaul(new ModPlatformForge());

        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onCanContinueSleeping);

        // DEBUG/TESTING ONLY
        //TestEventsNeoForge.subTestEvents();
    }

    /**
     * For Bed Rest; force allow sleeping if they're not really sleeping.
     * This event is mostly for Comforts Hammock compat. and likely other mod-added beds.
     */
    private void onCanContinueSleeping(CanContinueSleepingEvent event) {
        if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
            if (event.getEntity() instanceof Player player) {
                if (!((PlayerMixinProxy) player).so2_$isReallySleeping())
                    event.setContinueSleeping(true);
            }
        }
    }
}
