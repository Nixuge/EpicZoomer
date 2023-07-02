package me.nixuge.epiczoomer.manager;

import lombok.Getter;
import lombok.Setter;

public class ZoomProperties {
    // Actual zoom
    @Setter
    @Getter
    private static int zoomPercent = 1;
    // Targeted zoom
    @Getter
    private static int zoomTarget = 1;
    // If is currently zooming or not
    @Getter
    private static boolean isZooming = false;
    // If zoom has got an update or not
    @Getter
    @Setter
    private static boolean zoomGotUpdate = false;

    public static void enableZoom() {
        isZooming = true;
        zoomTarget = 100;
    }

    public static void disableZoom() {
        isZooming = false;
        zoomGotUpdate = false;
        zoomTarget = 1;
    }
    public static void add25() {
        zoomTarget += 25;
    }
    public static void remove25() {
        if (zoomTarget > 25)
            zoomTarget -= 25;
    }
}
