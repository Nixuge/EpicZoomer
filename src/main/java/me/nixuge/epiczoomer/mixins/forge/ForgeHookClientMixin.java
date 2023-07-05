package me.nixuge.epiczoomer.mixins.forge;

import me.nixuge.epiczoomer.zoom.ZoomHandler;
import me.nixuge.epiczoomer.zoom.properties.ForgeHookClientProperties;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeHooksClient.class)
public class ForgeHookClientMixin {
//    @ModifyArgs(method = "getFOVModifier", at = @At("HEAD"), remap = false)
//    private static void owo(Args args) {
//    }

    /**
     * @author Nixuge
     * @reason ModifyArgs fails for some reason. Too tired to understand the error.
     */
    @Overwrite
    public static float getFOVModifier(EntityRenderer renderer, Entity entity, Block block, double renderPartialTicks, float fov) {
        if (ForgeHookClientProperties.isShouldChangeFov()) {
            fov = ZoomHandler.runZoomLogic(fov);
            ForgeHookClientProperties.setShouldChangeFov(false);
        }
        EntityViewRenderEvent.FOVModifier event = new EntityViewRenderEvent.FOVModifier(renderer, entity, block, renderPartialTicks, fov);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getFOV();
    }
}
