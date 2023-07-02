package me.nixuge.epiczoomer.manager;

import lombok.Getter;
import lombok.Setter;
import me.nixuge.epiczoomer.keybinds.Keybinds;
import net.minecraft.client.Minecraft;

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

    // TODO: move that elsewhere
    // Actually TODO: refactor the whole thing lol
    
    // Default zoom is 100% and needs to get a function to get it to 4
    // + have it exponential, so it zooms the same amount close and further away
    // Formula: e^{.100x}\ = 4
    // Which has x =~ 0.01386294
    private static final float ZOOM_MULTIPLICATOR = 0.01386294f;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float runZoomLogic(float fov) {
        if (Keybinds.zoom.isKeyDown()) {
            // This unneccessary looking "if" fixes a render glitch when changing the zoom pos/when enabling zoom
            if (!ZoomProperties.isZooming() || ZoomProperties.isZoomGotUpdate()) {
                ZoomProperties.setZooming(true);
                ZoomProperties.setZoomGotUpdate(false);
                mc.renderGlobal.setDisplayListEntitiesDirty();
            } else {
                fov /= (Math.exp(ZOOM_MULTIPLICATOR * ZoomProperties.getZoomPercent()));
            }
            // Same for this else if
        } else if (ZoomProperties.isZooming()){
            mc.renderGlobal.setDisplayListEntitiesDirty();
            ZoomProperties.disableZoom();
        }
        return fov;
    }
}
