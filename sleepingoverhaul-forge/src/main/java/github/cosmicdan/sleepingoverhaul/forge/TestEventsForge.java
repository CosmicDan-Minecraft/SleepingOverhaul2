package github.cosmicdan.sleepingoverhaul.forge;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TestEventsForge {

    @SubscribeEvent
    public void onSleepFinishedTime(SleepFinishedTimeEvent event) {
        SleepingOverhaul.LOGGER.info("~ Fired: SleepFinishedTimeEvent");
    }

    @SubscribeEvent
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        SleepingOverhaul.LOGGER.info("~ Fired: PlayerSleepInBedEvent");
    }

    @SubscribeEvent
    public void onPlayerWakeUp(PlayerWakeUpEvent event) {
        SleepingOverhaul.LOGGER.info("~ Fired: PlayerWakeUpEvent");
    }

    @SubscribeEvent
    public void onSleepingLocationCheck(SleepingLocationCheckEvent event) {
        SleepingOverhaul.LOGGER.info("~ Fired: SleepingLocationCheckEvent");
    }

    @SubscribeEvent
    public void onSleepingTimeCheck(SleepingTimeCheckEvent event) {
        SleepingOverhaul.LOGGER.info("~ Fired: SleepingTimeCheckEvent");
    }
}

