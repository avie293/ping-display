package cc.avie.pingdisplay.neoforge;

import cc.avie.pingdisplay.PingDisplayConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = "ping_display", dist = Dist.CLIENT)
public class PingDisplayNeoForge {

    public PingDisplayNeoForge(ModContainer container) {
        // NeoForge mod ids can't contain "-", TabbyLib needs the id of the mod on this loader
        PingDisplayConfig.init(container.getModId());
    }
}
