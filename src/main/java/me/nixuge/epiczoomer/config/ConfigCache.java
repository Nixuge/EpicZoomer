package me.nixuge.epiczoomer.config;

import lombok.Getter;
import me.nixuge.epiczoomer.McMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Getter
public class ConfigCache {
    private final Configuration configuration;

    private boolean adaptativeZoomTime;
    private int maxZoomTime;
    private int msForHundredPercentZoom;
    private boolean smoothCamera;

    public ConfigCache(final Configuration configuration) {
        this.configuration = configuration;
        this.loadConfiguration();
        this.configuration.save();
    }

    private void loadConfiguration() {
        this.adaptativeZoomTime = this.configuration.getBoolean(
                "Adaptative zoom time",
                "General",
                false,
                "If set to false, will adapt the zoom time based on how much you're zooming. Otherwise, use the fixed value from below."
        );
        this.maxZoomTime = this.configuration.getInt(
                "Max zoom time",
                "General",
                500,
                0,
                5000,
                "Max zoom time possible. No effect if \"adaptative zoom time\" is set to false"
        );
        this.msForHundredPercentZoom = this.configuration.getInt(
                "MS per 100 percent zoom",
                "General",
                200,
                0,
                5000,
                "Self explanatory. If \"adaptative zoom time\" set to false, this will be the fixed time for every zoom animation."
        );

        this.smoothCamera = configuration.getBoolean(
                "Smooth camera",
                "General",
                false,
                "If set to true, smooth camera will be automatically enabled when zooming"
        );
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        this.configuration.save();
        if (event.modID.equalsIgnoreCase(McMod.MOD_ID)) {
            this.loadConfiguration();
        }
    }
}
