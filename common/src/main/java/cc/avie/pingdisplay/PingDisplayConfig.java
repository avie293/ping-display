package cc.avie.pingdisplay;

import cc.avie.tabbylib.api.TabbyConfig;
import cc.avie.tabbylib.api.option.BooleanOption;
import cc.avie.tabbylib.api.option.ColorOption;
import cc.avie.tabbylib.api.option.EnumOption;
import cc.avie.tabbylib.api.option.IntOption;
import cc.avie.tabbylib.api.option.Options;
import cc.avie.tabbylib.api.option.StringOption;
import net.minecraft.network.chat.Component;

public final class PingDisplayConfig {
    public enum ColorMode {
        STEPS,
        GRADIENT
    }

    public static final BooleanOption SHOW_IN_TAB_LIST = Options.toggle("showInTabList", true);
    public static final BooleanOption SHOW_MS = Options.toggle("showMs", true);
    public static final BooleanOption TEXT_SHADOW = Options.toggle("textShadow", true);
    public static final StringOption UNKNOWN_TEXT = Options.text("unknownText", "N/A")
        .maxLength(8);

    public static final BooleanOption SHOW_NAMETAG_PING = Options.toggle("showNametagPing", false);
    public static final ColorOption BRACKET_COLOR = Options.color("nametagBracketColor", 0xFFAAAAAA)
        .dependsOn(SHOW_NAMETAG_PING);

    public static final EnumOption<ColorMode> COLOR_MODE = Options.choice("colorMode", ColorMode.STEPS);
    public static final ColorOption COLOR_GOOD = Options.color("pingColorGood", 0xFF00E676);
    public static final ColorOption COLOR_OK = Options.color("pingColorOk", 0xFFD6CD30);
    public static final ColorOption COLOR_BAD = Options.color("pingColorBad", 0xFFF59042);
    public static final ColorOption COLOR_TERRIBLE = Options.color("pingColorTerrible", 0xFFE53935);
    public static final ColorOption COLOR_UNKNOWN = Options.color("pingColorUnknown", 0xFF535353);

    public static final IntOption GOOD_BELOW = Options.integer("goodBelow", 50)
        .slider(10, 300, 5)
        .format(PingDisplayConfig::ms);
    public static final IntOption OK_BELOW = Options.integer("okBelow", 100)
        .slider(20, 500, 5)
        .format(PingDisplayConfig::ms);
    public static final IntOption BAD_BELOW = Options.integer("badBelow", 200)
        .slider(50, 1000, 10)
        .format(PingDisplayConfig::ms);

    private static TabbyConfig config;

    private PingDisplayConfig() {
    }

    public static void init(String modId) {
        config = TabbyConfig.builder(modId)
            .translationId("ping-display")
            .fileName("pingdisplay")
            .category("display", category -> category
                .section("tabList", section -> section.add(SHOW_IN_TAB_LIST, SHOW_MS, TEXT_SHADOW, UNKNOWN_TEXT))
                .section("nametags", section -> section.add(SHOW_NAMETAG_PING, BRACKET_COLOR)))
            .category("colors", category -> category
                .add(COLOR_MODE, COLOR_GOOD, COLOR_OK, COLOR_BAD, COLOR_TERRIBLE, COLOR_UNKNOWN)
                .section("ranges", section -> section
                    .label(Component.translatable("config.ping-display.ranges.description"))
                    .add(GOOD_BELOW, OK_BELOW, BAD_BELOW)))
            .build();
    }

    public static TabbyConfig get() {
        return config;
    }

    private static Component ms(int value) {
        return Component.literal(value + " ms");
    }
}
