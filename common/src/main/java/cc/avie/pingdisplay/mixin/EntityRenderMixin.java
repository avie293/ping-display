package cc.avie.pingdisplay.mixin;

import cc.avie.pingdisplay.render.PingDisplayRender;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private <T extends Entity, S extends EntityRenderState> void appendPingToNametag(
            T entity, S state, float partialTicks, CallbackInfo ci) {
        Component withPing = PingDisplayRender.nametagWithPing(entity, state.nameTag);
        if (withPing != null) {
            state.nameTag = withPing;
        }
    }
}
