package cc.avie.pingdisplay.render;

import cc.avie.pingdisplay.PingDisplayConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class PingDisplayRender {

    public static void renderPingText(Minecraft mc, GuiGraphicsExtractor context, int width, int x, int y, PlayerInfo entry) {
        int ping = entry.getLatency();
        String pingText = getPingText(ping);
        int color = getPingColor(ping);

        Font font = mc.font;
        int textX = x + width - font.width(pingText);
        context.text(font, pingText, textX, y, color, PingDisplayConfig.TEXT_SHADOW.get());
    }

    public static String getPingText(int ping) {
        if (ping < 0) {
            return PingDisplayConfig.UNKNOWN_TEXT.get();
        }
        return PingDisplayConfig.SHOW_MS.get() ? ping + "ms" : String.valueOf(ping);
    }

    public static int getPingColor(int ping) {
        if (ping < 0) {
            return PingDisplayConfig.COLOR_UNKNOWN.get();
        }

        int good = PingDisplayConfig.GOOD_BELOW.get();
        int ok = Math.max(good + 1, PingDisplayConfig.OK_BELOW.get());
        int bad = Math.max(ok + 1, PingDisplayConfig.BAD_BELOW.get());

        if (PingDisplayConfig.COLOR_MODE.get() == PingDisplayConfig.ColorMode.GRADIENT) {
            if (ping <= good) {
                return PingDisplayConfig.COLOR_GOOD.get();
            }
            if (ping <= ok) {
                return lerp(ping, good, ok, PingDisplayConfig.COLOR_GOOD.get(), PingDisplayConfig.COLOR_OK.get());
            }
            if (ping <= bad) {
                return lerp(ping, ok, bad, PingDisplayConfig.COLOR_OK.get(), PingDisplayConfig.COLOR_BAD.get());
            }
            return lerp(ping, bad, bad * 2, PingDisplayConfig.COLOR_BAD.get(), PingDisplayConfig.COLOR_TERRIBLE.get());
        }

        if (ping < good) return PingDisplayConfig.COLOR_GOOD.get();
        if (ping < ok) return PingDisplayConfig.COLOR_OK.get();
        if (ping < bad) return PingDisplayConfig.COLOR_BAD.get();
        return PingDisplayConfig.COLOR_TERRIBLE.get();
    }

    private static int lerp(int ping, int from, int to, int fromColor, int toColor) {
        float delta = Math.min(1f, (ping - from) / (float) (to - from));
        int color = 0;
        for (int shift = 0; shift <= 24; shift += 8) {
            int a = fromColor >>> shift & 0xFF;
            int b = toColor >>> shift & 0xFF;
            color |= Math.round(a + (b - a) * delta) << shift;
        }
        return color;
    }

    public static Component nametagWithPing(Entity entity, Component nameTag) {
        if (!PingDisplayConfig.SHOW_NAMETAG_PING.get() || !(entity instanceof AbstractClientPlayer player) || nameTag == null) {
            return null;
        }
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return null;
        }
        PlayerInfo info = connection.getPlayerInfo(player.getUUID());
        if (info == null) {
            return null;
        }

        int ping = info.getLatency();
        String pingText = getPingText(ping);
        int color = getPingColor(ping) & 0xFFFFFF;
        int bracketColor = PingDisplayConfig.BRACKET_COLOR.get() & 0xFFFFFF;

        return nameTag.copy()
                .append(Component.literal(" (").withStyle(style -> style.withColor(bracketColor)))
                .append(Component.literal(pingText).withStyle(style -> style.withColor(color)))
                .append(Component.literal(")").withStyle(style -> style.withColor(bracketColor)));
    }
}
