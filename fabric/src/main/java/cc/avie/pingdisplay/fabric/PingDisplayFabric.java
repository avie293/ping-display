package cc.avie.pingdisplay.fabric;

import cc.avie.pingdisplay.PingDisplayConfig;
import net.fabricmc.api.ClientModInitializer;

public class PingDisplayFabric implements ClientModInitializer {
    public static final String MOD_ID = "ping-display";

    @Override
    public void onInitializeClient() {
        PingDisplayConfig.init(MOD_ID);
    }
}
