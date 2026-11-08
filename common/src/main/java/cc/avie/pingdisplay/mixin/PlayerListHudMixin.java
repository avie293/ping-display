package cc.avie.pingdisplay.mixin;

import cc.avie.pingdisplay.PingDisplayConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import cc.avie.pingdisplay.render.PingDisplayRender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public class PlayerListHudMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyConstant(method = "extractRenderState", constant = @Constant(intValue = 13))
    private int modifyPingAreaWidth(int original) {
        return PingDisplayConfig.SHOW_IN_TAB_LIST.get() ? original + 23 : original;
    }

    @Inject(method = "extractPingIcon(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void replacePingIconWithText(GuiGraphicsExtractor context, int slotWidth, int xo, int yo, PlayerInfo info, CallbackInfo ci) {
        if (!PingDisplayConfig.SHOW_IN_TAB_LIST.get()) {
            return;
        }
        PingDisplayRender.renderPingText(minecraft, context, slotWidth, xo, yo, info);
        ci.cancel();
    }
}
