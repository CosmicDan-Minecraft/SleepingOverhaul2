package github.cosmicdan.sleepingoverhaul;

import dev.architectury.event.events.common.TickEvent;
import github.cosmicdan.sleepingoverhaul.client.ClientConfig;
import github.cosmicdan.sleepingoverhaul.client.ClientState;
import github.cosmicdan.sleepingoverhaul.server.ClientStateDummy;
import github.cosmicdan.sleepingoverhaul.server.ServerConfig;
import github.cosmicdan.sleepingoverhaul.server.ServerState;
import com.mojang.logging.LogUtils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

@SuppressWarnings({"StaticNonFinalField", "PublicField"})
public class SleepingOverhaul {
    public static final String MOD_ID = "sleepingoverhaul";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IModPlatform MODPLATFORM;

    public static ServerState serverState = null;
    public static IClientState clientState = null;

    public static ServerConfig serverConfig = null;
    public static ClientConfig clientConfig = null;

    public static final ResourceLocation PACKET_TRY_REALLY_SLEEPING = ResourceLocation.fromNamespaceAndPath(SleepingOverhaul.MOD_ID, "is_really_sleeping");
    public static final ResourceLocation PACKET_TIMELAPSE_CHANGE = ResourceLocation.fromNamespaceAndPath(SleepingOverhaul.MOD_ID, "timelapse_change");

    //public static void init() {
    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public SleepingOverhaul(final IModPlatform modPlatform) {
        MODPLATFORM = modPlatform;
        // Register server/world config and state
        final Pair<ServerConfig, ModConfigSpec> specPairServer = new ModConfigSpec.Builder().configure(ServerConfig::new);
        serverConfig = specPairServer.getLeft();
        MODPLATFORM.registerConfigServer(specPairServer.getRight());
        serverState = new ServerState();
        if (Platform.getEnvironment() == Env.CLIENT) {
            // register client config and state
            final Pair<ClientConfig, ModConfigSpec> specPairClient = new ModConfigSpec.Builder().configure(ClientConfig::new);
            clientConfig = specPairClient.getLeft();
            MODPLATFORM.registerConfigClient(specPairClient.getRight());
            clientState = new ClientState();
        } else // dummy client state for dedicated server
            clientState = new ClientStateDummy();


        EntityEvent.LIVING_HURT.register(this::onLivingHurt);
        TickEvent.SERVER_POST.register(serverState::onServerTickPost);
    }

    private EventResult onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        EventResult eventResult = EventResult.pass(); // default = pass it on
        if (serverState.isTimelapseActive()) {
            if (entity instanceof ServerPlayer player) {
                if (! source.getMsgId().equals(TimelapseKillDamageSource.MSG_ID)) {
                    final float adjustedDamage = serverState.getPlayerHurtAdj(player, source, amount);
                    if (Float.isNaN(adjustedDamage))
                        // NaN = damage was cancelled
                        eventResult = EventResult.interruptFalse();
                    else if (Float.isInfinite(adjustedDamage)) {
                        // infinite = insta-kill configured
                        eventResult = EventResult.interruptFalse();
                        player.hurt(new TimelapseKillDamageSource(), Float.MAX_VALUE);
                    }
                    // Note: Player will always leave bed if they receive any damage;
                }
            }
        }
        return eventResult;
    }
}
