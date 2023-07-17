package me.nixuge.epiczoomer.zoom.properties;

import lombok.Getter;
import lombok.Setter;
import me.nixuge.epiczoomer.config.ConfigCache;
import me.nixuge.epiczoomer.zoom.ZoomObject;
import net.minecraft.client.Minecraft;

public class ZoomProperties {
    // Actual zoom
    @Setter
    @Getter
    private static double zoomPercent = 1;
    // Targeted zoom
    @Getter
    private static ZoomObject zoomObject;
    // If is currently zooming or not
    @Getter
    private static boolean isZooming = false;


    public static void enableZoom() {
        zoomObject = new ZoomObject(1, 100);
        isZooming = true;

        if (zoomObject.isSmoothCameraEnabled())
            Minecraft.getMinecraft().gameSettings.smoothCamera = true;
    }

    public static void disableZoom() {
        zoomObject.updateTargetPercent(1);
        isZooming = false;

        if (zoomObject.isSmoothCameraEnabled())
            Minecraft.getMinecraft().gameSettings.smoothCamera = false;
    }
    public static void scrollUp() {
        // Todo: custom values
        zoomObject.updateTargetPercent(zoomObject.getTargetZoomPercent() + 25);
    }
    public static void scrollDown() {
        // Todo: custom values
        if (zoomObject.getTargetZoomPercent() > 25)
            // todo: move that over to the object
            zoomObject.updateTargetPercent(zoomObject.getTargetZoomPercent() - 25);
    }

    public static void destroyZoomObject() {
        // Not sure if this is actually useful tbh
        // but yeah might as well not have a dangling zoom object for useless checks
        zoomObject = null;
    }
}
