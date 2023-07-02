package me.nixuge.epiczoomer.mixins;

import me.nixuge.epiczoomer.keybinds.Keybinds;
import me.nixuge.epiczoomer.manager.ZoomProperties;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    // Default zoom is 100% and needs to get a function to get it to 4
    // + have it exponantial so it zooms the same amount close and further away
    // Formula: e^{.100x}\ = 4
    // Which has x =~ 0.01386294
    private static float ZOOM_MULTIPLICATOR = 0.01386294f;

    @Shadow
    private Minecraft mc;

    // Note: this mixin does NOT support Optifine.
    @Redirect(method = "getFOVModifier", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;getFOVModifier(Lnet/minecraft/client/renderer/EntityRenderer;Lnet/minecraft/entity/Entity;Lnet/minecraft/block/Block;DF)F"))
    public float getFOVModifierRedirector(EntityRenderer renderer, Entity entity, Block block, double renderPartialTicks, float fov) {
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

        return net.minecraftforge.client.ForgeHooksClient.getFOVModifier(renderer, entity, block, renderPartialTicks, fov);
    }

}
