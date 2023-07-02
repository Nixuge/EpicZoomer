package me.nixuge.epiczoomer.manager;

import lombok.Getter;
import lombok.Setter;

public class ZoomProperties {
    @Getter
    private static int zoomPercent = 100;
    @Getter
    @Setter
    private static boolean isZooming = false;
    @Getter
    @Setter
    private static boolean zoomGotUpdate = false;

    public static void disableZoom() {
        isZooming = false;
        zoomGotUpdate = false;
        zoomPercent = 100;
    }
    public static void add25() {
        zoomPercent += 25;
    }
    public static void remove25() {
        if (zoomPercent > 25)
            zoomPercent -= 25;
    }
}
