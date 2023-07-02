package me.nixuge.epiczoomer.keybinds;

import me.nixuge.epiczoomer.manager.ZoomProperties;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeyInputHandler {
    // Todo: move that over to the hotbar to avoid scrolling it
    @SubscribeEvent
    public void onKeyInput(MouseEvent event) {
        if (!ZoomProperties.isZooming() || event.dwheel == 0)
            return;
        ZoomProperties.setZoomGotUpdate(true);
        if (event.dwheel < 0)
            ZoomProperties.remove25();
        else
            ZoomProperties.add25();
    }
}