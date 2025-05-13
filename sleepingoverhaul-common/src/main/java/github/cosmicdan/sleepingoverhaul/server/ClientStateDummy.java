package github.cosmicdan.sleepingoverhaul.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.Side;
import github.cosmicdan.sleepingoverhaul.IClientState;
import github.cosmicdan.sleepingoverhaul.networking.ReallySleepingBouncePacket;
import github.cosmicdan.sleepingoverhaul.networking.TimelapseChangePacket;
import net.minecraft.world.entity.player.Player;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ClientStateDummy implements IClientState {

    public ClientStateDummy() {
        NetworkManager.registerReceiver(Side.S2C, TimelapseChangePacket.TYPE, TimelapseChangePacket.STREAM_CODEC, this::noOp);
        NetworkManager.registerReceiver(Side.S2C, ReallySleepingBouncePacket.TYPE, ReallySleepingBouncePacket.STREAM_CODEC, this::noOp);
    }

    private void noOp(Object packet, NetworkManager.PacketContext packetContext) {}

    @Override
    public boolean isSleepButtonActive() {
        return false;
    }

    @Override
    public <T> void leaveBedButtonAssign(T button) {}

    @Override
    public <T> void sleepButtonAssign(final T button) {}

    @Override
    public void setTimelapseCamera(Player player, boolean timelapseEnabled) {}

    @Override
    public int getTimelapseCinematicStage() {
        return 0;
    }

    @Override
    public void advanceTimelapseCinematicStage() {}

    @Override
    public boolean isTimelapseCinematicActive() {return false;}

    @Override
    public void sleepButtonEnable(boolean enable) {}

    @Override
    public void onClickSleep() {}
}
