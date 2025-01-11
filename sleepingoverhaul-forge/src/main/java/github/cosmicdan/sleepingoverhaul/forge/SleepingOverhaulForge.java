package github.cosmicdan.sleepingoverhaul.forge;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SleepingOverhaul.MOD_ID)
public class SleepingOverhaulForge {
    private final SleepingOverhaul INSTANCE;

    public SleepingOverhaulForge() {
        EventBuses.registerModEventBus(SleepingOverhaul.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        INSTANCE = new SleepingOverhaul(new ModPlatformForge());

        //MinecraftForge.EVENT_BUS.register(new TestEventsForge());
    }
}
