package github.cosmicdan.sleepingoverhaul.forge;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(SleepingOverhaul.MOD_ID)
public class SleepingOverhaulNeoForge {
    private final SleepingOverhaul INSTANCE;
    public static ModContainer CONTAINER;

    public SleepingOverhaulNeoForge(ModContainer container, IEventBus modBus) {
        CONTAINER = container;
        INSTANCE = new SleepingOverhaul(new ModPlatformForge());

        // DEBUG/TESTING ONLY
        //TestEventsNeoForge.subTestEvents();
    }
}
