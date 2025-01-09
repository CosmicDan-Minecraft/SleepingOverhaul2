package github.cosmicdan.sleepingoverhaul.fabric;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.fabricmc.api.ModInitializer;

public class SleepingOverhaulFabric implements ModInitializer {
    private SleepingOverhaul INSTANCE = null;

    @Override
    public void onInitialize() {
        INSTANCE = new SleepingOverhaul();
    }
}
