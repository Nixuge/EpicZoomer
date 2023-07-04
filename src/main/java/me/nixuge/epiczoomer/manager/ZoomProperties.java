package me.nixuge.epiczoomer.manager;

import lombok.Getter;
import lombok.Setter;
import me.nixuge.epiczoomer.manager.obj.ZoomObject;

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
    // If zoom has got an update or not
    @Getter
    @Setter
    private static boolean zoomGotUpdate = false;


    public static void enableZoom() {
        zoomObject = new ZoomObject(1, 100);
        isZooming = true;
    }

    public static void disableZoom() {
        zoomObject.updateTargetPercent(1);
        isZooming = false;
        zoomGotUpdate = false;
    }
    public static void add25() {
        zoomGotUpdate = true;
        zoomObject.updateTargetPercent(zoomObject.getTargetZoomPercent() + 25);
    }
    public static void remove25() {
        zoomGotUpdate = true;
        // todo: move that over to the object
        if (zoomObject.getTargetZoomPercent() > 25)
            zoomObject.updateTargetPercent(zoomObject.getTargetZoomPercent() - 25);
    }

    public static void destroyZoomObject() {
        // Not sure if this is actually useful tbh
        // but yeah might as well not have a dangling zoom object
        zoomObject = null;
    }
}
