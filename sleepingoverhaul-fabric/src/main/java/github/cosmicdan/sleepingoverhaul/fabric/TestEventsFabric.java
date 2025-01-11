package github.cosmicdan.sleepingoverhaul.fabric;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.InteractionResult;

public class TestEventsFabric {
    public static void subTestEvents() {
        EntitySleepEvents.START_SLEEPING.register((entity, sleepingPos) -> {
            SleepingOverhaul.LOGGER.info("~ Fired: START_SLEEPING");
        });
        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            SleepingOverhaul.LOGGER.info("~ Fired: STOP_SLEEPING");
        });
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
            SleepingOverhaul.LOGGER.info("~ Fired: ALLOW_BED");
            return InteractionResult.PASS;
        });
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
            SleepingOverhaul.LOGGER.info("~ Fired: ALLOW_SLEEP_TIME");
            return InteractionResult.PASS;
        });
    }
}
