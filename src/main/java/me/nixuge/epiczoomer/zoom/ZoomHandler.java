package me.nixuge.epiczoomer.zoom;

import me.nixuge.epiczoomer.keybinds.Keybinds;
import me.nixuge.epiczoomer.zoom.properties.ZoomProperties;
import net.minecraft.client.Minecraft;

public class ZoomHandler {
    // TODO: move that elsewhere
    // Actually TODO: refactor the whole thing lol
    
    // Default zoom is 100% and needs to get a function to get it to 4
    // + have it exponential, so it zooms the same amount close and further away
    // Formula: e^{.100x}\ = 4
    // Which has x =~ 0.01386294
    private static final float ZOOM_MULTIPLICATOR = 0.01386294f;
    private static final Minecraft mc = Minecraft.getMinecraft();

    // private static long lastMillis;

    public static float runZoomLogic(float fov) {
        if (Keybinds.zoom.isKeyDown()) {
            // This unneccessary looking "if" fixes a render glitch when changing the zoom pos/when enabling zoom
            if (!ZoomProperties.isZooming()) {
                // lastMillis = System.currentTimeMillis();
                ZoomProperties.enableZoom();
                mc.renderGlobal.setDisplayListEntitiesDirty();
            }
        } else if (ZoomProperties.isZooming()){
            mc.renderGlobal.setDisplayListEntitiesDirty();
            ZoomProperties.disableZoom();
        }
        

        ZoomObject zoomObject = ZoomProperties.getZoomObject();

        if (zoomObject == null)
            return fov;

        if (updateZoomProgress(zoomObject)) 
            mc.renderGlobal.setDisplayListEntitiesDirty();
            
        return calculateZoomFov(fov);
    }

    public static float calculateZoomFov(float fov) {
        return fov / (float)(Math.exp(ZOOM_MULTIPLICATOR * ZoomProperties.getZoomPercent()));
    }

    /**
     * @return true if the zoom % got updated, false otherwise.
     */
    public static boolean updateZoomProgress(ZoomObject zoomObject) {
        if (zoomObject.hasAnimationEnded()) {
            if (zoomObject.getTargetZoomPercent() == 1) {
                ZoomProperties.setZoomPercent(zoomObject.getTargetZoomPercent()); //Just in case, to avoid float inaccuracies
                ZoomProperties.destroyZoomObject();
            }
                
            return false;
        }

        ZoomProperties.setZoomPercent(zoomObject.getPercentageToSet());
        return true;    
    }
}
