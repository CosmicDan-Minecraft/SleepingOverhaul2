package com.cosmicdan.sleepingoverhaul.server;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ServerConfig {
    private static final String sectionGeneral = "general";
    public final ForgeConfigSpec.EnumValue<SleepAction> sleepAction;
    private static final String sleepActionTxt = "The action to perform when all players are sleeping";

    private static final String sectionMorning = "morning";
    // TODO: Disabled because client (or player) has a "prevent sleep during day" hardcoded check
    //public final ForgeConfigSpec.IntValue morningHour;
    //private static final String morningHourTxt = "The hour of day to consider as 'morning' (for waking players)";
    //public final ForgeConfigSpec.IntValue morningMinute;
    //private static final String morningMinuteTxt = "The minute of day to consider as 'morning', past the hour (for waking players)";
    public final ForgeConfigSpec.BooleanValue morningResetWeather;
    private static final String morningResetWeatherTxt = "Reset the weather on morning (when players wake) if raining.";

    // TODO: Remove these for now, and make it impossible to wakeup from timelapse.
    //       First remove the Sleep and Leave Bed buttons, then prevent server from accepting wake packet (check InBedChatScreen)
    //private static final String sectionTimelapse = "timelapse";
    //public final ForgeConfigSpec.BooleanValue timelapsePreventWakeup;
    //private static final String timelapsePreventWakeupTxt = "Prevents players from waking up during timelapse (players will only wake when morning arrives)";
    //public final ForgeConfigSpec.BooleanValue timelapsePreventMoving;
    //private static final String timelapsePreventMovingTxt = "Prevents players from moving during timelapse (in case vote passes with some players awake)";

    private static final String sectionBedrest = "bedRest";
    public final ForgeConfigSpec.BooleanValue bedRestEnabled;
    private static final String bedRestEnabledTxt = "Allows players to rest in a bed without sleeping, adding a 'Sleep' button next to 'Leave Bed'";
    public final ForgeConfigSpec.BooleanValue bedRestOnEnter;
    private static final String bedRestOnEnterTxt = "Allows pressing Enter with no chat text to activate Sleep while resting. Requires the above bedRestEnabled to be true.";

    //private static final String sectionRestrictions = "restrictions";
    //public final ForgeConfigSpec.BooleanValue dayCheckOverride;

    public ServerConfig(final Builder builder) {
        builder.push(sectionGeneral); // TODO: test translation key...?

        sleepAction = builder
                .comment(sleepActionTxt)
                //.translation("config.sleepingoverhaul.sleepAction") // TODO: what's the point of translation if only comment is used?
                .defineEnum("sleepAction", SleepAction.Timelapse);

        builder.pop();

        builder.push(sectionMorning);
        /*
        morningHour = builder
                .comment(morningHourTxt)
                .defineInRange("morningHour", 6, 0, 23);
        morningMinute = builder
                .comment(morningMinuteTxt)
                .defineInRange("morningMinute", 0, 0, 59);

         */
        morningResetWeather = builder
                .comment(morningResetWeatherTxt)
                .define("morningResetWeather", true);
        builder.pop();

        /*
        builder.push(sectionTimelapse);
        timelapsePreventWakeup = builder
                .comment(timelapsePreventWakeupTxt)
                .define("timelapsePreventWakeup", true);
        timelapsePreventMoving = builder
                .comment(timelapsePreventMovingTxt)
                .define("timelapsePreventMoving", true);
        builder.pop();
         */

        builder.push(sectionBedrest);
        bedRestEnabled = builder
                .comment(bedRestEnabledTxt)
                .define("bedRestEnabled", true);
        bedRestOnEnter = builder
                .comment(bedRestOnEnterTxt)
                .define("bedRestWithChatEnter", true);
        builder.pop();
    }

    public enum SleepAction {
        Timelapse,
        SkipToDay,
        Nothing
    }
}