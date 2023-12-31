package com.cosmicdan.sleepingoverhaul.mixin.injection.client;

import com.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import com.cosmicdan.sleepingoverhaul.server.ServerState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Modifications to the in-bed chat screen:
 *  - Adds a "Sleep" button next to "Leave Bed", if configured
 *  - Activates "Sleep" if player presses ENTER with no text, if configured.
 *  - Also prevents players from requesting a wake packet (the server will ignore the packet anyway, if configured).
 * @author Daniel 'CosmicDan' Connolly
 */
@SuppressWarnings("MethodMayBeStatic")
@Environment(EnvType.CLIENT)
@Mixin(InBedChatScreen.class)
public abstract class InBedChatScreenMixin extends ChatScreen {
    // TODO: No UI feedback on Sleep (button doesn't disable). Also check sleep message during day.
    //       Might be related to PACKET_REALLY_SLEEPING failure...? Or maybe diff InBedChatScreen between 1.18.2 and 1.20.1...
    protected InBedChatScreenMixin(final String string) {
        super(string);
    }

    /**
     * Support for requesting Sleep by pressing ENTER when there is no chat input
     */
    @Inject(
            method = "keyPressed(III)Z",
            at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/EditBox.setValue (Ljava/lang/String;)V"),
            require = 1, allow = 1
    )
    public final void onClearChatEntry(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (SleepingOverhaul.serverConfig.bedRestOnEnter.get()) {
            if (SleepingOverhaul.clientState.isSleepButtonActive()) {
                if (input.getValue().isEmpty()) {
                    // try to sleep if ENTER was pressed and there was no input (not just whitespace)
                    if (minecraft != null)
                        onClickSleep(minecraft.level);
                }
            }
        }
    }

    /**
     * Adds the "Sleep" button on bed chat screen
     */
    @WrapOperation(
            method = "init()V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/InBedChatScreen.addRenderableWidget (Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"),
            require = 1, allow = 1
    )
    public final GuiEventListener onInitAddWidget(final InBedChatScreen self, final GuiEventListener guiEventListener, Operation<GuiEventListener> original) {
        if (guiEventListener instanceof Button buttonLeave) {
            if (SleepingOverhaul.clientState.getTimelapseCinematicStage() == 0) {
                if (SleepingOverhaul.serverConfig.bedRestEnabled.get()) {
                    // reduce buttonLeave width and move 5px left
                    buttonLeave.setWidth(100);
                    buttonLeave.setX(buttonLeave.getX() - 5);
                    // add our new "Sleep" button with same dimensions, 5px to the right of screen center

                    final Button sleepButton = new Button.Builder(
                            Component.translatable("gui.sleepingoverhaul.sleepButton"),
                            (Button button) -> onClickSleep(Minecraft.getInstance().level)
                    ).bounds((width / 2) + 5, buttonLeave.getY(), 100, 20
                    ).build();

                    addRenderableWidget(sleepButton);
                    SleepingOverhaul.clientState.sleepButtonAssign(sleepButton);
                }
                // keep a reference to buttonLeave
                SleepingOverhaul.clientState.leaveBedButtonAssign(buttonLeave);
            } else {
                // timelapse is active; don't add sleep button
                // additionally rather than returing null, we will just set buttonLeave to invisible to be safe
                buttonLeave.visible = false;
            }
            // return the buttonLeave result since it's required, InBedChatScreen doesn't use it though
            return addRenderableWidget(buttonLeave);
        }
        throw new RuntimeException("onInitAddWidget was not called with a button, eh? Tell CosmicDan to fix this...");
    }

    /**
     * Prevent players from waking during timelapse
     * @param ci
     */
    @Inject(
            method = "sendWakeUp()V",
            at = @At("HEAD"),
            cancellable = true,
            require = 1, allow = 1
    )
    public final void onSendWakeup(final CallbackInfo ci) {
        if (SleepingOverhaul.clientState.getTimelapseCinematicStage() != 0)
            ci.cancel(); // cancel sending wake packets if timelapse is active
    }

    /**
     * Hook action handler for when player presses Sleep.
     * Sends our custom "really sleeping" packet to the server.
     */
    private void onClickSleep(final Level level) {
        SleepingOverhaul.clientState.sleepButtonDisable();
        final FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(true);
        NetworkManager.sendToServer(ServerState.PACKET_REALLY_SLEEPING, buf);
    }

}
