package me.nixuge.epiczoomer.manager;

import me.nixuge.epiczoomer.keybinds.Keybinds;
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
            } else if (ZoomProperties.isZoomGotUpdate()) {
                mc.renderGlobal.setDisplayListEntitiesDirty();
            } 
            // else {
                // fov = setFov(fov) * getModifier(true);
                // mc.renderGlobal.setDisplayListEntitiesDirty();
            // }
        } else if (ZoomProperties.isZooming()){
            mc.renderGlobal.setDisplayListEntitiesDirty();
            ZoomProperties.disableZoom();
        }
        mc.renderGlobal.setDisplayListEntitiesDirty();

        updateZoomProgress(false);

        return calculateFov(fov);
    }

    public static float calculateFov(float fov) {
        return fov / (float)(Math.exp(ZOOM_MULTIPLICATOR * ZoomProperties.getZoomPercent()));
    }

    // Note:
    // This is MORE than suboptimal.
    // In addition to being linear and not a very nice bezier, it's also tied to the framerate.
    // Will revisit once I have the chance to. For now, going onto another branch & in beta.
    
    // private static long lastMillis = System.currentTimeMillis();
    public static void updateZoomProgress(boolean zooming) {
        // float t = 1F;
        // long time = System.currentTimeMillis();
        // long timeSinceLastChange = time - lastMillis;

        int target = ZoomProperties.getZoomTarget();
        int current = ZoomProperties.getZoomPercent();
        if (target == 1 && current == 1)
            return;
        
        int difference = Math.abs(target - current);
        if (Math.abs(target - current) == 1)
            return;
        
        int valueChange = 1;
        if (difference > 1000) {
            valueChange = 7;
        } else if (difference > 400) {
            valueChange = 4;
        } else if (difference > 200) {
            valueChange = 2;
        }

        // System.out.println("current: " + current + " target:" + target);
        if (target > current)
            ZoomProperties.setZoomPercent(current + valueChange);
        else
            ZoomProperties.setZoomPercent(current - valueChange);        
    }
}
