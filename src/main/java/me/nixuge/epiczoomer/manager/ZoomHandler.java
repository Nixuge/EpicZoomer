package me.nixuge.epiczoomer.manager;

import me.nixuge.epiczoomer.keybinds.Keybinds;
import me.nixuge.epiczoomer.manager.obj.ZoomObject;
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
        

        if (updateZoomProgress(false))
            mc.renderGlobal.setDisplayListEntitiesDirty();

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
    /**
     * @param zooming
     * @return true if the zoom % got updated, false otherwise.
     */
    public static boolean updateZoomProgress(boolean zooming) {
        // float t = 1F;
        // long time = System.currentTimeMillis();
        // double timeSinceAnimationStart = time - ZoomProperties.getZoomChangeStartTime();

        ZoomObject zoomObject = ZoomProperties.getZoomObject();
    
        if (zoomObject == null) {
            return false;
        }
        if (zoomObject.hasAnimationEnded()) {
            if (zoomObject.getTargetZoomPercent() == 1)
                ZoomProperties.destroyZoomObject();
            return false;
        }

        double prog = zoomObject.getPercentageToSet();
        ZoomProperties.setZoomPercent(prog);
        // if (prog > 0.1 && prog < 1.9)
        //     System.out.println(prog);
        // timeSinceAnimationStart /= 10; // Normalize to 100
        // timeSinceAnimationStart /= 50; // Normalize to 2

        // System.out.println(timeSinceAnimationStart + " vs " + Math.tanh(timeSinceAnimationStart));

        // int target = ZoomProperties.getZoomTarget();
        // double current = ZoomProperties.getZoomPercent();
        // if (target == 1 && current == 1)
        //     return false;
        
        // double animPercent = Math.tanh(timeSinceAnimationStart * .2) + 1;
        // System.out.println(animPercent);

        // double difference = Math.abs(target - current);
        // if (Math.abs(target - current) < 1)
        //     return false;
        
        // int valueChange = 1;
        // if (difference > 1000) {
        //     valueChange = 7;
        // } else if (difference > 400) {
        //     valueChange = 4;
        // } else if (difference > 200) {
        //     valueChange = 2;
        // }

        // System.out.println("current: " + current + " target:" + target);
        // if (target > current)
        //     ZoomProperties.setZoomPercent(current + Math.tanh(timeSinceAnimationStart));
        // else
        //     ZoomProperties.setZoomPercent(current - Math.tanh(timeSinceAnimationStart));    
        
        return true;    
    }
}
